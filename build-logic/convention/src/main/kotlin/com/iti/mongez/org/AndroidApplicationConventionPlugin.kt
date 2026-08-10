package com.iti.mongez.org

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.apply("com.android.application")
            plugins.apply("mongez.compose")

            extensions.configure<ApplicationExtension> {
                compileSdk = 37

                defaultConfig {
                    minSdk = 26
                    targetSdk = 37
                }

                signingConfigs {
                    getByName("debug") {
                        storeFile = rootProject.file("keystore/debug.keystore")
                        storePassword = "android"
                        keyAlias = "androiddebugkey"
                        keyPassword = "android"
                    }
                }

                buildTypes {
                    getByName("debug") {
                        signingConfig = signingConfigs.getByName("debug")
                    }
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
            }
        }
    }
}
