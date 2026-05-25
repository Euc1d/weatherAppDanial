import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinx.serialization)
    id("com.google.devtools.ksp")
}
val localProps = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}
android {
    namespace = "com.example.weatherappdanial"
    compileSdk {
        version = release(36)
    }
    defaultConfig {
        applicationId = "com.example.weatherappdanial"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        buildConfigField(
            "String",
            "WEATHER_API_KEY",
            "\"${localProps["WEATHER_API_KEY"]}\""
        )
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("release") {
            storeFile     = file(localProps["KEYSTORE_PATH"] as String)
            storePassword = localProps["KEYSTORE_PASSWORD"] as String
            keyAlias      = localProps["KEY_ALIAS"]         as String
            keyPassword   = localProps["KEY_PASSWORD"]      as String
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
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }



    flavorDimensions += "version"
    productFlavors{
        create("qa"){
            dimension = "version"
            versionNameSuffix = "-qa"
            applicationIdSuffix = ".qa"
        }
        create("dev"){
            dimension = "version"
            versionNameSuffix = "-dev"
            applicationIdSuffix = ".dev"
        }
        create("prod"){
            dimension = "version"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)


    implementation(libs.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.compose.animation)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.navigation.compose)


    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    implementation(libs.play.services.location)

    implementation(libs.converter.gson)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)



    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}