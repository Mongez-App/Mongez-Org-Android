package com.iti.mongez.org.presentation.courses.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.org.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.courses.usecase.CreateCourseUseCase
import com.iti.mongez.org.domain.courses.usecase.DeleteCourseUseCase
import com.iti.mongez.org.domain.courses.usecase.GetCoursesUseCase
import com.iti.mongez.org.presentation.courses.contract.CoursesEffect
import com.iti.mongez.org.presentation.courses.contract.CoursesIntent
import com.iti.mongez.org.presentation.courses.uiState.CoursesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val createCourseUseCase: CreateCourseUseCase,
    private val deleteCourseUseCase: DeleteCourseUseCase
) : ViewModel() {

    private val teamId = "a9bff20a-3bef-448d-a9b6-78b54e7def34"

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
            
            when (val result = getCoursesUseCase(teamId)) {
                is Result.Success -> {
                    val courses = result.data
                    _state.update {
                        it.copy(
                            isLoading = false,
                            allCourses = courses,
                            filteredCourses = courses,
                            teamName = "Java and Mobile"
                        )
                    }
                }
                is Result.Failure -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(CoursesEffect.ShowSnackbar(result.exception.message ?: "Failed to load courses", AppSnackbarType.Error))
                }
                else -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun deleteCourse(courseId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isDeletingCourse = true, courseToDeleteId = null) }

            when (val result = deleteCourseUseCase(courseId)) {
                is Result.Success -> {
                    _state.update { state ->
                        val updatedAll = state.allCourses.filter { it.id != courseId }
                        val updatedFiltered = state.filteredCourses.filter { it.id != courseId }
                        state.copy(
                            allCourses = updatedAll,
                            filteredCourses = updatedFiltered,
                            isDeletingCourse = false
                        )
                    }
                    _effect.emit(CoursesEffect.ShowSnackbar("Course deleted successfully", AppSnackbarType.Success))
                }
                is Result.Failure -> {
                    _state.update { it.copy(isDeletingCourse = false) }
                    _effect.emit(CoursesEffect.ShowSnackbar(result.exception.message ?: "Failed to delete course", AppSnackbarType.Error))
                }
                else -> {
                    _state.update { it.copy(isDeletingCourse = false) }
                }
            }
        }
    }

    private fun createCourse(intent: CoursesIntent.CreateCourse) {
        viewModelScope.launch {
            _state.update { it.copy(isCreatingCourse = true) }

            val result = createCourseUseCase(
                teamId = teamId,
                name = intent.name,
                startDate = intent.startDate,
                endDate = intent.examDate,
                thumbnailUrl = intent.imageUrl,
                materialIds = emptyList()
            )

            when (result) {
                is Result.Success -> {
                    _state.update { it.copy(isCreatingCourse = false, isAddCourseSheetVisible = false) }
                    _effect.emit(CoursesEffect.ShowSnackbar("Course created successfully", AppSnackbarType.Success))
                    loadCourses()
                }
                is Result.Failure -> {
                    _state.update { it.copy(isCreatingCourse = false) }
                    _effect.emit(CoursesEffect.ShowSnackbar(result.exception.message ?: "Failed to create course", AppSnackbarType.Error))
                }
                else -> {
                    _state.update { it.copy(isCreatingCourse = false) }
                }
            }
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
