import io.gitlab.arturbosch.detekt.Detekt
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.detekt)
    id("kotlin-parcelize")
    id("jacoco")
}

android {
    namespace = "com.picpay.desafio.android"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.picpay.desafio.android"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties().apply {
            val file = rootProject.file("local.properties")
            if (file.exists()) load(file.inputStream())
        }

        val serverClientId = localProperties.getProperty("SERVER_CLIENT_ID") ?: ""
        buildConfigField(
            "String",
            "SERVER_CLIENT_ID",
            "\"$serverClientId\""
        )
    }

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "PICPAY_SERVICE_BASE_URL",
                "\"https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/\""
            )

            buildConfigField(
                "String",
                "PERSON_SERVICE_BASE_URL",
                "\"https://randomuser.me/\""
            )

            buildConfigField(
                "String",
                "PHOTOS_SERVICE_BASE_URL",
                "\"https://picsum.photos/\""
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

            buildConfigField(
                "String",
                "PERSON_SERVICE_BASE_URL",
                "\"https://randomuser.me/api/\""
            )

            buildConfigField(
                "String",
                "PHOTOS_SERVICE_BASE_URL",
                "\"https://picsum.photos/v2/list/\""
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
}

jacoco {
    toolVersion = "0.8.13"
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    group = "verification"
    description = "Generate Jacoco coverage report"

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)

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

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    ignoreFailures = false
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(false)
        sarif.required.set(false)
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
    androidTestImplementation(libs.androidx.core)
    androidTestImplementation(libs.androidx.junit.v115)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.arch.core.testing)

    // Mockk
    testImplementation(libs.mockk)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockito.core)
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockito.core)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.serialization.converter)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Serialization
    implementation(libs.serialization.json)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit)

    // Coil
    implementation(libs.coil.compose)

    // Room
    implementation(libs.room)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    testImplementation(libs.room.test)

    // Firebase
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.googleid.v110)

    //Splash Screen
    implementation(libs.androidx.core.splashscreen)
    testImplementation(kotlin("test"))
}
