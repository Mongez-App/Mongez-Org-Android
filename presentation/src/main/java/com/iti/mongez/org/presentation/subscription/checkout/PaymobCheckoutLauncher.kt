package com.iti.mongez.org.presentation.subscription.checkout

import android.content.Context
import com.iti.mongez.org.domain.subscription.model.PaymentCheckoutSession
import com.iti.mongez.org.domain.subscription.model.PaymentOutcome

/**
 * The one integration seam between this feature's MVI loop and the actual Paymob SDK
 * types, so PlanSelectionScreen/ViewModel never import `com.paymob.sdk.*` directly.
 */
fun interface PaymobCheckoutLauncher {
    fun launch(
        context: Context,
        session: PaymentCheckoutSession,
        onResult: (PaymentOutcome) -> Unit,
    )
}
