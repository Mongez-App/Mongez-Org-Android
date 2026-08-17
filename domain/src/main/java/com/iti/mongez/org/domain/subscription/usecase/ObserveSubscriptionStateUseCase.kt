package com.iti.mongez.org.domain.subscription.usecase

import com.iti.mongez.org.domain.subscription.model.SubscriptionState
import com.iti.mongez.org.domain.subscription.repository.CurrentUserIdProvider
import com.iti.mongez.org.domain.subscription.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Step 2 of the flow: instead of navigating straight to MainScreen, the splash /
 * app-entry point collects this to decide between PlanSelectionScreen and MainScreen.
 * Scoped to whoever is currently signed in - no signed-in user means "not subscribed".
 */
class ObserveSubscriptionStateUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository,
    private val currentUserIdProvider: CurrentUserIdProvider,
) {
    operator fun invoke(): Flow<SubscriptionState> {
        val userId = currentUserIdProvider.currentUserId()
            ?: return flowOf(SubscriptionState.NotSubscribed)
        return subscriptionRepository.observeSubscriptionState(userId)
    }
}
