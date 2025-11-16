plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
}

android {
    namespace = "com.picpay.desafio.android"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.picpay.desafio.android"
        minSdk = 21
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "PICPAY_SERVICE_BASE_URL",
                "\"https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/\""
            )
        }
        release {
            buildConfigField(
                "String",
                "PICPAY_SERVICE_BASE_URL",
                "\"https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/\""
            )
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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    // Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    //Mockk
    testImplementation(libs.mockk)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.serialization.converter)

    //OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    //Serialization
    implementation(libs.serialization.json)

    //Navigation
    implementation(libs.androidx.navigation.compose)

    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    testImplementation(libs.koin.test)

    // Coil
    implementation(libs.coil.compose)

    //Room
    implementation(libs.room)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    testImplementation(libs.room.test)
}
