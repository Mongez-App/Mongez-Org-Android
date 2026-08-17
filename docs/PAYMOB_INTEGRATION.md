# Paymob subscription payments — integration notes

This document explains the "pay before you reach MainScreen" flow: what's wired up, what's
paused, and why.

## What's implemented

| Layer | Files |
|---|---|
| Domain (`:domain`) | `subscription/model/*`, `subscription/repository/*`, `subscription/usecase/*`, `subscription/util/PaymobHmacValidator.kt` |
| Data (`:data`) | `subscription/local/SubscriptionDataStore.kt` (per-user Preferences DataStore), `subscription/remote/*` (Retrofit API + DTOs + mapper + `PaymobConfig` BuildConfig wrapper), `subscription/di/*`, `subscription/repository/*`, `subscription/session/FirebaseCurrentUserIdProvider.kt` |
| Presentation (`:presentation`) | `subscription/planselection/*` (Contract/State/Effect/ViewModel/Screen), `subscription/checkout/*` (Paymob SDK launcher seam - **paused**, see below), `subscription/gate/SubscriptionGateViewModel.kt` |
| Navigation (`:navigation`) | `AppRoute.PlanSelection`, wired into `AppNavHost.kt` |
| Config | `local.properties` (Paymob keys), `data/build.gradle.kts` (BuildConfig fields) |

## The flow, as implemented

1. **Login / registration** — unchanged existing flows (Firebase-backed).
2. **Post-auth gate** — both the cold-start path (`SplashViewModel`, after `VerifyAuthTokenUseCase`
   succeeds) and the interactive path (`AppNavHost`, on `LoginEffect.NavigateToHome` /
   `RegisterEffect.NavigateToHome`) call `ObserveSubscriptionStateUseCase`/`SubscriptionGateViewModel`
   before deciding between `AppRoute.Main` and `AppRoute.PlanSelection`. This matters for the
   "log out, sign in as a different unpaid account, get prompted to pay again" requirement -
   a splash-only check wouldn't catch an in-session account switch.
3. **Plan selection** — `PlanSelectionScreen` shows `SubscriptionPlanCatalog.plans` (Monthly/Yearly
   placeholders - **edit the prices** in `domain/subscription/model/SubscriptionPlan.kt`, no real
   pricing was given).
4. **Payment execution** — `ConfirmPurchase` intent → `CreatePaymentIntentionUseCase` calls
   Paymob's `POST /v1/intention/` directly from the device (no backend, as requested) → on
   success, `PlanSelectionEffect.LaunchPaymobCheckout` is collected in `AppNavHost`, which calls
   `PaymobSdkCheckoutLauncher.launch(...)`.
5. **Success & persistence** — the SDK's raw callback is **not** trusted by itself.
   `VerifyAndActivateSubscriptionUseCase` re-checks it (HMAC, via `PaymobHmacValidator`) and
   only then writes to `SubscriptionDataStore`, keyed by the current user's Firebase uid.
6. **Redirection** — on successful verification, `PlanSelectionEffect.NavigateToMain` is
   emitted and handled in `AppNavHost`.

## ⏸ PAUSED: the native checkout launcher

`PaymobSdkCheckoutLauncher` currently just returns `PaymentOutcome.Failed(...)` - tapping
"Pay" will create a real Paymob intention (step 4 above works end-to-end) and then immediately
show a "not wired up yet" error instead of opening a checkout UI. Everything else in the app
builds and runs.

**Why:** the SDK integration this was originally built against (`PaymobSdk.Builder(context,
clientSecret, publicKey, listener).build().start()`, a `PaymobSdkListener` interface, resolved
via `com.gitlab.paymob-public:paymobsdk` on JitPack) turned out to be based on a mix-up:

- `gitlab.com/paymob-public/paymobsdk` is Paymob's **iOS** SDK (CocoaPods/Swift, `Paymob.podspec`,
  no Android code at all) - not Android. Every Android Gradle coordinate derived from it
  (`com.paymob.sdk:Paymob-SDK`, `com.gitlab.paymob-public:paymobsdk`, at several versions, against
  JitPack/Maven Central/Google) failed to resolve, both from this environment and from a real
  Android Studio machine.
