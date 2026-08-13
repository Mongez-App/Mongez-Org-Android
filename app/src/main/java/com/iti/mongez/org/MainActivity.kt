package com.iti.mongez.org

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.mongez.org.designsystem.theme.MongezTheme
import com.iti.mongez.org.navigation.AppNavHost
import com.iti.mongez.org.navigation.AppRoute
import com.iti.mongez.org.presentation.splash.SplashRoute
import com.iti.mongez.org.presentation.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        splashScreen.setKeepOnScreenCondition {
            splashViewModel.isLoading.value
        }
        
        enableEdgeToEdge()
        setContent {
            MongezTheme {
                val isLoading = splashViewModel.isLoading.collectAsStateWithLifecycle().value
                val routeState = splashViewModel.route.collectAsStateWithLifecycle().value
                
                if (!isLoading) {
                    val initialBackStack = when (routeState) {
                        is SplashRoute.ToLogin -> listOf(AppRoute.Login)
                        is SplashRoute.ToMain -> listOf(AppRoute.Main)
                        is SplashRoute.ToUnderReview -> listOf(AppRoute.UnderReview)
                        is SplashRoute.ToSignUpStep -> {
                            val stack = mutableListOf<AppRoute>(AppRoute.Login)
                            for (i in 1..routeState.step) {
                                when (i) {
                                    1 -> stack.add(AppRoute.SignUp1)
                                    2 -> stack.add(AppRoute.SignUp2)
                                    3 -> stack.add(AppRoute.SignUp3)
                                    4 -> stack.add(AppRoute.SignUp4)
                                }
                            }
                            stack
                        }
                        else -> listOf(AppRoute.Login)
                    }
                    AppNavHost(initialBackStack = initialBackStack)
                }
            }
        }
    }
}