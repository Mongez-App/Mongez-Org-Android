pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

includeBuild("build-logic")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Kept for whenever the real Paymob Android SDK artifact is added (currently paused -
        // see docs/PAYMOB_INTEGRATION.md). Not required by anything that's active right now.
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "MongezOrg"
include(":app")
include(":design_system")
include(":domain")
include(":data")
include(":presentation")
include(":navigation")
