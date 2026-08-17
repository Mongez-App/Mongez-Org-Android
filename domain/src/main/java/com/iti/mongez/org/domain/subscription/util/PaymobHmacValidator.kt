package com.iti.mongez.org.domain.subscription.util

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Validates the HMAC Paymob attaches to a transaction callback/response, so a native
 * SDK result can't be spoofed or tampered with client-side before the app trusts it.
 * Pure JVM crypto (no Android dependency) - safe to live in the domain module.
 *
 * Paymob computes the HMAC as HMAC-SHA512, hex-encoded, over a fixed, documented set of
 * transaction fields concatenated in a specific order. [orderedFieldNames] must match
 * whatever field set/order your Paymob HMAC calculation type expects - confirm the exact
 * list against your Paymob dashboard settings before relying on this in production.
 */
object PaymobHmacValidator {

    /**
     * @param fields the flat key/value fields returned by the transaction callback/SDK
     * @param orderedFieldNames the exact, ordered list of keys Paymob signs for this HMAC type
     * @param hmacSecret PAYMOB_HMAC_SECRET from your Paymob dashboard
     * @param receivedHmac the `hmac` value that came back alongside [fields]
     */
    fun isValid(
        fields: Map<String, String?>,
        orderedFieldNames: List<String>,
        hmacSecret: String,
        receivedHmac: String?,
    ): Boolean {
        if (receivedHmac.isNullOrBlank() || hmacSecret.isBlank()) return false

        val concatenated = orderedFieldNames.joinToString(separator = "") { key ->
            fields[key] ?: ""
        }

        val computed = hmacSha512Hex(concatenated, hmacSecret)
        return computed.equals(receivedHmac, ignoreCase = true)
    }

    private fun hmacSha512Hex(data: String, secret: String): String {
        val algorithm = "HmacSHA512"
        val mac = Mac.getInstance(algorithm)
        mac.init(SecretKeySpec(secret.toByteArray(Charsets.UTF_8), algorithm))
        val bytes = mac.doFinal(data.toByteArray(Charsets.UTF_8))
        return bytes.joinToString(separator = "") { "%02x".format(it) }
    }

    /**
     * Field order Paymob documents for the classic "transaction processed" callback.
     * The Intention/Unified Checkout flow may use a different field set - verify against
     * your Paymob dashboard > Payment Integrations > HMAC before shipping.
     */
    val TRANSACTION_CALLBACK_FIELD_ORDER: List<String> = listOf(
        "amount_cents",
        "created_at",
        "currency",
        "error_occured",
        "has_parent_transaction",
        "id",
        "integration_id",
        "is_3d_secure",
        "is_auth",
        "is_capture",
        "is_refunded",
        "is_standalone_payment",
        "is_voided",
        "order",
        "owner",
        "pending",
        "source_data.pan",
        "source_data.sub_type",
        "source_data.type",
        "success",
    )
}
