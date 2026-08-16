package com.iti.mongez.org.presentation.teams.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.courses.model.Course
import com.iti.mongez.org.domain.courses.usecase.CreateCourseUseCase
import com.iti.mongez.org.domain.courses.usecase.GetCoursesUseCase
import com.iti.mongez.org.domain.teams.model.Member
import com.iti.mongez.org.domain.teams.model.Team
import com.iti.mongez.org.domain.teams.model.TeamEvent
import com.iti.mongez.org.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.org.presentation.teams.details.contract.TeamDetailsEffect
import com.iti.mongez.org.presentation.teams.details.contract.TeamDetailsIntent
import com.iti.mongez.org.presentation.teams.details.uiState.TeamDetailsUiState
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
    private val createCourseUseCase: CreateCourseUseCase
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

            // 1. Load Team Info (Still mock until Team use cases are available)
            val mockTeam = Team(
                id = teamId,
                name = "Java and Mobile",
                description = "Focused on building high-quality Android applications.",
                imageUrl = "https://images.unsplash.com/photo-1517694712202-14dd9538aa97",
                membersCount = 0
            )

            // 2. Load Real Courses
            when (val result = getCoursesUseCase(teamId)) {
                is Result.Success -> {
                    val courses = result.data
                    _state.update {
                        it.copy(
                            isLoading = false,
                            team = mockTeam,
                            courses = courses,
                            filteredCourses = courses
                        )
                    }
                }
                is Result.Failure -> {
                    _state.update { it.copy(isLoading = false, team = mockTeam) }
                    _effect.emit(TeamDetailsEffect.ShowSnackbar("Failed to load courses", AppSnackbarType.Error))
                }
                else -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }

            // Mock Data for other sections (Events/Members)
            val mockEvents = listOf(
                TeamEvent(
                    id = "1",
                    title = "Monthly Tech Talk",
                    date = "2024-09-20",
                    location = "Room 302",
                    description = "Discussion about the new Android 15 features."
                )
            )

            val mockMembers = listOf(
                Member(id = "1", name = "Ahmed Ali", role = "Lead Android Developer")
            )

            _state.update {
                it.copy(
                    events = mockEvents,
                    members = mockMembers
                )
            }
        }
    }
}
