package com.iti.mongez.org.data.utils.network


import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.iti.mongez.org.data.local.AuthProgressDataStore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authProgressDataStore: AuthProgressDataStore,
    private val firebaseAuth: FirebaseAuth
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        // 1. Fetch fresh token directly from Firebase Auth
        val token = runBlocking {
            try {
                val freshToken = firebaseAuth.currentUser?.getIdToken(false)?.await()?.token
                if (freshToken != null) {
                    authProgressDataStore.saveToken(freshToken)
                    freshToken
                } else {
                    authProgressDataStore.getToken()
                }
            } catch (e: Exception) {
                authProgressDataStore.getToken()
            }
        }

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
            Log.e("token",token)
        }



        return chain.proceed(requestBuilder.build())
    }
}

