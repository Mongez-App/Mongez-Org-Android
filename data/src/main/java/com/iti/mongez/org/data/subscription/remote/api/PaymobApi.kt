package com.iti.mongez.org.data.subscription.remote.api

import com.iti.mongez.org.data.subscription.remote.dto.PaymobIntentionRequestDto
import com.iti.mongez.org.data.subscription.remote.dto.PaymobIntentionResponseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Talks to Paymob's Unified Intention API directly from the app (no backend involved).
 *
 * NOTE ON AUTH: Paymob's own documentation for this endpoint was not fully accessible
 * while wiring this up - the "Token {secret_key}" scheme below is Paymob's long-standing
 * convention across their v1 API family. Verify against your Paymob dashboard / the
 * response you get back before shipping; if you see 401s, the most likely fix is
 * swapping this for `"Bearer ${secretKey}"`.
 */
interface PaymobApi {

    @POST("v1/intention/")
    suspend fun createIntention(
        @Header("Authorization") authorization: String,
        @Body request: PaymobIntentionRequestDto,
    ): PaymobIntentionResponseDto
}
