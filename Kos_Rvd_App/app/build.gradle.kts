import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    // kotlin serialization
    alias(libs.plugins.jetbrains.kotlin.serialization)
    // ksp
    alias(libs.plugins.google.devtools.ksp)
    // dagger hilt
    alias(libs.plugins.hilt.android)
    // google services plugin
    alias(libs.plugins.google.gms.google.services)
}

val localProperties = Properties()
val localPropertiesFile: File = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

android {
    signingConfigs {
        create("release") {
            storeFile =
                file("${localProperties.getProperty("STORE_FILE", "")}")
            storePassword = "${localProperties.getProperty("STORE_PASSWORD", "")}"
            keyAlias = "${localProperties.getProperty("KEY_ALIAS", "")}"
            keyPassword = "${localProperties.getProperty("KEY_PASSWORD", "")}"
        }
    }
    namespace = "com.kosrvd.app"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.kosrvd.app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val projectId = localProperties.getProperty("FIREBASE_PROJECT_ID")
            val fullUrlRelease = "\"https://$projectId.web.app/api/\""
            val bucketName = localProperties.getProperty("BUCKET_NAME","")

            buildConfigField("String", "IP_LAPTOP", "\"\"")
            buildConfigField("String", "BASE_URL", fullUrlRelease)
            buildConfigField("Boolean", "DEBUG", "false")
            buildConfigField("String", "BUCKET_NAME", "\"$bucketName\"")
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
            isJniDebuggable = false
            versionNameSuffix = ".release"
        }
        debug {
            versionNameSuffix = ".debug"
            applicationIdSuffix =  ".debug"
            isMinifyEnabled = false
            val ip = localProperties.getProperty("IP_LAPTOP", "")
            val projectId = localProperties.getProperty("FIREBASE_PROJECT_ID", "")
            val region = localProperties.getProperty("FIREBASE_REGION", "")
            val fullUrlDebug = "\"http://$ip:5001/$projectId/$region/\""
            val bucketName = localProperties.getProperty("BUCKET_NAME","")

            buildConfigField("String", "IP_LAPTOP", "\"$ip\"")
            buildConfigField("String", "BASE_URL", fullUrlDebug)
            buildConfigField("Boolean", "DEBUG", "true")
            buildConfigField("String", "BUCKET_NAME", "\"$bucketName\"")
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
            isJniDebuggable = true
            isShrinkResources = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11

        isCoreLibraryDesugaringEnabled = true

    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

composeCompiler {
    includeComposeMappingFile.set(false)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}

dependencies {
    // kotlin & lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.exifinterface)
    implementation(libs.androidx.ui.graphics)

    // unit test & ui test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // Jetpack compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.androidx.constraintlayout.compose)

    // Androidx window
    implementation(libs.androidx.window)
    implementation(libs.androidx.window.core)

    // dns okhttp
    implementation(libs.okhttp.dnsoverhttps)

    // ktor
    implementation(libs.bundles.ktor)


    // material 3
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)

    // desugaring
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // splashscreen
    implementation(libs.androidx.core.splashscreen)

    // hilt navigation
    implementation(libs.androidx.hilt.navigation.compose)

    // JSON serialization library, works with the Kotlin serialization plugin
    implementation(libs.kotlinx.serialization.json)

    // kotlin coroutine
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // viewmodel compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // dagger hilt and ksp dagger hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)


    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))


    // When using the BoM, don't specify versions in Firebase dependencies
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.appcheck.playintegrity)
    implementation(libs.firebase.appcheck.debug)
    implementation(libs.firebase.functions)


    // Data Store
    implementation(libs.androidx.datastore.preferences)
}