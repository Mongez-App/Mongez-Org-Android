package com.iti.mongez.org.domain.subscription.model

/**
 * Minimal customer info Paymob's Intention API requires as `billing_data`.
 * Unused address sub-fields are defaulted to "NA" by the data-layer mapper -
 * Paymob rejects the request if they are missing entirely.
 */
data class BillingData(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
)
