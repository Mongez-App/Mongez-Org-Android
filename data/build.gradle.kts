import java.io.FileInputStream
import java.util.Properties

plugins {
    id("mongez.android.library")
    id("mongez.android.hilt")
}

// Paymob credentials are kept out of source control in local.properties and
// surfaced to Kotlin code only inside this module's generated BuildConfig.
// See docs/PAYMOB_INTEGRATION.md for why PAYMOB_SECRET_KEY / PAYMOB_API_KEY
// being embedded client-side is a deliberate (and normally discouraged)
// trade-off made because this project has no backend for payments.
val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(FileInputStream(localPropertiesFile))
    }
}

fun paymobProperty(key: String, default: String = ""): String =
    localProperties.getProperty(key) ?: System.getenv(key) ?: default

android {
    namespace = "com.iti.mongez.org.data"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "PAYMOB_BASE_URL", "\"${paymobProperty("PAYMOB_BASE_URL", "https://accept.paymob.com")}\"")
        buildConfigField("String", "PAYMOB_PUBLIC_KEY", "\"${paymobProperty("PAYMOB_PUBLIC_KEY")}\"")
        buildConfigField("String", "PAYMOB_SECRET_KEY", "\"${paymobProperty("PAYMOB_SECRET_KEY")}\"")
        buildConfigField("String", "PAYMOB_API_KEY", "\"${paymobProperty("PAYMOB_API_KEY")}\"")
        buildConfigField("String", "PAYMOB_HMAC_SECRET", "\"${paymobProperty("PAYMOB_HMAC_SECRET")}\"")
        buildConfigField("String", "PAYMOB_INTEGRATION_ID_CARD", "\"${paymobProperty("PAYMOB_INTEGRATION_ID_CARD")}\"")
        buildConfigField("String", "PAYMOB_CURRENCY", "\"${paymobProperty("PAYMOB_CURRENCY", "EGP")}\"")
        buildConfigField("int", "PAYMOB_INTENTION_EXPIRATION", paymobProperty("PAYMOB_INTENTION_EXPIRATION", "3600"))
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.google.gson)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.firebase.auth)
    implementation(libs.logging.interceptor)
}
