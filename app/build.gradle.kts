plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.jetbrains.kotlin.plugin.compose)
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("kotlin-android")
}

android {
    namespace = "com.bigbratan.emulair"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bigbratan.emulair"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        // buildConfigField("String", "GAME_COVERS_API_HOST", "\"https://internship.api.mocked.io/\"")

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.datastore.preferences)

    // Compose
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.navigation.compose)

    // Dependency Injection
    implementation(libs.androidx.work.runtime.ktx)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.google.dagger.hilt.compiler)
    implementation(libs.google.dagger.hilt.android)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Google + Kotlin
    implementation(platform(libs.jetbrains.kotlin.bom))
    implementation(libs.jetbrains.kotlin.reflect)
    implementation(libs.jetbrains.kotlinx.coroutines.play.services)
    implementation(libs.google.code.gson)

    // API
    implementation(libs.coil.kt.compose)
    implementation(libs.mready.apiclient)
    debugImplementation(libs.github.chuckerteam.chucker)
    releaseImplementation(libs.github.chuckerteam.chucker)

    // SQLite
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    kapt(libs.androidx.room.compiler)

    // Extras
    implementation(libs.google.accompanist.systemuicontroller)
    implementation(libs.google.accompanist.permissions)
    implementation(libs.google.accompanist.navigation.animation)
}