package com.iti.mongez.org

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.util.Properties
import java.io.FileInputStream

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.apply("com.android.library")

            val localProperties = Properties()
            val localPropertiesFile = rootProject.file("local.properties")
            if (localPropertiesFile.exists()) {
                localProperties.load(FileInputStream(localPropertiesFile))
            }
            val googleWebClientId = localProperties.getProperty("GOOGLE_WEB_CLIENT_ID", "")
            val baseUrl = localProperties.getProperty("BASE_URL", "")

            extensions.configure<LibraryExtension> {
                compileSdk = 37

                defaultConfig {
                    minSdk = 26

                    buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$googleWebClientId\"")
                    buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
                }

                buildFeatures {
                    buildConfig = true
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
            }
        }
    }
}
