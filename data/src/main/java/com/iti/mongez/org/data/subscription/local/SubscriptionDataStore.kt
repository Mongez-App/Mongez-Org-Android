package com.iti.mongez.org.data.subscription.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.iti.mongez.org.domain.subscription.model.ActiveSubscription
import com.iti.mongez.org.domain.subscription.model.SubscriptionState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.subscriptionDataStore by preferencesDataStore(name = "subscription_prefs")

/**
 * Every key is namespaced by [userId] so a logout + sign-in as a different account
 * never inherits someone else's paid state (step 5 of the flow: "if they log out and
 * sign in with a different unpaid account, they are prompted to pay again").
 */
@Singleton
class SubscriptionDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private fun subscribedKey(userId: String) = booleanPreferencesKey("subscribed_$userId")
    private fun planIdKey(userId: String) = stringPreferencesKey("plan_id_$userId")
    private fun transactionIdKey(userId: String) = stringPreferencesKey("transaction_id_$userId")
    private fun activatedAtKey(userId: String) = longPreferencesKey("activated_at_$userId")
    private fun expiresAtKey(userId: String) = longPreferencesKey("expires_at_$userId")

    fun observeState(userId: String): Flow<SubscriptionState> =
        context.subscriptionDataStore.data.map { prefs ->
            val isSubscribed = prefs[subscribedKey(userId)] == true
            val expiresAt = prefs[expiresAtKey(userId)]

            if (!isSubscribed || expiresAt == null || expiresAt < System.currentTimeMillis()) {
                SubscriptionState.NotSubscribed
            } else {
                SubscriptionState.Subscribed(
                    subscription = ActiveSubscription(
                        planId = prefs[planIdKey(userId)].orEmpty(),
                        transactionId = prefs[transactionIdKey(userId)].orEmpty(),
                        activatedAtEpochMillis = prefs[activatedAtKey(userId)] ?: 0L,
                        expiresAtEpochMillis = expiresAt,
                    ),
                )
            }
        }

    suspend fun save(userId: String, subscription: ActiveSubscription) {
        context.subscriptionDataStore.edit { prefs ->
            prefs[subscribedKey(userId)] = true
            prefs[planIdKey(userId)] = subscription.planId
            prefs[transactionIdKey(userId)] = subscription.transactionId
            prefs[activatedAtKey(userId)] = subscription.activatedAtEpochMillis
            prefs[expiresAtKey(userId)] = subscription.expiresAtEpochMillis
        }
    }

    suspend fun clear(userId: String) {
        context.subscriptionDataStore.edit { prefs ->
            prefs.remove(subscribedKey(userId))
            prefs.remove(planIdKey(userId))
            prefs.remove(transactionIdKey(userId))
            prefs.remove(activatedAtKey(userId))
            prefs.remove(expiresAtKey(userId))
        }
    }
}
