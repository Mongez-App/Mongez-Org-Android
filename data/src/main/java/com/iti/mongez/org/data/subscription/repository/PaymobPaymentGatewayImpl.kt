package com.iti.mongez.org.data.subscription.repository

import com.iti.mongez.org.data.subscription.remote.PaymobConfig
import com.iti.mongez.org.data.subscription.remote.api.PaymobApi
import com.iti.mongez.org.data.subscription.remote.mapper.toDomain
import com.iti.mongez.org.data.subscription.remote.mapper.toIntentionRequestDto
import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.subscription.model.BillingData
import com.iti.mongez.org.domain.subscription.model.PaymentCheckoutSession
import com.iti.mongez.org.domain.subscription.model.SubscriptionPlan
import com.iti.mongez.org.domain.subscription.model.VerifiedTransaction
import com.iti.mongez.org.domain.subscription.repository.PaymentGatewayRepository
import com.iti.mongez.org.domain.subscription.util.PaymobHmacValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Calls Paymob directly from the client - the intentional, backend-free integration
 * this project asked for. [PaymobConfig] is the *only* thing in this class that ever
 * touches the raw secret/API/HMAC keys; nothing above the data layer sees them.
 */
class PaymobPaymentGatewayImpl @Inject constructor(
    private val paymobApi: PaymobApi,
    private val paymobConfig: PaymobConfig,
) : PaymentGatewayRepository {

    override suspend fun createCheckoutSession(
        plan: SubscriptionPlan,
        billingData: BillingData,
        merchantOrderReference: String,
    ): Result<PaymentCheckoutSession> = withContext(Dispatchers.IO) {
        try {
            val request = plan.toIntentionRequestDto(
                billingData = billingData,
                merchantOrderReference = merchantOrderReference,
                config = paymobConfig,
            )
            val response = paymobApi.createIntention(
                authorization = "Token ${paymobConfig.secretKey}",
                request = request,
            )
            Result.Success(
                response.toDomain(
                    plan = plan,
                    publicKey = paymobConfig.publicKey,
                    merchantOrderReference = merchantOrderReference,
                ),
            )
        } catch (throwable: Throwable) {
            Result.Failure(throwable)
        }
    }

    override suspend fun verifyTransaction(rawFields: Map<String, String?>): Result<VerifiedTransaction> {
        val receivedHmac = rawFields["hmac"]
        val isValid = PaymobHmacValidator.isValid(
            fields = rawFields,
            orderedFieldNames = PaymobHmacValidator.TRANSACTION_CALLBACK_FIELD_ORDER,
            hmacSecret = paymobConfig.hmacSecret,
            receivedHmac = receivedHmac,
        )

        if (!isValid) {
            return Result.Failure(
                IllegalStateException(
                    "Paymob HMAC verification failed - refusing to trust this payment result. " +
                        "If this fires for genuine successful payments, double-check " +
                        "PaymobHmacValidator.TRANSACTION_CALLBACK_FIELD_ORDER against your " +
                        "Paymob dashboard's HMAC field list.",
                ),
            )
        }

        val transactionId = rawFields["id"].orEmpty()
        val success = rawFields["success"]?.toBooleanStrictOrNull() ?: false
        val amountCents = rawFields["amount_cents"]?.toLongOrNull() ?: 0L
        val currency = rawFields["currency"] ?: paymobConfig.currency

        return Result.Success(
            VerifiedTransaction(
                transactionId = transactionId,
                success = success,
                amountCents = amountCents,
                currency = currency,
            ),
        )
    }
}
