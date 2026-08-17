package com.iti.mongez.org.presentation.subscription.gate

import androidx.lifecycle.ViewModel
import com.iti.mongez.org.domain.subscription.model.SubscriptionState
import com.iti.mongez.org.domain.subscription.usecase.ObserveSubscriptionStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Answers "does the currently signed-in user have an active subscription right now?" for
 * navigation decisions made outside the PlanSelection MVI loop itself - both the cold-start
 * splash check and the post-login/post-registration routing in AppNavHost need this same
 * one-shot read, scoped to whoever is signed in at the time of the call.
 */
@HiltViewModel
class SubscriptionGateViewModel @Inject constructor(
    private val observeSubscriptionStateUseCase: ObserveSubscriptionStateUseCase,
) : ViewModel() {
    suspend fun isSubscribed(): Boolean =
        observeSubscriptionStateUseCase().first() is SubscriptionState.Subscribed
}
