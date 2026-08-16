package com.iti.mongez.org.presentation.courses.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.org.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.org.domain.courses.model.Course
import com.iti.mongez.org.presentation.courses.contract.CoursesEffect
import com.iti.mongez.org.presentation.courses.contract.CoursesIntent
import com.iti.mongez.org.presentation.courses.uiState.CoursesState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(CoursesState())
    val state: StateFlow<CoursesState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CoursesEffect>()
    val effect: SharedFlow<CoursesEffect> = _effect.asSharedFlow()

    init {
        handleIntent(CoursesIntent.LoadCourses)
    }

    fun handleIntent(intent: CoursesIntent) {
        when(intent) {
            is CoursesIntent.LoadCourses -> loadCourses()
            is CoursesIntent.SearchQueryChanged -> filterCourses(intent.query)
            is CoursesIntent.CreateCourse -> createCourse(intent)
            is CoursesIntent.ToggleAddCourseSheet -> {
                _state.update { it.copy(isAddCourseSheetVisible = !it.isAddCourseSheetVisible) }
            }
            is CoursesIntent.ShowDeleteConfirmation -> {
                _state.update { it.copy(courseToDeleteId = intent.courseId) }
            }
            CoursesIntent.DismissDeleteConfirmation -> {
                _state.update { it.copy(courseToDeleteId = null) }
            }
            is CoursesIntent.ConfirmDeleteCourse -> {
                deleteCourse(intent.courseId)
            }
            is CoursesIntent.TabSelected -> {
                _state.update { it.copy(selectedTabIndex = intent.index) }
            }
            CoursesIntent.FilterClicked -> { /* Filter logic */ }
            is CoursesIntent.ShowSnackbar -> {
                viewModelScope.launch {
                    _effect.emit(CoursesEffect.ShowSnackbar(intent.message, intent.type))
                }
            }
        }
    }

    private fun loadCourses() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            // Mock Data
            val mockCourses = listOf(
                Course(
                    id = "1",
                    name = "Advanced Kotlin Coroutines",
                    courseCode = "KOT-101",
                    imageUrl = "https://images.unsplash.com/photo-1517694712202-14dd9538aa97",
                    startDate = "2023-09-01",
                    examDate = "2023-12-15",
                    hasMaterials = true,
                    completionPercentage = 85f
                ),
                Course(
                    id = "2",
                    name = "Jetpack Compose Internals",
                    courseCode = "CMP-202",
                    imageUrl = "https://images.unsplash.com/photo-1555066931-4365d14bab8c",
                    startDate = "2023-10-10",
                    examDate = "2024-01-20",
                    hasMaterials = true,
                    completionPercentage = 45f
                ),
                Course(
                    id = "3",
                    name = "Android System Architecture",
                    courseCode = "SYS-303",
                    imageUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475",
                    startDate = "2023-11-05",
                    examDate = "2024-03-10",
                    hasMaterials = false,
                    completionPercentage = 10f
                )
            )

            _state.update {
                it.copy(
                    isLoading = false,
                    allCourses = mockCourses,
                    filteredCourses = mockCourses,
                    teamName = "Mobile Native"
                )
            }
        }
    }

    private fun deleteCourse(courseId: String) {
        viewModelScope.launch {
            // Dismiss dialog immediately and mark deleting state
            _state.update { it.copy(isDeletingCourse = true, courseToDeleteId = null) }

            // 1. INSTANT OPTIMISTIC UPDATE: Remove course immediately from local lists
            val updatedAll = _state.value.allCourses.filter { it.id != courseId }
            val updatedFiltered = _state.value.filteredCourses.filter { it.id != courseId }
            
            _state.update {
                it.copy(
                    allCourses = updatedAll,
                    filteredCourses = updatedFiltered,
                    isDeletingCourse = false
                )
            }
            _effect.emit(CoursesEffect.ShowSnackbar("Course deleted successfully (Mock)", AppSnackbarType.Success))
        }
    }

    private fun createCourse(intent: CoursesIntent.CreateCourse) {
        viewModelScope.launch {
            _state.update { it.copy(isCreatingCourse = true) }

            // Simulate network delay
            kotlinx.coroutines.delay(1500)

            val newCourse = Course(
                id = java.util.UUID.randomUUID().toString(),
                name = intent.name,
                courseCode = intent.courseCode,
                imageUrl = if (intent.imageUrl.isNotEmpty()) intent.imageUrl else null,
                startDate = intent.startDate,
                examDate = intent.examDate,
                hasMaterials = intent.materials.isNotEmpty(),
                completionPercentage = 0f
            )

            _state.update { 
                val updatedList = it.allCourses + newCourse
                it.copy(
                    isCreatingCourse = false,
                    isAddCourseSheetVisible = false,
                    allCourses = updatedList,
                    filteredCourses = updatedList
                )
            }
            
            _effect.emit(CoursesEffect.ShowSnackbar("Course created successfully (Mock)", AppSnackbarType.Success))
        }
    }

    private fun filterCourses(query: String) {
        val filtered = if (query.isEmpty()) {
            _state.value.allCourses
        } else {
            _state.value.allCourses.filter {
                it.name.contains(query, ignoreCase = true) || it.courseCode.contains(query, ignoreCase = true)
            }
        }
        _state.update { it.copy(searchQuery = query, filteredCourses = filtered) }
    }
}
