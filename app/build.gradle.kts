plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.mywallet"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.mywallet"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}

dependencies {
    // Android Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.core.splashscreen)

    // Compose
    implementation(libs.androidx.activity.compose)
    //implementation(platform(libs.androidx.compose.bom))
    implementation(libs.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.foundation.layout)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.ui.text)
    implementation(libs.androidx.animation.core)

    // Navigation & Third-party Libraries
    implementation(libs.androidx.navigation.compose)
    implementation(libs.mpandroidchart)
    implementation(libs.coil.compose)
    implementation(libs.material)

    // Retrofit (Networking)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.3.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.android.gms:play-services-auth:20.6.0")

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.compose.ui.ui)
    implementation(libs.runtime)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.webkit)
    implementation(libs.foundation.layout)
    implementation(libs.ui.graphics)
    implementation(libs.ui.text)

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.compose.material3:material3:1.2.0-alpha03")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

}





