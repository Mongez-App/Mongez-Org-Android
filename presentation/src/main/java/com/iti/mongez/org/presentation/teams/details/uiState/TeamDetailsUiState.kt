package com.iti.mongez.org.presentation.teams.details.uiState

import com.iti.mongez.org.domain.courses.model.Course
import com.iti.mongez.org.domain.teams.model.Member
import com.iti.mongez.org.domain.teams.model.Team
import com.iti.mongez.org.domain.teams.model.TeamEvent

data class TeamDetailsUiState(
    val isLoading: Boolean = false,
    val team: Team? = null,
    val courses: List<Course> = emptyList(),
    val events: List<TeamEvent> = emptyList(),
    val members: List<Member> = emptyList(),
    val selectedTabIndex: Int = 0,
    val searchQuery: String = "",
    val filteredCourses: List<Course> = emptyList(),
    val isAddCourseSheetVisible: Boolean = false,
    val isCreatingCourse: Boolean = false,
    val isAddEventSheetVisible: Boolean = false,
    val isCreatingEvent: Boolean = false,
    val isEventAddedSuccessfully: Boolean = false
)
