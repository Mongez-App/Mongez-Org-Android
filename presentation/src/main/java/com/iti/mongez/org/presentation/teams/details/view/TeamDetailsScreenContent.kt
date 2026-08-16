package com.iti.mongez.org.presentation.teams.details.view

import android.graphics.Paint
import android.util.Patterns
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Group
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.org.designsystem.components.button.AppButton
import com.iti.mongez.org.designsystem.components.common.AppEmptyState
import com.iti.mongez.org.designsystem.components.loading.AppShimmer
import com.iti.mongez.org.designsystem.components.dialog.AppConfirmationDialog
import com.iti.mongez.org.designsystem.components.search.AppSearchBar
import com.iti.mongez.org.designsystem.components.sheet.AppBottomSheet
import com.iti.mongez.org.designsystem.components.snackbar.AppSnackbarContent
import com.iti.mongez.org.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.org.designsystem.components.tabs.AppPrimaryTabs
import com.iti.mongez.org.designsystem.theme.MongezTheme
import com.iti.mongez.org.designsystem.theme.Theme
import com.iti.mongez.org.presentation.R
import com.iti.mongez.org.presentation.courses.components.AddCourseSheetContent
import com.iti.mongez.org.presentation.courses.components.CoursesList
import com.iti.mongez.org.presentation.teams.details.components.AddEventSheetContent
import com.iti.mongez.org.presentation.teams.details.components.TeamEventsList
import com.iti.mongez.org.presentation.teams.details.contract.TeamDetailsIntent
import com.iti.mongez.org.presentation.teams.details.uiState.TeamDetailsUiState
import com.iti.mongez.org.domain.teams.model.TeamEvent
import com.iti.mongez.org.domain.teams.model.Member
import com.iti.mongez.org.domain.teams.model.Team
import com.iti.mongez.org.domain.courses.model.Course

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
fun TeamDetailsScreenContent(
    state: TeamDetailsUiState,
    innerPadding: PaddingValues,
    onIntent: (TeamDetailsIntent) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToCourseDetails: (String) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.team?.name ?: "",
                        style = Theme.typography.headline.medium,
                        color = Theme.colorScheme.text.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Theme.colorScheme.text.primary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (state.selectedTabIndex == 0) {
                                onIntent(TeamDetailsIntent.ToggleAddCourseSheet)
                            } else if (state.selectedTabIndex == 1) {
                                onIntent(TeamDetailsIntent.ToggleAddEventSheet)
                            }
                        },
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
        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Theme.colorScheme.brand.primary)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    val tabs = listOf("Courses", "Events", "Members")
                    AppPrimaryTabs(
                        tabs = tabs,
                        selectedTabIndex = state.selectedTabIndex,
                        onTabSelected = { onIntent(TeamDetailsIntent.TabSelected(it)) }
                    )

                    Box(modifier = Modifier.fillMaxSize().padding(Theme.spacing.lg)) {
                        when (state.selectedTabIndex) {
                            0 -> {
                                Column {
                                    AppSearchBar(
                                        query = state.searchQuery,
                                        onQueryChange = { onIntent(TeamDetailsIntent.SearchQueryChanged(it)) },
                                        placeholder = stringResource(R.string.search_courses)
                                    )
                                    Spacer(modifier = Modifier.height(Theme.spacing.lg))
                                    if (state.filteredCourses.isEmpty()) {
                                        AppEmptyState(
                                            title = stringResource(R.string.no_courses_yet),
                                            description = stringResource(R.string.no_courses_for_team_desc),
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
                                            actionText = stringResource(R.string.add_first_course),
                                            onAction = { onIntent(TeamDetailsIntent.ToggleAddCourseSheet) }
                                        )
                                    } else {
                                        CoursesList(
                                            courses = state.filteredCourses,
                                            onCourseClick = onNavigateToCourseDetails
                                        )
                                    }
                                }
                            }
                            1 -> TeamEventsList(
                                events = state.events,
                                onAddEventClick = { onIntent(TeamDetailsIntent.ToggleAddEventSheet) }
                            )
                            2 -> MembersList(members = state.members)
                        }
                    }
                }
            }

            if (state.isAddCourseSheetVisible) {
                AppBottomSheet(
                    onDismiss = { onIntent(TeamDetailsIntent.ToggleAddCourseSheet) },
                    title = stringResource(R.string.add_new_course)
                ) {
                    AddCourseSheetContent(
                        isLoading = state.isCreatingCourse,
                        onAddCourse = { name, code, imageUrl, startDate, examDate, materials, isOnlineCourse, materialUrl ->
                            onIntent(
                                TeamDetailsIntent.CreateCourse(
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
                    )
                }
            }

            if (state.isAddEventSheetVisible) {
                AppBottomSheet(
                    onDismiss = { onIntent(TeamDetailsIntent.ToggleAddEventSheet) },
                    title = "Add Event"
                ) {
                    AddEventSheetContent(
                        isLoading = state.isCreatingEvent,
                        onAddEvent = { courseId, type, date ->
                            onIntent(TeamDetailsIntent.CreateEvent(courseId, type, date))
                        }
                    )
                }
            }

            if (state.isEventAddedSuccessfully) {
                AppConfirmationDialog(
                    title = "Confirmation",
                    description = "Your Event Has Been Added Successfully",
                    primaryActionText = "OK",
                    onPrimaryAction = { onIntent(TeamDetailsIntent.DismissEventSuccessDialog) },
                    onDismiss = { onIntent(TeamDetailsIntent.DismissEventSuccessDialog) }
                )
            }
        }
    }
}

