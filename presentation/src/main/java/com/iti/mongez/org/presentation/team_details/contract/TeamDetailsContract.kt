package com.iti.mongez.org.presentation.team_details.contract

sealed interface TeamDetailsIntent {
    data class LoadTeam(val teamId: String, val teamName: String) : TeamDetailsIntent
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

    object ToggleAddEventSheet : TeamDetailsIntent
    data class CreateEvent(
        val courseId: String,
        val type: String,
        val date: String
    ) : TeamDetailsIntent

    object DismissEventSuccessDialog : TeamDetailsIntent

    data class AcceptMember(val memberId: String) : TeamDetailsIntent
    data class DeclineMember(val memberId: String) : TeamDetailsIntent
}

sealed interface TeamDetailsEffect {
    data class ShowSnackbar(val message: String, val type: com.iti.mongez.org.designsystem.components.snackbar.AppSnackbarType) : TeamDetailsEffect
}
