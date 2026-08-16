package com.iti.mongez.org.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.iti.mongez.org.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.org.designsystem.components.snackbar.TopSnackbar
import com.iti.mongez.org.presentation.auth.login.uiState.LoginEffect
import com.iti.mongez.org.presentation.auth.login.view.LoginScreen
import com.iti.mongez.org.presentation.auth.login.viewmodel.LoginViewModel
import com.iti.mongez.org.presentation.auth.register.contract.RegisterIntent
import com.iti.mongez.org.presentation.auth.register.uiState.RegisterEffect
import com.iti.mongez.org.presentation.auth.register.view.ReviewScreen
import com.iti.mongez.org.presentation.auth.register.view.SignUp1Screen
import com.iti.mongez.org.presentation.auth.register.view.SignUp2Screen
import com.iti.mongez.org.presentation.auth.register.view.SignUp3Screen
import com.iti.mongez.org.presentation.auth.register.view.SignUp4Screen
import com.iti.mongez.org.presentation.auth.register.viewmodel.RegisterViewModel
import com.iti.mongez.org.presentation.coursedetails.view.CourseDetailsScreen
import com.iti.mongez.org.presentation.courses.view.CoursesScreen
import com.iti.mongez.org.presentation.main.MainScreen
import com.iti.mongez.org.presentation.team_details.view.TeamDetailsScreen
import kotlinx.coroutines.delay

