package com.iti.mongez.org.presentation.teams

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.iti.mongez.org.designsystem.R
import com.iti.mongez.org.designsystem.components.button.AppButton
import com.iti.mongez.org.designsystem.components.card.AppCard
import com.iti.mongez.org.designsystem.components.dialog.AppConfirmationDialog
import com.iti.mongez.org.designsystem.components.sheet.AppBottomSheet
import com.iti.mongez.org.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.org.designsystem.components.snackbar.TopSnackbar
import com.iti.mongez.org.designsystem.components.textfield.AppTextField
import com.iti.mongez.org.designsystem.theme.MongezTheme
import com.iti.mongez.org.designsystem.theme.Theme
import com.iti.mongez.org.domain.team.model.Team
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TeamsScreen(
    viewModel: TeamsViewModel = hiltViewModel(),
    onNavigateToCourseDetails: (String) -> Unit = {},
    onNavigateToTeamDetails: (String, String) -> Unit = { _, _ -> }
) {
    val state by viewModel.uiState.collectAsState()

    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    var snackbarVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TeamsEffect.ShowError -> {
                    snackbarMessage = effect.message
                    snackbarVisible = true
                    delay(3000.milliseconds)
                    snackbarVisible = false
                }
            }
        }
    }

    TeamsScreenContent(
        state = state,
        snackbarVisible = snackbarVisible,
        snackbarMessage = snackbarMessage,
        onIntent = viewModel::onIntent,
        onNavigateToCourseDetails = onNavigateToCourseDetails,
        onNavigateToTeamDetails = onNavigateToTeamDetails
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TeamsScreenContent(
    state: TeamsState,
    snackbarVisible: Boolean,
    snackbarMessage: String?,
    onIntent: (TeamsIntent) -> Unit,
    onNavigateToCourseDetails: (String) -> Unit,
    onNavigateToTeamDetails: (String, String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .systemBarsPadding()
                        .padding(horizontal = Theme.spacing.xl, vertical = Theme.spacing.lg),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Teams",
                        style = Theme.typography.headline.large.copy(fontWeight = FontWeight.Bold),
                        color = Theme.colorScheme.text.primary
                    )
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        border = BorderStroke(1.dp, Theme.colorScheme.brand.primary.copy(alpha = 0.2f)),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { onIntent(TeamsIntent.OpenAddTeamSheet) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Team",
                                tint = Theme.colorScheme.brand.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            },
            containerColor = Theme.colorScheme.surface.background
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(horizontal = Theme.spacing.xl)) {
                    // Search Bar
                    AppTextField(
                        value = state.searchQuery,
                        onValueChange = { onIntent(TeamsIntent.UpdateSearchQuery(it)) },
                        placeholder = "Search teams...",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Theme.spacing.md),
                        leadingIcon = Icons.Default.Search
                    )

                    val filteredTeams = if (state.searchQuery.isBlank()) {
                        state.teams
                    } else {
                        state.teams.filter { it.name.contains(state.searchQuery, ignoreCase = true) }
                    }

                    if (state.isLoading) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Theme.colorScheme.brand.primary)
                        }
                    } else if (state.teams.isEmpty()) {
                        EmptyTeamsState { onIntent(TeamsIntent.OpenAddTeamSheet) }
                    } else if (filteredTeams.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No teams match your search", color = Theme.colorScheme.text.secondary)
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = Theme.spacing.xxl),
                            verticalArrangement = Arrangement.spacedBy(Theme.spacing.md)
                        ) {
                            items(filteredTeams, key = { it.id }) { team ->
                                TeamCard(
                                    team = team,
                                    modifier = Modifier.animateItem(),
                                    onClick = { onNavigateToTeamDetails(team.id, team.name) }
                                )
                            }
                        }
                    }
                }

                if (state.isAddTeamSheetVisible) {
                    AddTeamSheet(
                        state = state,
                        onNameChange = { onIntent(TeamsIntent.UpdateNewTeamName(it)) },
                        onInviteCodeChange = { onIntent(TeamsIntent.UpdateNewTeamInviteCode(it)) },
                        onAddPhotoClick = { uri -> 
                            onIntent(TeamsIntent.PickPhoto(uri))
                        },
                        onSubmit = { onIntent(TeamsIntent.SubmitAddTeam) },
                        onDismiss = { onIntent(TeamsIntent.CloseAddTeamSheet) }
                    )
                }

                if (state.isSuccessDialogVisible) {
                    SuccessDialog { onIntent(TeamsIntent.DismissSuccessDialog) }
                }
            }
        }

        TopSnackbar(
            visible = snackbarVisible,
            message = snackbarMessage ?: "",
            type = AppSnackbarType.Error,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun EmptyTeamsState(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_teams),
            contentDescription = "No Teams",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(Theme.spacing.lg))
        Text(
            text = "No Teams Yet",
            style = Theme.typography.title.large.copy(fontWeight = FontWeight.Bold),
            color = Theme.colorScheme.text.primary
        )
        Spacer(modifier = Modifier.height(Theme.spacing.sm))
        Text(
            text = "You haven't added any teams yet",
            style = Theme.typography.body.medium,
            color = Theme.colorScheme.text.secondary
        )
        Spacer(modifier = Modifier.height(Theme.spacing.xl))
        AppButton(
            text = "Add Your First Team",
            onClick = onAddClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun TeamCard(team: Team, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    AppCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(Theme.spacing.lg)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Initials Circle
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Theme.colorScheme.brand.primary.copy(alpha = 0.8f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = team.name.take(2).uppercase(),
                        color = Theme.colorScheme.surface.surface,
                        style = Theme.typography.title.medium
                    )
                }
                Spacer(modifier = Modifier.width(Theme.spacing.md))
                Text(
                    text = team.name,
                    style = Theme.typography.title.medium.copy(fontWeight = FontWeight.Bold),
                    color = Theme.colorScheme.text.primary
                )
            }
            
            Spacer(modifier = Modifier.height(Theme.spacing.lg))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Team Progress",
                    style = Theme.typography.label.small,
                    color = Theme.colorScheme.text.tertiary
                )
                Text(
                    text = "${team.progress}%",
                    style = Theme.typography.label.small.copy(fontWeight = FontWeight.Bold),
                    color = Theme.colorScheme.brand.primary
                )
            }
            Spacer(modifier = Modifier.height(Theme.spacing.xs))
            LinearProgressIndicator(
                progress = { team.progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(Theme.radius.full)),
                color = Theme.colorScheme.brand.primary,
                trackColor = Theme.colorScheme.border.primary
            )

            Spacer(modifier = Modifier.height(Theme.spacing.md))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(Theme.colorScheme.brand.primary.copy(alpha = 0.1f), RoundedCornerShape(Theme.radius.sm))
                        .padding(horizontal = Theme.spacing.sm, vertical = 4.dp)
                ) {
                    val eventsText = if (team.events.isEmpty()) {
                        "No events in this team"
                    } else {
                        "${team.events.size} Events this week"
                    }
                    Text(
                        text = eventsText,
                        style = Theme.typography.label.small,
                        color = Theme.colorScheme.brand.primary
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Theme.colorScheme.brand.primary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTeamSheet(
    state: TeamsState,
    onNameChange: (String) -> Unit,
    onInviteCodeChange: (String) -> Unit,
    onAddPhotoClick: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                onAddPhotoClick(uri.toString())
            }
        }
    )

    AppBottomSheet(
        onDismiss = onDismiss
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clickable { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .border(1.dp, Theme.colorScheme.brand.primary, CircleShape)
                        .background(Color.Transparent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Theme.colorScheme.surface.surfaceVariant, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (state.newTeamPhotoUrl != null) {
                            AsyncImage(
                                model = state.newTeamPhotoUrl,
                                contentDescription = "Team Photo",
                                modifier = Modifier.fillMaxSize().clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.ic_image),
                                contentDescription = "Add Photo",
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-4).dp, y = (-4).dp)
                        .size(32.dp)
                        .background(Theme.colorScheme.brand.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(Theme.spacing.sm))
            Text(
                text = "Add Photo",
                style = Theme.typography.label.medium.copy(fontWeight = FontWeight.Bold),
                color = Theme.colorScheme.brand.primary
            )
            Text(
                text = "Choose a photo for your New Team",
                style = Theme.typography.label.small,
                color = Theme.colorScheme.text.tertiary
            )
            
            Spacer(modifier = Modifier.height(Theme.spacing.lg))
            
            AppTextField(
                value = state.newTeamName,
                onValueChange = onNameChange,
                placeholder = "Enter Your New Team Name Here",
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(Theme.spacing.md))
            
            AppTextField(
                value = state.newTeamInviteCode,
                onValueChange = onInviteCodeChange,
                placeholder = "Enter Your Team's Invite Code",
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(Theme.spacing.xl))
            
            AppButton(
                text = if (state.isSubmittingTeam) "Adding..." else "Add Team",
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSubmittingTeam
            )
        }
    }
}

@Composable
fun SuccessDialog(onDismiss: () -> Unit) {
    AppConfirmationDialog(
        title = "Confirmation",
        description = "Your New Team Has Been Added Successfully",
        primaryActionText = "OK",
        onPrimaryAction = onDismiss,
        onDismiss = onDismiss
    )
}

@Preview(showBackground = true)
@Composable
fun TeamsScreenPreview() {
    val sampleTeams = listOf(
        Team(
            id = "1",
            name = "Mobile Development",
            photoUrl = "",
            memberCount = 5,
            progress = 75,
            events = emptyList()
        ),
        Team(
            id = "2",
            name = "Backend Services",
            photoUrl = "",
            memberCount = 3,
            progress = 40,
            events = emptyList()
        )
    )
    val state = TeamsState(teams = sampleTeams)
    MongezTheme {
        TeamsScreenContent(
            state = state,
            snackbarVisible = false,
            snackbarMessage = null,
            onIntent = {},
            onNavigateToCourseDetails = {},
            onNavigateToTeamDetails = { _, _ -> }
        )
    }
}
