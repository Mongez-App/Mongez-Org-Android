package com.iti.mongez.org.presentation.teams.details.contract

sealed interface TeamDetailsIntent {
    data class LoadTeam(val teamId: String) : TeamDetailsIntent
    data class TabSelected(val index: Int) : TeamDetailsIntent
    data class SearchQueryChanged(val query: String) : TeamDetailsIntent
    object ToggleAddCourseSheet : TeamDetailsIntent
    data class CreateCourse(
        val name: String,
        val courseCode: String,
        val imageUrl: String,
        val startDate: String,
        val examDate: String,
        val materials: List<android.net.Uri>,
        val courseType: String,
        val materialUrl: String?
    ) : TeamDetailsIntent
}

sealed interface TeamDetailsEffect {
    data class ShowSnackbar(val message: String, val type: com.iti.mongez.org.designsystem.components.snackbar.AppSnackbarType) : TeamDetailsEffect
}
