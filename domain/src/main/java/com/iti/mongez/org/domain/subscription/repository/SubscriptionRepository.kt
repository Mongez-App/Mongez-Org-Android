package com.iti.mongez.org.domain.subscription.repository

import com.iti.mongez.org.domain.subscription.model.ActiveSubscription
import com.iti.mongez.org.domain.subscription.model.SubscriptionState
import kotlinx.coroutines.flow.Flow

/**
 * Local persistence for "does this user have a paid subscription". Deliberately scoped
 * by [userId] on every call so that logging out and signing in as a different, unpaid
 * account always re-prompts for payment (nothing here is a single global flag).
 */
interface SubscriptionRepository {
    fun observeSubscriptionState(userId: String): Flow<SubscriptionState>
    suspend fun saveActiveSubscription(userId: String, subscription: ActiveSubscription)
    suspend fun clearSubscription(userId: String)
}
