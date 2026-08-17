package com.iti.mongez.org.domain.subscription.model

/** A subscription that has been paid for and verified, persisted per user in DataStore. */
data class ActiveSubscription(
    val planId: String,
    val transactionId: String,
    val activatedAtEpochMillis: Long,
    val expiresAtEpochMillis: Long,
)

/** What [com.iti.mongez.org.domain.subscription.usecase.ObserveSubscriptionStateUseCase] reports. */
sealed interface SubscriptionState {
    data object Loading : SubscriptionState
    data object NotSubscribed : SubscriptionState
    data class Subscribed(val subscription: ActiveSubscription) : SubscriptionState
}

/** A transaction result re-validated (HMAC) by the data layer before it can be trusted. */
data class VerifiedTransaction(
    val transactionId: String,
    val success: Boolean,
    val amountCents: Long,
    val currency: String,
)

class PaymentException(message: String, cause: Throwable? = null) : Exception(message, cause)
