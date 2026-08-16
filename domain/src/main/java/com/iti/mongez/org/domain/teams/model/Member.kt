package com.iti.mongez.org.domain.teams.model

data class Member(
    val id: String,
    val name: String,
    val role: String,
    val imageUrl: String? = null
)
