package com.iti.mongez.org.data.subscription.di

import com.google.gson.Gson
import com.iti.mongez.org.data.subscription.remote.PaymobConfig
import com.iti.mongez.org.data.subscription.remote.api.PaymobApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Qualifies the Retrofit instance pointed at Paymob's own base URL, kept separate from
 * the app's main API Retrofit instance (different host, no shared AuthInterceptor).
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PaymobRetrofit

@Module
@InstallIn(SingletonComponent::class)
object PaymobNetworkModule {

    // Qualified (not just @Singleton OkHttpClient) so this can't collide with whatever
    // unqualified OkHttpClient the existing NetworkModule.kt already provides for the
    // main API - Hilt would otherwise reject two unqualified bindings of the same type.
    @Provides
    @Singleton
    @PaymobRetrofit
    fun providePaymobOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    @PaymobRetrofit
    fun providePaymobRetrofit(
        @PaymobRetrofit okHttpClient: OkHttpClient,
        paymobConfig: PaymobConfig,
    ): Retrofit {
        // A locally-created Gson instance is used deliberately (rather than an
        // injected, unqualified `Gson`) to avoid colliding with whatever Gson/Retrofit
        // provider the app's existing NetworkModule.kt may already declare for the
        // main API's Retrofit instance.
        val baseUrl = if (paymobConfig.baseUrl.endsWith("/")) paymobConfig.baseUrl else "${paymobConfig.baseUrl}/"
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }

    @Provides
    @Singleton
    fun providePaymobApi(@PaymobRetrofit retrofit: Retrofit): PaymobApi =
        retrofit.create(PaymobApi::class.java)
}
