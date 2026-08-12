package com.iti.mongez.org.domain.organization.model

/**
 * Represents an Organization in the system.
 */
data class Organization(
    val uid: String,
    val email: String,
    val name: String,
    val avatar: String? = null,
    val description: String? = null,
    val establishedAt: String? = null,
    val noOfStudents: Int = 0,
    val noOfCourses: Int = 0,
    val noOfTeams: Int = 0
)
