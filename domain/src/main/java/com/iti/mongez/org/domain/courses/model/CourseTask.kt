package com.iti.mongez.org.domain.courses.model

data class CourseTask(
    val id: String,
    val title: String,
    val durationMinutes: Int,
    val priority: String,
    val isCompleted: Boolean,
    val scheduledDate: String
)