@Composable
fun MembersList(members: List<Member>) {
    if (members.isEmpty()) {
        AppEmptyState(
            title = stringResource(R.string.no_members_yet),
            description = stringResource(R.string.no_members_desc),
            illustration = {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(Theme.colorScheme.brand.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Group,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = Theme.colorScheme.brand.primary
                    )
                }
            }
        )
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.xl),
            contentPadding = PaddingValues(top = Theme.spacing.xl, bottom = Theme.spacing.lg)
        ) {
            items(members) { member ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Theme.colorScheme.surface.surfaceVariant)
                ) {
                    Row(modifier = Modifier.padding(Theme.spacing.md)) {
                        Column {
                            Text(text = member.name, style = Theme.typography.title.medium, fontWeight = FontWeight.Bold)
                            Text(text = member.role, style = Theme.typography.body.medium)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeamDetailsScreenContentPreview() {
    val sampleTeam = Team(
        id = "1",
        name = "Mobile Native",
        description = "Core Mobile Development Team",
        membersCount = 15
    )

    val sampleCourses = listOf(
        Course(
            id = "1",
            name = "Kotlin Coroutines",
            courseCode = "KOT101",
            imageUrl = null,
            startDate = "2023-08-01",
            examDate = "2023-09-01",
            hasMaterials = true,
            completionPercentage = 60f
        ),
        Course(
            id = "2",
            name = "Jetpack Compose",
            courseCode = "CMP102",
            imageUrl = null,
            startDate = "2023-08-15",
            examDate = "2023-10-01",
            hasMaterials = true,
            completionPercentage = 30f
        )
    )

    val sampleEvents = listOf(
        TeamEvent(
            id = "1",
            title = "Tech Talk: KMP",
            date = "2023-08-25",
            location = "Meeting Room A"
        ),
        TeamEvent(
            id = "2",
            title = "Monthly Team Sync",
            date = "2023-09-01",
            location = "Main Hall"
        )
    )

    val sampleMembers = listOf(
        Member(id = "1", name = "Ahmed Ali", role = "Lead Android Developer"),
        Member(id = "2", name = "Sara Jones", role = "UI/UX Designer")
    )

    val state = TeamDetailsUiState(
        isLoading = false,
        team = sampleTeam,
        courses = sampleCourses,
        filteredCourses = sampleCourses,
        events = sampleEvents,
        members = sampleMembers,
        selectedTabIndex = 0
    )

    MongezTheme {
        Surface(color = Theme.colorScheme.surface.background) {
            TeamDetailsScreenContent(
                state = state,
                innerPadding = PaddingValues(0.dp),
                onIntent = {},
                onNavigateBack = {},
                onNavigateToCourseDetails = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeamDetailsScreenContentDarkPreview() {
    val sampleTeam = Team(
        id = "1",
        name = "Mobile Native",
        description = "Core Mobile Development Team",
        membersCount = 15
    )

    val sampleCourses = listOf(
        Course(
            id = "1",
            name = "Kotlin Coroutines",
            courseCode = "KOT101",
            imageUrl = null,
            startDate = "2023-08-01",
            examDate = "2023-09-01",
            hasMaterials = true,
            completionPercentage = 60f
        ),
        Course(
            id = "2",
            name = "Jetpack Compose",
            courseCode = "CMP102",
            imageUrl = null,
            startDate = "2023-08-15",
            examDate = "2023-10-01",
            hasMaterials = true,
            completionPercentage = 30f
        )
    )

    val sampleEvents = listOf(
        TeamEvent(
            id = "1",
            title = "Tech Talk: KMP",
            date = "2023-08-25",
            location = "Meeting Room A"
        ),
        TeamEvent(
            id = "2",
            title = "Monthly Team Sync",
            date = "2023-09-01",
            location = "Main Hall"
        )
    )

    val sampleMembers = listOf(
        Member(id = "1", name = "Ahmed Ali", role = "Lead Android Developer"),
        Member(id = "2", name = "Sara Jones", role = "UI/UX Designer")
    )

    val state = TeamDetailsUiState(
        isLoading = false,
        team = sampleTeam,
        courses = sampleCourses,
        filteredCourses = sampleCourses,
        events = sampleEvents,
        members = sampleMembers,
        selectedTabIndex = 0
    )

    MongezTheme(darkTheme = true) {
        Surface(color = Theme.colorScheme.surface.background) {
            TeamDetailsScreenContent(
                state = state,
                innerPadding = PaddingValues(0.dp),
                onIntent = {},
                onNavigateBack = {},
                onNavigateToCourseDetails = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Team Details - Empty Courses")
@Composable
fun TeamDetailsEmptyCoursesPreview() {
    val state = TeamDetailsUiState(
        isLoading = false,
        team = Team(id = "1", name = "Mobile Native"),
        courses = emptyList(),
        selectedTabIndex = 0
    )

    MongezTheme {
        Surface(color = Theme.colorScheme.surface.background) {
            TeamDetailsScreenContent(
                state = state,
                innerPadding = PaddingValues(0.dp),
                onIntent = {},
                onNavigateBack = {},
                onNavigateToCourseDetails = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Team Details - Empty Events")
@Composable
fun TeamDetailsEmptyEventsPreview() {
    val state = TeamDetailsUiState(
        isLoading = false,
        team = Team(id = "1", name = "Mobile Native"),
        events = emptyList(),
        selectedTabIndex = 1
    )

    MongezTheme {
        Surface(color = Theme.colorScheme.surface.background) {
            TeamDetailsScreenContent(
                state = state,
                innerPadding = PaddingValues(0.dp),
                onIntent = {},
                onNavigateBack = {},
                onNavigateToCourseDetails = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Team Details - Empty Members")
@Composable
fun TeamDetailsEmptyMembersPreview() {
    val state = TeamDetailsUiState(
        isLoading = false,
        team = Team(id = "1", name = "Mobile Native"),
        members = emptyList(),
        selectedTabIndex = 2
    )

    MongezTheme {
        Surface(color = Theme.colorScheme.surface.background) {
            TeamDetailsScreenContent(
                state = state,
                innerPadding = PaddingValues(0.dp),
                onIntent = {},
                onNavigateBack = {},
                onNavigateToCourseDetails = {}
            )
        }
    }
}
