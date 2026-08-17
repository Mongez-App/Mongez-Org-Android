plugins {
    id("mongez.android.library.compose")
    id("mongez.android.hilt")
}

android {
    namespace = "com.iti.mongez.org.presentation"

    // dataBinding was enabled here as prep for a specific (wrong) Paymob SDK guess - removed
    // since the real SDK's requirements are unknown. Re-add if the real SDK needs it.
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":design_system"))

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.maps.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.coil.compose)
    implementation(libs.google.gson)
    implementation(libs.places)

    // Paymob native checkout SDK - PAUSED. gitlab.com/paymob-public/paymobsdk turned out to be
    // Paymob's iOS SDK, not Android; no public Gradle coordinate for the Android SDK was found.
    // See docs/PAYMOB_INTEGRATION.md and PaymobSdkCheckoutLauncher.kt for details and how to
    // re-enable this once you have the real artifact from Paymob.
    // implementation(libs.paymob.sdk)
}