@Composable
fun AppNavHost(
    initialBackStack: List<AppRoute>
) {
    val backStack = remember { mutableStateListOf(*initialBackStack.toTypedArray()) }
    
    var snackbarVisible by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    
    val showErrorSnackbar: (String) -> Unit = { message ->
        snackbarMessage = message
        snackbarVisible = true
    }
    
    LaunchedEffect(snackbarVisible) {
        if (snackbarVisible) {
            delay(3000)
            snackbarVisible = false
        }
    }
    
    // Hoist RegisterViewModel so it's shared across all registration steps.
    // In a real app we might scope this more tightly, but for now this works.
    val registerViewModel: RegisterViewModel = hiltViewModel()
    val registerState by registerViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        registerViewModel.effect.collect { effect ->
            when (effect) {
                is RegisterEffect.NavigateToHome -> {
                    backStack.clear()
                    backStack.add(AppRoute.Main)
                }
                is RegisterEffect.NavigateToLogin -> {
                    backStack.clear()
                    backStack.add(AppRoute.Login)
                }
                is RegisterEffect.ShowError -> {
                    showErrorSnackbar(effect.message)
                }
                is RegisterEffect.NavigateToStep -> {
                    when (effect.step) {
                        1 -> {
                            if (backStack.lastOrNull() != AppRoute.SignUp1) {
                                backStack.removeAll { it is AppRoute.SignUp2 || it is AppRoute.SignUp3 || it is AppRoute.SignUp4 }
                                if (!backStack.contains(AppRoute.SignUp1)) {
                                    backStack.add(AppRoute.SignUp1)
                                }
                            }
                        }
                        2 -> {
                            if (backStack.lastOrNull() != AppRoute.SignUp2) {
                                backStack.removeAll { it is AppRoute.SignUp3 || it is AppRoute.SignUp4 }
                                if (!backStack.contains(AppRoute.SignUp2)) {
                                    backStack.add(AppRoute.SignUp2)
                                }
                            }
                        }
                        3 -> {
                            if (backStack.lastOrNull() != AppRoute.SignUp3) {
                                backStack.removeAll { it is AppRoute.SignUp4 }
                                if (!backStack.contains(AppRoute.SignUp3)) {
                                    backStack.add(AppRoute.SignUp3)
                                }
                            }
                        }
                        4 -> {
                            if (backStack.lastOrNull() != AppRoute.SignUp4) {
                                backStack.add(AppRoute.SignUp4)
                            }
                        }
                        5 -> {
                            backStack.clear()
                            backStack.add(AppRoute.UnderReview)
                        }
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() }
        ) { key ->
            when (key) {
                is AppRoute.Onboarding -> NavEntry(AppRoute.Onboarding) {
                    PlaceholderScreen("Onboarding")
                }
            is AppRoute.Login -> NavEntry(AppRoute.Login) {
                val loginViewModel: LoginViewModel = hiltViewModel()
                LaunchedEffect(Unit) {
                    loginViewModel.effect.collect { effect ->
                        when (effect) {
                            is LoginEffect.NavigateToHome -> {
                                backStack.clear()
                                backStack.add(AppRoute.Main)
                            }
                            is LoginEffect.NavigateToSignUp -> {
                                backStack.add(AppRoute.SignUp1)
                            }
                            is LoginEffect.LaunchGoogleSignIn -> {
                                // Handled in LoginScreen or here
                            }
                            is LoginEffect.ShowError -> {
                                showErrorSnackbar(effect.message)
                            }
                        }
                    }
                }
                val loginState by loginViewModel.uiState.collectAsState()
                LoginScreen(
                    uiState = loginState,
                    onIntent = loginViewModel::onIntent
                )
            }
            is AppRoute.SignUp1 -> NavEntry(AppRoute.SignUp1) {
                // Intercept step navigation
                val onIntent: (RegisterIntent) -> Unit = { intent ->
                    if (intent == RegisterIntent.OnBackToLogin) {
                        backStack.removeLastOrNull()
                    } else {
                        registerViewModel.onIntent(intent)
                    }
                }
                SignUp1Screen(
                    uiState = registerState,
                    onIntent = onIntent
                )
            }
            is AppRoute.SignUp2 -> NavEntry(AppRoute.SignUp2) {
                val onIntent: (RegisterIntent) -> Unit = { intent ->
                    if (intent == RegisterIntent.OnStep2Back) {
                        backStack.removeLastOrNull()
                    } else {
                        registerViewModel.onIntent(intent)
                    }
                }
                SignUp2Screen(
                    uiState = registerState,
                    onIntent = onIntent
                )
            }
            is AppRoute.SignUp3 -> NavEntry(AppRoute.SignUp3) {
                val onIntent: (RegisterIntent) -> Unit = { intent ->
                    if (intent == RegisterIntent.OnStep3Back) {
                        backStack.removeLastOrNull()
                    } else {
                        registerViewModel.onIntent(intent)
                    }
                }
                SignUp3Screen(
                    uiState = registerState,
                    onIntent = onIntent
                )
            }
            is AppRoute.SignUp4 -> NavEntry(AppRoute.SignUp4) {
                val onIntent: (RegisterIntent) -> Unit = { intent ->
                    if (intent == RegisterIntent.OnStep4Back) {
                        backStack.removeLastOrNull()
                    } else if (intent == RegisterIntent.OnNavigateToLocationPicker) {
                        backStack.add(AppRoute.LocationPicker)
                    } else {
                        registerViewModel.onIntent(intent)
                    }
                }
                SignUp4Screen(
                    uiState = registerState,
                    onIntent = onIntent
                )
            }
            is AppRoute.LocationPicker -> NavEntry(AppRoute.LocationPicker) {
                com.iti.mongez.org.presentation.auth.register.view.LocationPickerScreen(
                    initialLat = registerState.latitude,
                    initialLng = registerState.longitude,
                    onLocationSelected = { lat, lng, address ->
                        registerViewModel.onIntent(RegisterIntent.OnLocationChanged(lat, lng))
                        registerViewModel.onIntent(RegisterIntent.OnAddressChanged(address))
                        backStack.removeLastOrNull()
                    },
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }
            is AppRoute.UnderReview -> NavEntry(AppRoute.UnderReview) {
                ReviewScreen(
                    uiState = registerState,
                    onIntent = registerViewModel::onIntent
                )
            }
            is AppRoute.Verified -> NavEntry(AppRoute.Verified) {
                ReviewScreen(
                    uiState = registerState,
                    onIntent = registerViewModel::onIntent
                )
            }
                is AppRoute.Main -> NavEntry(AppRoute.Main) {
                    MainScreen(
                        onNavigateToCourseDetails = { courseId -> backStack.add(AppRoute.CourseDetails(courseId)) },
                        onNavigateToTeamDetails = { teamId -> backStack.add(AppRoute.TeamDetails(teamId)) },
                        onNavigateToLogin = {
                            backStack.clear()
                            backStack.add(AppRoute.Login)
                        }
                    )
                }
            is AppRoute.Courses -> NavEntry(AppRoute.Courses) {
                CoursesScreen(
                    innerPadding = PaddingValues(0.dp), // NavDisplay handles layout, but CoursesScreen expects PaddingValues
                    onCourseClick = { courseId -> backStack.add(AppRoute.CourseDetails(courseId)) },
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }
            is AppRoute.TeamDetails -> NavEntry(key) {
                TeamDetailsScreen(
                    teamId = key.teamId,
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onNavigateToCourseDetails = { courseId: String -> backStack.add(AppRoute.CourseDetails(courseId)) }
                )
            }
            is AppRoute.CourseDetails -> NavEntry(key) {
                CourseDetailsScreen(
                    courseId = key.courseId,
                    onNavigateBack = {
                        backStack.remove(key)
                    },
                    onNavigateToStudyRoom = { taskId, title ->
//                        backStack.add(AppRoute.StudyRoom(taskId, title))
                    },
                )
            }
            is AppRoute.Events -> NavEntry(AppRoute.Events) {
                PlaceholderScreen("Events")
            }
            is AppRoute.Tasks -> NavEntry(AppRoute.Tasks) {
                PlaceholderScreen("Tasks")
            }
            is AppRoute.MagicBox -> NavEntry(AppRoute.MagicBox) {
                PlaceholderScreen("Magic Box")
            }
        }
        }
        
        TopSnackbar(
            visible = snackbarVisible,
            message = snackbarMessage,
            type = AppSnackbarType.Error,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun PlaceholderScreen(screenName: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = screenName)
    }
}
