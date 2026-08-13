plugins {
    `kotlin-dsl`
}

group = "com.iti.mongez.org.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.hilt.android.plugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "mongez.android.application"
            implementationClass = "com.iti.mongez.org.AndroidApplicationConventionPlugin"
        }
        register("compose") {
            id = "mongez.compose"
            implementationClass = "com.iti.mongez.org.ComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "mongez.android.library"
            implementationClass = "com.iti.mongez.org.AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "mongez.android.library.compose"
            implementationClass = "com.iti.mongez.org.AndroidLibraryComposeConventionPlugin"
        }
        register("kotlinLibrary") {
            id = "mongez.kotlin.library"
            implementationClass = "com.iti.mongez.org.KotlinLibraryConventionPlugin"
        }
        register("androidHilt") {
            id = "mongez.android.hilt"
            implementationClass = "com.iti.mongez.org.AndroidHiltConventionPlugin"
        }
    }
}
