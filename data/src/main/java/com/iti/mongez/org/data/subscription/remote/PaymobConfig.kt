package com.iti.mongez.org.data.subscription.remote

import com.iti.mongez.org.data.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Thin wrapper around the BuildConfig fields generated from local.properties (see
 * :data build.gradle.kts). Keeping BuildConfig access behind one small class - rather
 * than referencing `BuildConfig.PAYMOB_*` all over the repository/gateway impls - makes
 * it trivial to fake in tests and is the single place that ever touches the raw
 * secret/API keys.
 */
@Singleton
class PaymobConfig @Inject constructor() {
    val baseUrl: String get() = BuildConfig.PAYMOB_BASE_URL
    val publicKey: String get() = BuildConfig.PAYMOB_PUBLIC_KEY
    val secretKey: String get() = BuildConfig.PAYMOB_SECRET_KEY
    val apiKey: String get() = BuildConfig.PAYMOB_API_KEY
    val hmacSecret: String get() = BuildConfig.PAYMOB_HMAC_SECRET
    val cardIntegrationId: Long get() = BuildConfig.PAYMOB_INTEGRATION_ID_CARD.toLong()
    val currency: String get() = BuildConfig.PAYMOB_CURRENCY
    val intentionExpirationSeconds: Int get() = BuildConfig.PAYMOB_INTENTION_EXPIRATION
}
