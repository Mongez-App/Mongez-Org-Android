plugins {
    id("mongez.android.library")
    id("mongez.android.hilt")
}

android {
    namespace = "com.iti.mongez.org.domain"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
