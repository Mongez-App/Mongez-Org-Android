package com.iti.mongez.org.presentation.subscription.checkout

import android.content.Context
import com.iti.mongez.org.domain.subscription.model.PaymentCheckoutSession
import com.iti.mongez.org.domain.subscription.model.PaymentOutcome

/**
 * Paused pending the real Paymob native Android SDK artifact.
 *
 * The public GitLab repo this was previously sourced from (gitlab.com/paymob-public/paymobsdk)
 * turned out to be Paymob's iOS SDK (CocoaPods/Swift), not Android - every Gradle coordinate
 * derived from it was a dead end. The only publicly resolvable Android artifact found
 * (com.github.PaymobAccept:Android-SDK on JitPack) implements Paymob's older "Accept"
 * iframe/payment_key API, launched via startActivityForResult - not the Intention/client_secret
 * flow this feature's data layer (PaymobApi, CreatePaymentIntentionUseCase,
 * VerifyAndActivateSubscriptionUseCase) is built around, and matching what Paymob's current docs
 * describe. That SDK appears to be distributed directly by Paymob (dashboard/account team)
 * rather than publicly, so it needs to come from them before this class can be finished.
 *
 * Once you have the real SDK:
 *  1. Add its dependency to gradle/libs.versions.toml + presentation/build.gradle.kts.
 *  2. Replace the body of [launch] below with the real Builder/listener (or equivalent) call,
 *     using [session]'s clientSecret/publicKey and forwarding the result to [onResult] as a
 *     [PaymentOutcome].
 * Nothing else in this feature needs to change - [PaymobCheckoutLauncher] is the only seam
 * between the MVI loop and the SDK.
 */
class PaymobSdkCheckoutLauncher : PaymobCheckoutLauncher {

    override fun launch(
        context: Context,
        session: PaymentCheckoutSession,
        onResult: (PaymentOutcome) -> Unit,
    ) {
        onResult(
            PaymentOutcome.Failed(
                message = "Native checkout isn't wired up yet - see docs/PAYMOB_INTEGRATION.md.",
            ),
        )
    }
}
