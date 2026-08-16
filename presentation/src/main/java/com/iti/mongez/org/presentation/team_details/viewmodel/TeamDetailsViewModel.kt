package com.iti.mongez.org.presentation.team_details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.courses.usecase.CreateCourseUseCase
import com.iti.mongez.org.domain.courses.usecase.GetCoursesUseCase
import com.iti.mongez.org.domain.team_details.model.Team
import com.iti.mongez.org.domain.team_details.usecase.AcceptMemberRequestUseCase
import com.iti.mongez.org.domain.team_details.usecase.CreateTeamEventUseCase
import com.iti.mongez.org.domain.team_details.usecase.DeclineMemberRequestUseCase
import com.iti.mongez.org.domain.team_details.usecase.GetTeamEventsUseCase
import com.iti.mongez.org.domain.team_details.usecase.GetTeamMembersUseCase
import com.iti.mongez.org.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.org.presentation.team_details.contract.TeamDetailsEffect
import com.iti.mongez.org.presentation.team_details.contract.TeamDetailsIntent
import com.iti.mongez.org.presentation.team_details.uiState.TeamDetailsUiState
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
class TeamDetailsViewModel @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val createCourseUseCase: CreateCourseUseCase,
    private val getTeamEventsUseCase: GetTeamEventsUseCase,
    private val createTeamEventUseCase: CreateTeamEventUseCase,
    private val getTeamMembersUseCase: GetTeamMembersUseCase,
    private val acceptMemberRequestUseCase: AcceptMemberRequestUseCase,
    private val declineMemberRequestUseCase: DeclineMemberRequestUseCase
) : ViewModel() {

    private var currentTeamId: String = ""

    private val _state = MutableStateFlow(TeamDetailsUiState())
    val state: StateFlow<TeamDetailsUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<TeamDetailsEffect>()
    val effect: SharedFlow<TeamDetailsEffect> = _effect.asSharedFlow()

    fun handleIntent(intent: TeamDetailsIntent) {
        when (intent) {
            is TeamDetailsIntent.LoadTeam -> {
                currentTeamId = intent.teamId
                loadTeam(intent.teamId)
            }
            is TeamDetailsIntent.TabSelected -> _state.update { it.copy(selectedTabIndex = intent.index) }
            is TeamDetailsIntent.SearchQueryChanged -> filterCourses(intent.query)
            TeamDetailsIntent.ToggleAddCourseSheet -> {
                _state.update { it.copy(isAddCourseSheetVisible = !it.isAddCourseSheetVisible) }
            }
            is TeamDetailsIntent.CreateCourse -> createCourse(intent)
            TeamDetailsIntent.ToggleAddEventSheet -> {
                _state.update { it.copy(isAddEventSheetVisible = !it.isAddEventSheetVisible) }
            }
            is TeamDetailsIntent.CreateEvent -> createEvent(intent)
            TeamDetailsIntent.DismissEventSuccessDialog -> {
                _state.update { it.copy(isEventAddedSuccessfully = false) }
            }
            is TeamDetailsIntent.AcceptMember -> acceptMember(intent.memberId)
            is TeamDetailsIntent.DeclineMember -> declineMember(intent.memberId)
        }
    }

    private fun acceptMember(memberId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = acceptMemberRequestUseCase(memberId)) {
                is Result.Success -> {
                    _effect.emit(TeamDetailsEffect.ShowSnackbar("Member accepted successfully", AppSnackbarType.Success))
                    loadTeam(currentTeamId)
                }
                is Result.Failure -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(TeamDetailsEffect.ShowSnackbar(result.exception.message ?: "Failed to accept member", AppSnackbarType.Error))
                }
                else -> {}
            }
        }
    }

    private fun declineMember(memberId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = declineMemberRequestUseCase(memberId)) {
                is Result.Success -> {
                    _effect.emit(TeamDetailsEffect.ShowSnackbar("Request declined", AppSnackbarType.Success))
                    loadTeam(currentTeamId)
                }
                is Result.Failure -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(TeamDetailsEffect.ShowSnackbar(result.exception.message ?: "Failed to decline member", AppSnackbarType.Error))
                }
                else -> {}
            }
        }
    }

    private fun createEvent(intent: TeamDetailsIntent.CreateEvent) {
        viewModelScope.launch {
            _state.update { it.copy(isCreatingEvent = true) }
            
            val result = createTeamEventUseCase(
                teamId = currentTeamId,
                courseId = intent.courseId,
                eventType = intent.type,
                eventDate = intent.date
            )

            when (result) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isCreatingEvent = false,
                            isAddEventSheetVisible = false,
                            isEventAddedSuccessfully = true
                        )
                    }
                    loadTeam(currentTeamId)
                }
                is Result.Failure -> {
                    _state.update { it.copy(isCreatingEvent = false) }
                    _effect.emit(TeamDetailsEffect.ShowSnackbar(result.exception.message ?: "Failed to add event", AppSnackbarType.Error))
                }
                else -> {}
            }
        }
    }

    private fun filterCourses(query: String) {
        val filtered = if (query.isEmpty()) {
            _state.value.courses
        } else {
            _state.value.courses.filter {
                it.name.contains(query, ignoreCase = true) || it.courseCode.contains(query, ignoreCase = true)
            }
        }
        _state.update { it.copy(searchQuery = query, filteredCourses = filtered) }
    }

    private fun createCourse(intent: TeamDetailsIntent.CreateCourse) {
        viewModelScope.launch {
            _state.update { it.copy(isCreatingCourse = true) }
            
            val result = createCourseUseCase(
                teamId = currentTeamId,
                name = intent.name,
                startDate = intent.startDate,
                endDate = intent.examDate,
                thumbnailUrl = intent.imageUrl,
                materialIds = emptyList()
            )

            when (result) {
                is Result.Success -> {
                    _state.update { it.copy(isCreatingCourse = false, isAddCourseSheetVisible = false) }
                    _effect.emit(TeamDetailsEffect.ShowSnackbar("Course added successfully", AppSnackbarType.Success))
                    loadTeam(currentTeamId)
                }
                is Result.Failure -> {
                    _state.update { it.copy(isCreatingCourse = false) }
                    _effect.emit(TeamDetailsEffect.ShowSnackbar(result.exception.message ?: "Failed to add course", AppSnackbarType.Error))
                }
                else -> {}
            }
        }
    }

    private fun loadTeam(teamId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            // 1. Load Team Info (Mock until Team API is ready)
            val mockTeam = Team(
                id = teamId,
                name = "Java and Mobile",
                description = "Focused on building high-quality Android applications.",
                imageUrl = "https://images.unsplash.com/photo-1517694712202-14dd9538aa97",
                membersCount = 0
            )

            // 2. Load Real Courses
            val coursesResult = getCoursesUseCase(teamId)
            val courses = (coursesResult as? Result.Success)?.data ?: emptyList()

            // 3. Load Real Events
            val eventsResult = getTeamEventsUseCase(teamId)
            val events = (eventsResult as? Result.Success)?.data ?: emptyList()

            // 4. Load Real Members
            val membersResult = getTeamMembersUseCase(teamId)
            val teamMembersData = (membersResult as? Result.Success)?.data

            _state.update {
                it.copy(
                    isLoading = false,
                    team = mockTeam,
                    courses = courses,
                    filteredCourses = courses,
                    events = events,
                    members = teamMembersData?.teamMembers ?: emptyList(),
                    pendingMembers = teamMembersData?.pendingMembers ?: emptyList()
                )
            }

            if (coursesResult is Result.Failure) {
                _effect.emit(TeamDetailsEffect.ShowSnackbar("Failed to load courses", AppSnackbarType.Error))
            }
            if (eventsResult is Result.Failure) {
                _effect.emit(TeamDetailsEffect.ShowSnackbar("Failed to load events", AppSnackbarType.Error))
            }
            if (membersResult is Result.Failure) {
                _effect.emit(TeamDetailsEffect.ShowSnackbar("Failed to load members", AppSnackbarType.Error))
            }
        }
    }
}
