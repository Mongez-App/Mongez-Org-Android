package com.iti.mongez.org.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.org.domain.auth.usecase.GetRegistrationProgressUseCase
import com.iti.mongez.org.domain.auth.usecase.VerifyAuthTokenUseCase
import com.iti.mongez.org.domain.subscription.model.SubscriptionState
import com.iti.mongez.org.domain.subscription.usecase.ObserveSubscriptionStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashRoute {
    object ToLogin : SplashRoute()
    data class ToSignUpStep(val step: Int) : SplashRoute()
    object ToPlanSelection : SplashRoute()
    object ToMain : SplashRoute()
    object ToUnderReview : SplashRoute()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getRegistrationProgressUseCase: GetRegistrationProgressUseCase,
    private val verifyAuthTokenUseCase: VerifyAuthTokenUseCase,
    private val observeSubscriptionStateUseCase: ObserveSubscriptionStateUseCase,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _route = MutableStateFlow<SplashRoute?>(null)
    val route: StateFlow<SplashRoute?> = _route.asStateFlow()

    init {
        checkInitialState()
    }

    private fun checkInitialState() {
        viewModelScope.launch {
            val progress = getRegistrationProgressUseCase()
            if (!progress.token.isNullOrEmpty() && progress.currentStep >= 5) {
                val result = verifyAuthTokenUseCase()
                if (result is com.iti.mongez.org.domain.core.Result.Success) {
                    val isVerifiedOrg = true
                    if (isVerifiedOrg) {
                        val subscriptionState = observeSubscriptionStateUseCase().first()
                        _route.value = if (subscriptionState is SubscriptionState.Subscribed) {
                            SplashRoute.ToMain
                        } else {
                            SplashRoute.ToPlanSelection
                        }
                    } else {
                        _route.value = SplashRoute.ToUnderReview
                    }
                } else {
                    fallbackToOnboarding()
                }
            } else {
                fallbackToOnboarding()
            }
            _isLoading.value = false
        }
    }

    private suspend fun fallbackToOnboarding() {
        val step = getRegistrationProgressUseCase().currentStep
        if (step > 1) {
            _route.value = SplashRoute.ToSignUpStep(step)
        } else {
            _route.value = SplashRoute.ToLogin
        }
    }
}
