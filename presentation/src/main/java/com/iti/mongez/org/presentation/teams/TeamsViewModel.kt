package com.iti.mongez.org.presentation.teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.team.usecase.CreateTeamUseCase
import com.iti.mongez.org.domain.team.usecase.GetTeamsUseCase
import com.iti.mongez.org.domain.team.usecase.UploadTeamPhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(
    private val getTeamsUseCase: GetTeamsUseCase,
    private val createTeamUseCase: CreateTeamUseCase,
    private val uploadTeamPhotoUseCase: UploadTeamPhotoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TeamsState())
    val uiState: StateFlow<TeamsState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<TeamsEffect>()
    val effect: SharedFlow<TeamsEffect> = _effect.asSharedFlow()

    init {
        onIntent(TeamsIntent.LoadTeams)
    }

    fun onIntent(intent: TeamsIntent) {
        when (intent) {
            is TeamsIntent.LoadTeams -> loadTeams()
            is TeamsIntent.OpenAddTeamSheet -> _uiState.update { it.copy(isAddTeamSheetVisible = true) }
            is TeamsIntent.CloseAddTeamSheet -> _uiState.update { it.copy(isAddTeamSheetVisible = false, newTeamName = "", newTeamInviteCode = "", newTeamPhotoUrl = null) }
            is TeamsIntent.UpdateNewTeamName -> _uiState.update { it.copy(newTeamName = intent.name) }
            is TeamsIntent.UpdateNewTeamInviteCode -> _uiState.update { it.copy(newTeamInviteCode = intent.code) }
            is TeamsIntent.UpdateSearchQuery -> _uiState.update { it.copy(searchQuery = intent.query) }
            is TeamsIntent.PickPhoto -> uploadPhoto(intent.fileUrl)
            is TeamsIntent.SubmitAddTeam -> submitTeam()
            is TeamsIntent.DismissSuccessDialog -> _uiState.update { it.copy(isSuccessDialogVisible = false) }
        }
    }

    private fun loadTeams() {
        viewModelScope.launch {
            getTeamsUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Result.Success -> _uiState.update { it.copy(isLoading = false, teams = result.data) }
                    is Result.Failure -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _effect.emit(TeamsEffect.ShowError(result.exception.message ?: "Failed to load teams"))
                    }
                }
            }
        }
    }

    private fun uploadPhoto(fileUrl: String) {
        viewModelScope.launch {
            uploadTeamPhotoUseCase(fileUrl).collect { result ->
                when (result) {
                    is Result.Loading -> { /* Optionally show uploading state */ }
                    is Result.Success -> _uiState.update { it.copy(newTeamPhotoUrl = result.data) }
                    is Result.Failure -> _effect.emit(TeamsEffect.ShowError(result.exception.message ?: "Failed to upload photo"))
                }
            }
        }
    }

    private fun submitTeam() {
        val state = _uiState.value
        if (state.newTeamName.isBlank() || state.newTeamInviteCode.isBlank()) {
            viewModelScope.launch {
                _effect.emit(TeamsEffect.ShowError("Please fill all required fields"))
            }
            return
        }

        viewModelScope.launch {
            createTeamUseCase(state.newTeamName, state.newTeamPhotoUrl ?: "", state.newTeamInviteCode).collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update { it.copy(isSubmittingTeam = true) }
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isSubmittingTeam = false,
                                isAddTeamSheetVisible = false,
                                isSuccessDialogVisible = true,
                                newTeamName = "",
                                newTeamInviteCode = "",
                                newTeamPhotoUrl = null
                            )
                        }
                        loadTeams() // Refresh list
                    }
                    is Result.Failure -> {
                        _uiState.update { it.copy(isSubmittingTeam = false) }
                        _effect.emit(TeamsEffect.ShowError(result.exception.message ?: "Failed to create team"))
                    }
                }
            }
        }
    }
}
