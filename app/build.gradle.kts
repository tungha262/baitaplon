@file:Suppress("UNUSED_EXPRESSION")

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.baitaplon"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.baitaplon"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding =  true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.volley)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.google.gms:google-services:4.4.1")

    implementation ("com.squareup.picasso:picasso:2.71828")
    // loading buttom
    implementation("com.github.leandroborgesferreira:loading-button-android:2.3.0")

    //firebase
    implementation(libs.firebase.auth)
    implementation ("com.google.firebase:firebase-auth:21.0.6")


    //Firebase coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.1.1")


    // dapper hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    // Android ktx
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.7")


    // fragment
    implementation("androidx.fragment:fragment-ktx:1.7.1")

    // lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")

    val nav_version = "2.7.7"
    // Kotlin
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    //server
    implementation ("com.google.code.gson:gson:2.11.0")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")


}
kapt {
    correctErrorTypes = true
}

