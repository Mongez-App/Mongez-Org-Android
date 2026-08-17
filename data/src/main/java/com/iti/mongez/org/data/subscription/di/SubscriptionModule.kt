package com.iti.mongez.org.data.subscription.di

import com.iti.mongez.org.data.subscription.repository.PaymobPaymentGatewayImpl
import com.iti.mongez.org.data.subscription.repository.SubscriptionRepositoryImpl
import com.iti.mongez.org.data.subscription.session.FirebaseCurrentUserIdProvider
import com.iti.mongez.org.domain.subscription.repository.CurrentUserIdProvider
import com.iti.mongez.org.domain.subscription.repository.PaymentGatewayRepository
import com.iti.mongez.org.domain.subscription.repository.SubscriptionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * [FirebaseCurrentUserIdProvider] injects `FirebaseAuth` by constructor without a
 * dedicated @Provides here deliberately - the existing FirebaseAuthDataSource.kt
 * already requires an unqualified `FirebaseAuth` binding elsewhere in the data
 * module's DI graph, and Hilt rejects two unqualified providers for the same type.
 * If Gradle reports `FirebaseAuth` as unbound, add
 * `@Provides fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()`
 * to that existing module instead of duplicating one here.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class SubscriptionModule {

    @Binds
    @Singleton
    abstract fun bindSubscriptionRepository(impl: SubscriptionRepositoryImpl): SubscriptionRepository

    @Binds
    @Singleton
    abstract fun bindPaymentGatewayRepository(impl: PaymobPaymentGatewayImpl): PaymentGatewayRepository

    @Binds
    @Singleton
    abstract fun bindCurrentUserIdProvider(impl: FirebaseCurrentUserIdProvider): CurrentUserIdProvider
}
