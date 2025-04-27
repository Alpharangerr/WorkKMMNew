plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    id("org.jetbrains.kotlin.kapt") // <-- Added this for Room (kapt needed)
}

android {
    namespace = "com.example.workkmmnew.android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.workkmmnew.android"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0" // Specify the Compose Compiler version
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(projects.shared)

    // Compose dependencies
    implementation(libs.compose.ui) // If this is defined as a version, ensure it's updated to the latest.
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.compose.ui.tooling)

    // Room dependencies (NEW)
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Updated Compose dependencies for version 1.5.0 or higher
    implementation("androidx.compose.ui:ui:1.5.0")  // Update Compose UI to 1.5.0 or higher
    implementation("androidx.compose.material:material:1.5.0") // Update Material to 1.5.0 or higher
    implementation("androidx.compose.ui:ui-tooling:1.5.0") // For tooling support
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.0") // For preview
    implementation("androidx.navigation:navigation-compose:2.6.0")
}
