package com.iti.mongez.org.presentation.courses.view

import android.graphics.Paint
import android.util.Patterns
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.org.designsystem.components.button.AppButton
import com.iti.mongez.org.designsystem.components.common.AppEmptyState
import com.iti.mongez.org.designsystem.components.dialog.AppConfirmationDialog
import com.iti.mongez.org.designsystem.components.loading.AppShimmer
import com.iti.mongez.org.designsystem.components.search.AppSearchBar
import com.iti.mongez.org.designsystem.components.sheet.AppBottomSheet
import com.iti.mongez.org.designsystem.components.snackbar.AppSnackbarContent
import com.iti.mongez.org.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.org.designsystem.screens.courses.CourseCard
import com.iti.mongez.org.designsystem.theme.MongezTheme
import com.iti.mongez.org.designsystem.theme.Theme
import com.iti.mongez.org.domain.courses.model.Course
import com.iti.mongez.org.presentation.R
import com.iti.mongez.org.presentation.courses.components.AddCourseSheetContent
import com.iti.mongez.org.presentation.courses.contract.CoursesIntent
import com.iti.mongez.org.presentation.courses.components.CoursesList
import com.iti.mongez.org.presentation.courses.components.CoursesShimmerLoading
import com.iti.mongez.org.presentation.courses.uiState.CoursesState

