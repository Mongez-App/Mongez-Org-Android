package com.iti.mongez.org.domain.teams.model

data class TeamMembers(
    val teamId: String,
    val pendingMembers: List<Member>,
    val teamMembers: List<Member>,
    val pendingTotal: Int,
    val teamTotal: Int
)
