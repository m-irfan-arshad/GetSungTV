plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.andorid.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.getsung.tv"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.getsung.tv"
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.android.core)
    implementation(libs.bundles.activity)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.compose.core)
    implementation(libs.bundles.compose.material)
    implementation(libs.bundles.compose.tooling)
    implementation(libs.bundles.tv.compose)
    implementation(libs.bundles.networking)
    implementation(libs.bundles.compose.adaptive)
    implementation(libs.bundles.hilt)
    ksp(libs.bundles.hilt.ksp)

    implementation(libs.coil.compose)
    implementation(libs.bundles.coroutines)
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.datastore.preferences)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.compose.ui.tooling)
//    ksp(libs.androidx.room.compiler)
}