private fun Modifier.coursesActionShadow(
    shadowColor: Color,
    backgroundColor: Color
): Modifier = this.drawBehind {
    val shadowColorArgb = shadowColor.copy(alpha = 0.7f).toArgb()

    drawIntoCanvas { canvas ->
        val paint = Paint()
        paint.color = backgroundColor.toArgb()

        val blurRadius = 5.dp.toPx()

        paint.setShadowLayer(
            blurRadius,
            0f,
            0f,
            shadowColorArgb
        )

        canvas.nativeCanvas.drawCircle(
            size.width / 2,
            size.height / 2,
            size.width / 2,
            paint
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreenContent(
    state: CoursesState,
    innerPadding: PaddingValues,
    topSnackbarMessage: String?,
    topSnackbarType: AppSnackbarType,
    onIntent: (CoursesIntent) -> Unit,
    onCourseClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = state.teamName,
                            style = Theme.typography.headline.medium,
                            color = Theme.colorScheme.text.primary
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = { onIntent(CoursesIntent.ToggleAddCourseSheet) },
                            modifier = Modifier
                                .padding(end = Theme.spacing.lg)
                                .size(Theme.spacing.xxxl)
                                .coursesActionShadow(
                                    shadowColor = Theme.colorScheme.brand.primary,
                                    backgroundColor = Theme.colorScheme.surface.background
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Theme.colorScheme.brand.primary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Theme.colorScheme.surface.background
                    )
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(Theme.spacing.lg)
            ) {

                AppSearchBar(
                    query = state.searchQuery,
                    onQueryChange = { onIntent(CoursesIntent.SearchQueryChanged(it)) },
//                    onFilterClick = { onIntent(CoursesIntent.FilterClicked) }
                )

                Spacer(modifier = Modifier.height(Theme.spacing.lg))

                if (state.isLoading && state.allCourses.isEmpty()) {
                    CoursesShimmerLoading()
                } else if (!state.isLoading && state.filteredCourses.isEmpty()) {
                    AppEmptyState(
                        title = if (state.searchQuery.isNotEmpty()) {
                            stringResource(R.string.no_courses_match_search)
                        } else {
                            stringResource(R.string.no_courses_yet)
                        },
                        description = if (state.searchQuery.isNotEmpty()) {
                            "Try searching with different keywords."
                        } else {
                            stringResource(R.string.no_courses_for_team_desc)
                        },
                        illustration = {
                            Box(
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(CircleShape)
                                    .background(Theme.colorScheme.brand.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_empty_course),
                                    contentDescription = null,
                                    modifier = Modifier.size(110.dp)
                                )
                            }
                        },
                        actionText = if (state.searchQuery.isEmpty()) stringResource(R.string.add_first_course) else null,
                        onAction = { onIntent(CoursesIntent.ToggleAddCourseSheet) }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(Theme.spacing.xl),
                        contentPadding = PaddingValues(
                            top = Theme.spacing.xl,
                            bottom = Theme.spacing.giant + Theme.spacing.lg
                        )
                    ) {
                        items(state.filteredCourses, key = { it.id }) { course ->
                            Box(modifier = Modifier.fillMaxWidth().animateItem()) {
                                CourseCard(
                                    title = course.name,
                                    progress = course.completionPercentage / 100f,
                                    onClick = { onCourseClick(course.id) },
                                    imageUrl = course.imageUrl
                                )
                            }
                        }
                    }
                }
            }

            state.courseToDeleteId?.let { courseId ->
                AppConfirmationDialog(
                    title = stringResource(R.string.delete_course_title),
                    description = stringResource(R.string.delete_course_dialog_description),
                    primaryActionText = stringResource(R.string.action_delete),
                    onPrimaryAction = {
                        onIntent(CoursesIntent.ConfirmDeleteCourse(courseId))
                    },
                    onDismiss = {
                        onIntent(CoursesIntent.DismissDeleteConfirmation)
                    },
                    secondaryActionText = stringResource(R.string.action_cancel),
                    onSecondaryAction = {
                        onIntent(CoursesIntent.DismissDeleteConfirmation)
                    }
                )
            }

            if (state.isAddCourseSheetVisible) {
                AppBottomSheet(
                    onDismiss = { onIntent(CoursesIntent.ToggleAddCourseSheet) },
                    title = stringResource(R.string.add_new_course)
                ) {
                    val emptyFieldsError = stringResource(R.string.error_empty_fields)

                    AddCourseSheetContent(
                        isLoading = state.isCreatingCourse,
                        onAddCourse = { name, code, imageUrl, startDate, examDate, materials, isOnlineCourse, materialUrl ->

                            val isUrlValid = materialUrl?.let { Patterns.WEB_URL.matcher(it).matches() } ?: true

                            if (name.isBlank() || code.isBlank() || startDate.isBlank() || examDate.isBlank() || (isOnlineCourse && materialUrl.isNullOrBlank())) {
                                onIntent(CoursesIntent.ShowSnackbar(message = emptyFieldsError, type = AppSnackbarType.Error))
                            } else if (isOnlineCourse && !isUrlValid) {
                                onIntent(CoursesIntent.ShowSnackbar(message = "Please enter a valid Course URL", type = AppSnackbarType.Error))
                            } else {
                                onIntent(
                                    CoursesIntent.CreateCourse(
                                        name = name,
                                        courseCode = code,
                                        imageUrl = imageUrl,
                                        startDate = startDate,
                                        examDate = examDate,
                                        materials = materials,
                                        courseType = if (isOnlineCourse) "URL_COURSE" else "MATERIAL_COURSE",
                                        materialUrl = if (isOnlineCourse) materialUrl else null
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = topSnackbarMessage != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(Theme.spacing.md)
                .padding(top = Theme.spacing.xxl)
        ) {
            topSnackbarMessage?.let { message ->
                AppSnackbarContent(message = message, type = topSnackbarType)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoursesScreenContentPreview() {
    val sampleCourses = listOf(
        Course(
            id = "1",
            name = "Mobile Development",
            courseCode = "CS101",
            imageUrl = null, // Showing placeholder in preview
            startDate = "2023-01-01",
            examDate = "2023-06-01",
            hasMaterials = true,
            completionPercentage = 75f
        ),
        Course(
            id = "2",
            name = "Algorithms & Data Structures",
            courseCode = "CS102",
            imageUrl = null,
            startDate = "2023-01-01",
            examDate = "2023-06-01",
            hasMaterials = false,
            completionPercentage = 40f
        )
    )

    val sampleState = CoursesState(
        allCourses = sampleCourses,
        filteredCourses = sampleCourses,
        isLoading = false,
        teamName = "Mobile Native"
    )

    MongezTheme {
        Surface(color = Theme.colorScheme.surface.background) {
            CoursesScreenContent(
                state = sampleState,
                innerPadding = PaddingValues(0.dp),
                topSnackbarMessage = null,
                topSnackbarType = AppSnackbarType.Success,
                onIntent = {},
                onCourseClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoursesScreenContentDarkPreview() {
    val sampleCourses = listOf(
        Course(
            id = "1",
            name = "Mobile Development",
            courseCode = "CS101",
            imageUrl = null, // Showing placeholder in preview
            startDate = "2023-01-01",
            examDate = "2023-06-01",
            hasMaterials = true,
            completionPercentage = 75f
        ),
        Course(
            id = "2",
            name = "Algorithms & Data Structures",
            courseCode = "CS102",
            imageUrl = null,
            startDate = "2023-01-01",
            examDate = "2023-06-01",
            hasMaterials = false,
            completionPercentage = 40f
        )
    )

    val sampleState = CoursesState(
        allCourses = sampleCourses,
        filteredCourses = sampleCourses,
        isLoading = false,
        teamName = "Mobile Native"
    )

    MongezTheme(darkTheme = true) {
        Surface(color = Theme.colorScheme.surface.background) {
            CoursesScreenContent(
                state = sampleState,
                innerPadding = PaddingValues(0.dp),
                topSnackbarMessage = null,
                topSnackbarType = AppSnackbarType.Success,
                onIntent = {},
                onCourseClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Courses Screen - Empty State")
@Composable
fun CoursesScreenEmptyPreview() {
    val sampleState = CoursesState(
        allCourses = emptyList(),
        filteredCourses = emptyList(),
        isLoading = false,
        teamName = "Mobile Native"
    )

    MongezTheme {
        Surface(color = Theme.colorScheme.surface.background) {
            CoursesScreenContent(
                state = sampleState,
                innerPadding = PaddingValues(0.dp),
                topSnackbarMessage = null,
                topSnackbarType = AppSnackbarType.Success,
                onIntent = {},
                onCourseClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Courses Shimmer Loading - Light")
@Composable
fun CoursesShimmerLoadingPreview() {
    MongezTheme {
        Surface(color = Theme.colorScheme.surface.background, modifier = Modifier.padding(Theme.spacing.lg)) {
            CoursesShimmerLoading()
        }
    }
}

@Preview(showBackground = true, name = "Courses Shimmer Loading - Dark")
@Composable
fun CoursesShimmerLoadingDarkPreview() {
    MongezTheme(darkTheme = true) {
        Surface(color = Theme.colorScheme.surface.background, modifier = Modifier.padding(Theme.spacing.lg)) {
            CoursesShimmerLoading()
        }
    }
}