- The one real, publicly resolvable Android artifact found is
  `com.github.PaymobAccept:Android-SDK:0.1.2` on JitPack (github.com/PaymobAccept/Android-SDK).
  Its actual source (github.com/PaymobAccept/Android-TestApp) shows it implements Paymob's
  **older "Accept" iframe API** - `payment_key` + `iframe_id`, launched via
  `startActivityForResult(Intent(this, Pay::class.java), ...)`, result read back from
  `onActivityResult` extras (`success`, `ID`, `amount_cents`, ...). That's a different API
  family from the Intention/`client_secret` flow this project's data layer is built around
  (which matches what Paymob's *current* docs describe conceptually), so it can't be dropped in
  as-is.
- Paymob's current official docs (`developers.paymob.com/paymob-docs/integration-paths/mobile-sdks`)
  describe the Intention → `client_secret` → native SDK flow at a conceptual level but don't
  publish a Gradle coordinate anywhere in the public docs. It looks like the current SDK is
  handed out directly by Paymob (dashboard/account team), not distributed via a public repo.

**Decision:** rather than keep guessing coordinates or silently switching your integration to
the older iframe API, this is paused until you get the real SDK from Paymob.

**To finish it:**
1. Ask Paymob support/your account team for the native Android SDK (the one matching the
   Intention/`client_secret`/Mobile-SDK flow, not "Accept"/iframe) - the artifact, its Maven
   coordinate or AAR, and its actual Builder/listener API shape.
2. Add the dependency to `gradle/libs.versions.toml` + `presentation/build.gradle.kts` (both
   already have commented-out placeholders pointing at this file).
3. Replace the body of `PaymobSdkCheckoutLauncher.launch(...)`
   (`presentation/src/main/java/com/iti/mongez/org/presentation/subscription/checkout/PaymobSdkCheckoutLauncher.kt`)
   with the real call, using `session.clientSecret` / `session.publicKey`, and forward the
   result to `onResult` as a `PaymentOutcome`. Nothing else in the feature needs to change -
   `PaymobCheckoutLauncher` is the only seam between the MVI loop and the SDK.

If Paymob's account team instead points you at the older Accept/iframe API, that's a bigger
change: the data layer would need a different endpoint (a "payment key" endpoint, not
`/v1/intention/`), a different DTO shape, and the launcher would use an Activity-result pattern
instead of a listener - ask before assuming which one applies to your integration ID.

## ⚠️ Security trade-offs you're accepting

You asked for **no backend at all**, so a few things are embedded in the app that normally
wouldn't be:

- **`PAYMOB_SECRET_KEY` and `PAYMOB_API_KEY`** are meant to be server-only. They're compiled
  into `:data`'s `BuildConfig`, shipping as plain strings inside your APK/AAB, extractable by
  anyone who decompiles the app. Someone with the secret key could, in principle, create
  arbitrary Paymob intentions against your account. `PAYMOB_PUBLIC_KEY` is fine to ship
  (Paymob designed it to be client-safe). Paymob's own docs describe intention creation as a
  *backend* responsibility precisely because of this.
- **`PAYMOB_HMAC_SECRET`** is also embedded, used to verify the SDK's payment callback
  client-side (`PaymobPaymentGatewayImpl.verifyTransaction`). This is a workable pattern for
  backend-less integrations, but a sufficiently motivated attacker who extracts the secret
  could forge a "verified" success locally (they still can't get Paymob to actually process a
  fraudulent charge through your integration ID, but they could unlock the app for free).
- The unrelated secrets pasted alongside the Paymob ones (Supabase service-role key, Zernio,
  OpenWA) are **not** used anywhere in this app - they're backend-only credentials with no
  legitimate reason to ever ship inside a mobile app.

If you revisit this later, the standard fix is a tiny serverless function that does nothing
but mint the Paymob `client_secret` server-side - the app would call *that* instead of Paymob
directly, and `PAYMOB_SECRET_KEY`/`PAYMOB_API_KEY`/`PAYMOB_HMAC_SECRET` would move there and
never touch the app again.

## Before you build

1. Get the real SDK from Paymob (see "PAUSED" section above).
2. Re-check `PaymobApi.kt`'s `Authorization` header ("Token {secret_key}") against a real
   response - Paymob's v1 family has historically used this scheme, but confirm against a
   real 401/200 from your account.
3. Edit the placeholder prices in `SubscriptionPlanCatalog` (`domain/subscription/model/SubscriptionPlan.kt`).
4. Confirm `PaymobHmacValidator.TRANSACTION_CALLBACK_FIELD_ORDER` against your Paymob
   dashboard's HMAC field list for the Intention/Unified Checkout flow specifically (the list
   currently wired matches the classic "transaction processed" callback documentation).
5. Run a real test payment with your `egy_*_test_*` keys before ever touching live keys.
