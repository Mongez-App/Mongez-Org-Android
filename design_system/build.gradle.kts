plugins {
    id("mongez.android.library.compose")
}

android {
    namespace = "com.iti.mongez.org.designsystem"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.splashscreen)
}
