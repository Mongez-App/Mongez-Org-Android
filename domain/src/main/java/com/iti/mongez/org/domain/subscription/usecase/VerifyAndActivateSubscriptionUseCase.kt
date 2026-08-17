package com.iti.mongez.org.domain.subscription.usecase

import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.subscription.model.ActiveSubscription
import com.iti.mongez.org.domain.subscription.model.PaymentException
import com.iti.mongez.org.domain.subscription.model.SubscriptionPlan
import com.iti.mongez.org.domain.subscription.model.expiryFrom
import com.iti.mongez.org.domain.subscription.repository.CurrentUserIdProvider
import com.iti.mongez.org.domain.subscription.repository.PaymentGatewayRepository
import com.iti.mongez.org.domain.subscription.repository.SubscriptionRepository
import javax.inject.Inject

/**
 * Step 5 of the flow: the native SDK's own callback is never trusted by itself. This
 * re-verifies the transaction (HMAC) through the data layer and only then persists the
 * subscription as active, scoped to the currently signed-in user.
 */
class VerifyAndActivateSubscriptionUseCase @Inject constructor(
    private val paymentGatewayRepository: PaymentGatewayRepository,
    private val subscriptionRepository: SubscriptionRepository,
    private val currentUserIdProvider: CurrentUserIdProvider,
) {
    suspend operator fun invoke(
        plan: SubscriptionPlan,
        rawPaymentFields: Map<String, String?>,
    ): Result<ActiveSubscription> {
        val userId = currentUserIdProvider.currentUserId()
            ?: return Result.Failure(PaymentException("No signed-in user to activate a subscription for."))

        return when (val verification = paymentGatewayRepository.verifyTransaction(rawPaymentFields)) {
            is Result.Success -> {
                val verified = verification.data
                if (!verified.success) {
                    Result.Failure(PaymentException("Payment verification failed for transaction ${verified.transactionId}."))
                } else {
                    val now = System.currentTimeMillis()
                    val subscription = ActiveSubscription(
                        planId = plan.id,
                        transactionId = verified.transactionId,
                        activatedAtEpochMillis = now,
                        expiresAtEpochMillis = plan.billingCycle.expiryFrom(now),
                    )
                    subscriptionRepository.saveActiveSubscription(userId, subscription)
                    Result.Success(subscription)
                }
            }
            is Result.Failure -> verification
            Result.Loading -> Result.Loading
        }
    }
}
