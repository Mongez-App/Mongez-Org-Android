package com.iti.mongez.org.presentation.teams.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.org.domain.courses.model.Course
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
class TeamDetailsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(TeamDetailsUiState())
    val state: StateFlow<TeamDetailsUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<TeamDetailsEffect>()
    val effect: SharedFlow<TeamDetailsEffect> = _effect.asSharedFlow()

    fun handleIntent(intent: TeamDetailsIntent) {
        when (intent) {
            is TeamDetailsIntent.LoadTeam -> loadTeam(intent.teamId)
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
            kotlinx.coroutines.delay(1000)
            val newCourse = Course(
                id = java.util.UUID.randomUUID().toString(),
                name = intent.name,
                courseCode = intent.courseCode,
                imageUrl = if (intent.imageUrl.isNotEmpty()) intent.imageUrl else null,
                startDate = intent.startDate,
                examDate = intent.examDate,
                hasMaterials = intent.materials.isNotEmpty(),
                completionPercentage = 0f
            )
            _state.update {
                val updated = it.courses + newCourse
                it.copy(
                    isCreatingCourse = false,
                    isAddCourseSheetVisible = false,
                    courses = updated,
                    filteredCourses = updated
                )
            }
            _effect.emit(TeamDetailsEffect.ShowSnackbar("Course added successfully", AppSnackbarType.Success))
        }
    }

    private fun loadTeam(teamId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            // Mock Data
            val mockTeam = Team(
                id = teamId,
                name = "Mobile Native",
                description = "Focused on building high-quality Android applications.",
                imageUrl = "https://images.unsplash.com/photo-1517694712202-14dd9538aa97",
                membersCount = 15
            )

            val mockCourses = listOf(
                Course(
                    id = "1",
                    name = "Kotlin Advanced",
                    courseCode = "KOT-201",
                    imageUrl = null,
                    startDate = "2024-01-01",
                    examDate = "2024-03-01",
                    hasMaterials = true,
                    completionPercentage = 75f
                ),
                Course(
                    id = "2",
                    name = "Jetpack Compose Basics",
                    courseCode = "JPC-101",
                    imageUrl = null,
                    startDate = "2024-02-15",
                    examDate = "2024-04-15",
                    hasMaterials = false,
                    completionPercentage = 30f
                )
            )

            val mockEvents = listOf(
                TeamEvent(
                    id = "1",
                    title = "Monthly Tech Talk",
                    date = "2024-09-20",
                    location = "Room 302",
                    description = "Discussion about the new Android 15 features."
                ),
                TeamEvent(
                    id = "2",
                    title = "Team Outing",
                    date = "2024-10-05",
                    location = "Al Azhar Park",
                    description = "A day of fun and team building."
                )
            )

            val mockMembers = listOf(
                Member(id = "1", name = "Ahmed Ali", role = "Lead Android Developer"),
                Member(id = "2", name = "Sara Mohamed", role = "UI/UX Designer"),
                Member(id = "3", name = "John Doe", role = "Backend Engineer")
            )

            _state.update {
                it.copy(
                    isLoading = false,
                    team = mockTeam,
                    courses = emptyList(), // Empty for now to show the empty state
                    filteredCourses = emptyList(),
                    events = mockEvents,
                    members = mockMembers
                )
            }
        }
    }
}
