plugins {
    id("mongez.android.library.compose")
    id("mongez.android.hilt")
}

android {
    namespace = "com.iti.mongez.org.presentation"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":design_system"))

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.hilt.navigation.compose)
}
