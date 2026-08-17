package com.iti.mongez.org.domain.subscription.usecase

import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.subscription.model.BillingData
import com.iti.mongez.org.domain.subscription.model.PaymentCheckoutSession
import com.iti.mongez.org.domain.subscription.model.SubscriptionPlan
import com.iti.mongez.org.domain.subscription.repository.PaymentGatewayRepository
import java.util.UUID
import javax.inject.Inject

/** Step 4 of the flow: kicks off the native Paymob checkout for the chosen [plan]. */
class CreatePaymentIntentionUseCase @Inject constructor(
    private val paymentGatewayRepository: PaymentGatewayRepository,
) {
    suspend operator fun invoke(
        plan: SubscriptionPlan,
        billingData: BillingData,
    ): Result<PaymentCheckoutSession> {
        val merchantOrderReference = "mongez-${plan.id}-${UUID.randomUUID()}"
        return paymentGatewayRepository.createCheckoutSession(
            plan = plan,
            billingData = billingData,
            merchantOrderReference = merchantOrderReference,
        )
    }
}
