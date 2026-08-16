package com.iti.mongez.org.presentation.teams

import com.iti.mongez.org.domain.team.model.Team

sealed class TeamsIntent {
    object LoadTeams : TeamsIntent()
    object OpenAddTeamSheet : TeamsIntent()
    object CloseAddTeamSheet : TeamsIntent()
    data class UpdateNewTeamName(val name: String) : TeamsIntent()
    data class UpdateNewTeamInviteCode(val code: String) : TeamsIntent()
    data class UpdateSearchQuery(val query: String) : TeamsIntent()
    data class PickPhoto(val fileUrl: String) : TeamsIntent() // Mocking photo pick by passing a url
    object SubmitAddTeam : TeamsIntent()
    object DismissSuccessDialog : TeamsIntent()
}

data class TeamsState(
    val isLoading: Boolean = false,
    val teams: List<Team> = emptyList(),
    val searchQuery: String = "",
    val isAddTeamSheetVisible: Boolean = false,
    val newTeamName: String = "",
    val newTeamInviteCode: String = "",
    val newTeamPhotoUrl: String? = null,
    val isSubmittingTeam: Boolean = false,
    val isSuccessDialogVisible: Boolean = false
)

sealed class TeamsEffect {
    data class ShowError(val message: String) : TeamsEffect()
}
