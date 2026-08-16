package com.iti.mongez.org.domain.teams.model

data class TeamEvent(
    val id: String,
    val title: String,
    val date: String,
    val location: String? = null,
    val description: String? = null
)
