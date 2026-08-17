package com.iti.mongez.org.data.subscription.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Request body for `POST /v1/intention/`. Field names/shape per Paymob's Unified
 * Intention API (docs: developers.paymob.com > Accept Standard Redirect > Create
 * Intention). Amounts are in the smallest currency unit (piasters for EGP).
 */
data class PaymobIntentionRequestDto(
    @SerializedName("amount") val amountCents: Long,
    @SerializedName("currency") val currency: String,
    @SerializedName("payment_methods") val paymentMethods: List<Long>,
    @SerializedName("items") val items: List<PaymobItemDto>,
    @SerializedName("billing_data") val billingData: PaymobBillingDataDto,
    @SerializedName("special_reference") val specialReference: String,
    @SerializedName("expiration") val expirationSeconds: Int,
)

data class PaymobItemDto(
    @SerializedName("name") val name: String,
    @SerializedName("amount") val amountCents: Long,
    @SerializedName("description") val description: String,
    @SerializedName("quantity") val quantity: Int = 1,
)

/**
 * Paymob rejects the intention request if any of these are missing, even when they
 * are irrelevant to a purely digital subscription - unused address fields are filled
 * with "NA" by [com.iti.mongez.org.data.subscription.remote.PaymobMapper].
 */
data class PaymobBillingDataDto(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("apartment") val apartment: String = "NA",
    @SerializedName("floor") val floor: String = "NA",
    @SerializedName("street") val street: String = "NA",
    @SerializedName("building") val building: String = "NA",
    @SerializedName("city") val city: String = "NA",
    @SerializedName("country") val country: String = "EG",
    @SerializedName("state") val state: String = "NA",
    @SerializedName("postal_code") val postalCode: String = "NA",
)

data class PaymobIntentionResponseDto(
    @SerializedName("id") val id: String?,
    @SerializedName("client_secret") val clientSecret: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("amount") val amountCents: Long?,
    @SerializedName("currency") val currency: String?,
)
