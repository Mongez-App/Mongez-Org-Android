package com.iti.mongez.org.data.subscription.remote.mapper

import com.iti.mongez.org.data.subscription.remote.PaymobConfig
import com.iti.mongez.org.data.subscription.remote.dto.PaymobBillingDataDto
import com.iti.mongez.org.data.subscription.remote.dto.PaymobIntentionRequestDto
import com.iti.mongez.org.data.subscription.remote.dto.PaymobIntentionResponseDto
import com.iti.mongez.org.data.subscription.remote.dto.PaymobItemDto
import com.iti.mongez.org.domain.subscription.model.BillingData
import com.iti.mongez.org.domain.subscription.model.PaymentCheckoutSession
import com.iti.mongez.org.domain.subscription.model.PaymentException
import com.iti.mongez.org.domain.subscription.model.SubscriptionPlan
import kotlin.math.roundToLong

fun SubscriptionPlan.toIntentionRequestDto(
    billingData: BillingData,
    merchantOrderReference: String,
    config: PaymobConfig,
): PaymobIntentionRequestDto {
    val amountCents = (priceEgp * 100).roundToLong()
    return PaymobIntentionRequestDto(
        amountCents = amountCents,
        currency = currency,
        paymentMethods = listOf(config.cardIntegrationId),
        items = listOf(
            PaymobItemDto(
                name = title,
                amountCents = amountCents,
                description = description,
            ),
        ),
        billingData = billingData.toDto(),
        specialReference = merchantOrderReference,
        expirationSeconds = config.intentionExpirationSeconds,
    )
}

fun BillingData.toDto(): PaymobBillingDataDto = PaymobBillingDataDto(
    firstName = firstName,
    lastName = lastName,
    email = email,
    phoneNumber = phoneNumber,
)

fun PaymobIntentionResponseDto.toDomain(
    plan: SubscriptionPlan,
    publicKey: String,
    merchantOrderReference: String,
): PaymentCheckoutSession {
    val secret = clientSecret ?: throw PaymentException("Paymob intention response was missing client_secret.")
    return PaymentCheckoutSession(
        clientSecret = secret,
        publicKey = publicKey,
        merchantOrderReference = merchantOrderReference,
        plan = plan,
    )
}
