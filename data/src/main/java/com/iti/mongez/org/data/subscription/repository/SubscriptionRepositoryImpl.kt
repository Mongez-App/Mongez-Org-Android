package com.iti.mongez.org.data.subscription.repository

import com.iti.mongez.org.data.subscription.local.SubscriptionDataStore
import com.iti.mongez.org.domain.subscription.model.ActiveSubscription
import com.iti.mongez.org.domain.subscription.model.SubscriptionState
import com.iti.mongez.org.domain.subscription.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscriptionRepositoryImpl @Inject constructor(
    private val subscriptionDataStore: SubscriptionDataStore,
) : SubscriptionRepository {

    override fun observeSubscriptionState(userId: String): Flow<SubscriptionState> =
        subscriptionDataStore.observeState(userId)

    override suspend fun saveActiveSubscription(userId: String, subscription: ActiveSubscription) {
        subscriptionDataStore.save(userId, subscription)
    }

    override suspend fun clearSubscription(userId: String) {
        subscriptionDataStore.clear(userId)
    }
}
