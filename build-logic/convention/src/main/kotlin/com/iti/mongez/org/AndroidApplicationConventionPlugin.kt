package com.iti.mongez.org

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.FileInputStream
import java.util.Properties

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.apply("com.android.application")
            plugins.apply("mongez.compose")

            val localProperties = Properties()
            val localPropertiesFile = rootProject.file("local.properties")
            if (localPropertiesFile.exists()) {
                localProperties.load(FileInputStream(localPropertiesFile))
            }
            val googleWebClientId = localProperties.getProperty("GOOGLE_WEB_CLIENT_ID", "")
            val baseUrl = localProperties.getProperty("BASE_URL", "")
            val mapsApiKey = localProperties.getProperty("MAPS_API_KEY", "")

            extensions.configure<ApplicationExtension> {
                compileSdk = 37

                defaultConfig {
                    minSdk = 26
                    targetSdk = 37

                    buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$googleWebClientId\"")
                    buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
                    manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
                }

                buildFeatures {
                    buildConfig = true
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
