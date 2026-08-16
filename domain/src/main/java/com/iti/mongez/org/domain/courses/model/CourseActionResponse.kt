package com.iti.mongez.org.domain.courses.model

import com.iti.mongez.org.domain.core.Alert

data class CourseActionResponse<T>(
    val data: T,
    val alert: Alert? = null
)
