plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.googleServices)
}

android {
    namespace = "com.itcc.avasarpay"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.itcc.avasarpay"
        minSdk = 24
        targetSdk = 35
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
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.hilt.android)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.googleid)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.lifecycle.viewmodal.ktx)
    implementation(libs.lifecycle.runtime.ktx)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.logging.intercepter)

    implementation(libs.sdp.android)
    implementation(libs.ssp.android)

    implementation(libs.glide)

    implementation(platform(libs.firebase))
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.auth.ktx)

    implementation(libs.play.services.auth)
    implementation(libs.credentials.play.services.auth)

    implementation(libs.circle.imagview)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}