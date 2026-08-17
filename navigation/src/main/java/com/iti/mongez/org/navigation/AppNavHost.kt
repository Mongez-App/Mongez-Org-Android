package com.iti.mongez.org.navigation

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.iti.mongez.org.presentation.subscription.checkout.PaymobWebViewActivity
import com.iti.mongez.org.presentation.subscription.gate.SubscriptionGateViewModel
import com.iti.mongez.org.presentation.subscription.planselection.contract.PlanSelectionIntent
import com.iti.mongez.org.presentation.subscription.planselection.uiState.PlanSelectionEffect
import com.iti.mongez.org.presentation.subscription.planselection.view.PlanSelectionScreen
import com.iti.mongez.org.presentation.subscription.planselection.viewmodel.PlanSelectionViewModel
import com.iti.mongez.org.presentation.team_details.view.TeamDetailsScreen
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AppNavHost(
    initialBackStack: List<AppRoute>
) {
    val backStack = rememberSaveable(
        saver = listSaver<SnapshotStateList<AppRoute>, String>(
            save = { it.map { route -> serializeRoute(route) } },
            restore = { mutableStateListOf(*it.map { str -> deserializeRoute(str) }.toTypedArray()) }
        )
    ) { mutableStateListOf(*initialBackStack.toTypedArray()) }
    
    var snackbarVisible by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarType by remember { mutableStateOf(AppSnackbarType.Error) }

    val showSnackbar: (String, AppSnackbarType) -> Unit = { message, type ->
        snackbarMessage = message
        snackbarType = type
        snackbarVisible = true
    }
    val showErrorSnackbar: (String) -> Unit = { message -> showSnackbar(message, AppSnackbarType.Error) }

    LaunchedEffect(snackbarVisible) {
        if (snackbarVisible) {
            delay(3000.milliseconds)
            snackbarVisible = false
        }
    }

    // Hoist RegisterViewModel so it's shared across all registration steps.
    // In a real app we might scope this more tightly, but for now this works.
    val registerViewModel: RegisterViewModel = hiltViewModel()
    val registerState by registerViewModel.uiState.collectAsState()

    // Gates post-auth navigation: a signed-in user without an active subscription is routed
    // to PlanSelection instead of Main, whether they just logged in, just registered, or the
    // app cold-started straight past Splash (see SplashViewModel for the cold-start case).
    val subscriptionGateViewModel: SubscriptionGateViewModel = hiltViewModel()
    val navigateAfterAuth: suspend () -> Unit = {
        val destination = if (subscriptionGateViewModel.isSubscribed()) AppRoute.Main else AppRoute.PlanSelection
        backStack.clear()
        backStack.add(destination)
    }

    LaunchedEffect(Unit) {
        registerViewModel.effect.collect { effect ->
            when (effect) {
                is RegisterEffect.NavigateToHome -> {
                    navigateAfterAuth()
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
                                navigateAfterAuth()
                            }
                            is LoginEffect.NavigateToSignUp -> {
                                backStack.add(AppRoute.SignUp1)
                            }
                            is LoginEffect.NavigateToSignUpStep2 -> {
                                backStack.add(AppRoute.SignUp2)
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
            is AppRoute.PlanSelection -> NavEntry(AppRoute.PlanSelection) {
                val planSelectionViewModel: PlanSelectionViewModel = hiltViewModel()
                val planSelectionState by planSelectionViewModel.state.collectAsState()
                val context = LocalContext.current

                val checkoutLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        val data = result.data
                        val rawFields = data?.getSerializableExtra(PaymobWebViewActivity.EXTRA_RAW_FIELDS) as? HashMap<String, String?>
                        if (rawFields != null) {
                            planSelectionViewModel.onIntent(
                                PlanSelectionIntent.PaymentSdkFinished(
                                    com.iti.mongez.org.domain.subscription.model.PaymentOutcome.Completed(rawFields)
                                )
                            )
                        } else {
                            planSelectionViewModel.onIntent(
                                PlanSelectionIntent.PaymentSdkFinished(
                                    com.iti.mongez.org.domain.subscription.model.PaymentOutcome.Cancelled
                                )
                            )
                        }
                    } else {
                        planSelectionViewModel.onIntent(
                            PlanSelectionIntent.PaymentSdkFinished(
                                com.iti.mongez.org.domain.subscription.model.PaymentOutcome.Cancelled
                            )
                        )
                    }
                }

                LaunchedEffect(Unit) {
                    planSelectionViewModel.effect.collect { effect ->
                        when (effect) {
                            is PlanSelectionEffect.LaunchPaymobCheckout -> {
                                val url = "https://accept.paymob.com/unifiedcheckout/?publicKey=${effect.session.publicKey}&clientSecret=${effect.session.clientSecret}"
                                val intent = android.content.Intent(context, PaymobWebViewActivity::class.java).apply {
                                    putExtra(PaymobWebViewActivity.EXTRA_URL, url)
                                }
                                checkoutLauncher.launch(intent)
                            }
                            is PlanSelectionEffect.ShowSnackbar -> {
                                showSnackbar(effect.message, AppSnackbarType.Success)
                            }
                            PlanSelectionEffect.NavigateToMain -> {
                                backStack.clear()
                                backStack.add(AppRoute.Main)
                            }
                        }
                    }
                }

                PlanSelectionScreen(
                    state = planSelectionState,
                    onIntent = planSelectionViewModel::onIntent,
                )
            }
                is AppRoute.Main -> NavEntry(AppRoute.Main) {
                    MainScreen(
                        onNavigateToCourseDetails = { courseId -> backStack.add(AppRoute.CourseDetails(courseId)) },
                        onNavigateToTeamDetails = { teamId, teamName -> backStack.add(AppRoute.TeamDetails(teamId, teamName)) },
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
                    teamName = key.teamName,
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
            type = snackbarType,
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

private fun serializeRoute(route: AppRoute): String {
    return when (route) {
        is AppRoute.Onboarding -> "Onboarding"
        is AppRoute.Login -> "Login"
        is AppRoute.SignUp1 -> "SignUp1"
        is AppRoute.SignUp2 -> "SignUp2"
        is AppRoute.SignUp3 -> "SignUp3"
        is AppRoute.SignUp4 -> "SignUp4"
        is AppRoute.LocationPicker -> "LocationPicker"
        is AppRoute.UnderReview -> "UnderReview"
        is AppRoute.Verified -> "Verified"
        is AppRoute.PlanSelection -> "PlanSelection"
        is AppRoute.Main -> "Main"
        is AppRoute.Courses -> "Courses"
        is AppRoute.Events -> "Events"
        is AppRoute.Tasks -> "Tasks"
        is AppRoute.MagicBox -> "MagicBox"
        is AppRoute.TeamDetails -> "TeamDetails|${route.teamId}|${route.teamName}"
        is AppRoute.CourseDetails -> "CourseDetails|${route.courseId}"
    }
}

private fun deserializeRoute(str: String): AppRoute {
    val parts = str.split("|")
    return when (parts[0]) {
        "Onboarding" -> AppRoute.Onboarding
        "Login" -> AppRoute.Login
        "SignUp1" -> AppRoute.SignUp1
        "SignUp2" -> AppRoute.SignUp2
        "SignUp3" -> AppRoute.SignUp3
        "SignUp4" -> AppRoute.SignUp4
        "LocationPicker" -> AppRoute.LocationPicker
        "UnderReview" -> AppRoute.UnderReview
        "Verified" -> AppRoute.Verified
        "PlanSelection" -> AppRoute.PlanSelection
        "Main" -> AppRoute.Main
        "Courses" -> AppRoute.Courses
        "Events" -> AppRoute.Events
        "Tasks" -> AppRoute.Tasks
        "MagicBox" -> AppRoute.MagicBox
        "TeamDetails" -> AppRoute.TeamDetails(parts[1], parts[2])
        "CourseDetails" -> AppRoute.CourseDetails(parts[1])
        else -> AppRoute.Login
    }
}
