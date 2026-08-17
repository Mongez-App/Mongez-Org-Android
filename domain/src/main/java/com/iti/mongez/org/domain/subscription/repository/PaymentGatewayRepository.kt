package com.iti.mongez.org.domain.subscription.repository

import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.subscription.model.BillingData
import com.iti.mongez.org.domain.subscription.model.PaymentCheckoutSession
import com.iti.mongez.org.domain.subscription.model.SubscriptionPlan
import com.iti.mongez.org.domain.subscription.model.VerifiedTransaction

/**
 * Talks to Paymob directly from the device (no backend - see docs/PAYMOB_INTEGRATION.md
 * for the security trade-off this implies). Implemented in the data layer, where the
 * BuildConfig-backed secret/HMAC keys actually live.
 */
interface PaymentGatewayRepository {

    /** Creates a Paymob "intention" for [plan] and returns what the native SDK needs to launch. */
    suspend fun createCheckoutSession(
        plan: SubscriptionPlan,
        billingData: BillingData,
        merchantOrderReference: String,
    ): Result<PaymentCheckoutSession>

    /**
     * Re-validates the raw fields the native SDK handed back (HMAC signature check)
     * before the app trusts that a payment actually succeeded.
     */
    suspend fun verifyTransaction(rawFields: Map<String, String?>): Result<VerifiedTransaction>
}
