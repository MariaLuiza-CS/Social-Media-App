plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
    id("jacoco")
}

jacoco {
    toolVersion = "0.8.13"
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
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    testOptions {
        unitTests.all {
            // "jacoco.includeNoLocationClasses" não existe mais → remover
        }
    }
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    group = "verification"
    description = "Generate Jacoco coverage report"

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)

        // 👇 garante que o HTML vai para uma pasta previsível
        html.outputLocation.set(
            layout.buildDirectory.dir("reports/jacoco/jacocoTestReport/html")
        )
        xml.outputLocation.set(
            layout.buildDirectory.file("reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
        )
    }

    val fileFilter = listOf(
        "**/R.class",
        "**/R\$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*"
    )

    val debugTree = fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
        exclude(fileFilter)
    }

    classDirectories.setFrom(debugTree)
    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))

    val jacocoDir = layout.buildDirectory.dir("jacoco")
    val unitTestCoverageDir = layout.buildDirectory.dir("outputs/unit_test_code_coverage/debugUnitTest")

    executionData.setFrom(
        files(
            fileTree(jacocoDir) {
                include("**/*.exec")
            },
            fileTree(unitTestCoverageDir) {
                include("**/*.exec")
            }
        )
    )

    doFirst {
        println(">>> buildDir = ${layout.buildDirectory.get().asFile.absolutePath}")
        println(">>> debug classes = ${debugTree.files}")
        println(">>> jacoco dir files: ${fileTree(jacocoDir).files}")
        println(">>> unit test coverage dir files: ${fileTree(unitTestCoverageDir).files}")
        println(">>> html report will be at: ${reports.html.outputLocation.get().asFile.absolutePath}")
        println(">>> xml report will be at: ${reports.xml.outputLocation.get().asFile.absolutePath}")
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
