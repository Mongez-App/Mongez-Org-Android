plugins {
    id("mongez.android.library")
    id("mongez.android.hilt")
}

android {
    namespace = "com.iti.mongez.org.data"
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
