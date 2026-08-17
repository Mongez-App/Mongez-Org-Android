package com.iti.mongez.org.domain.subscription.model

/**
 * Raw result the native Paymob SDK callback reports back to the app. This is
 * intentionally NOT trusted as proof of payment on its own - [rawFields] is fed into
 * [com.iti.mongez.org.domain.subscription.usecase.VerifyAndActivateSubscriptionUseCase],
 * which re-validates it (via HMAC) before the subscription is ever marked active.
 */
sealed interface PaymentOutcome {
    data class Completed(val rawFields: Map<String, String?>) : PaymentOutcome
    data class Failed(val message: String) : PaymentOutcome
    data object Cancelled : PaymentOutcome
}
