package com.iti.mongez.org.domain.subscription.model

/**
 * Everything the presentation layer needs to hand off to the native Paymob checkout
 * UI. [publicKey] is Paymob's client-safe "publishable" key - it is fine for this to
 * cross the domain boundary into presentation, unlike the secret/API keys which never
 * leave the data layer.
 */
data class PaymentCheckoutSession(
    val clientSecret: String,
    val publicKey: String,
    val merchantOrderReference: String,
    val plan: SubscriptionPlan,
)
