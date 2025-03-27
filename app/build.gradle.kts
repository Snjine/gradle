plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 34  // Set to your desired API level

    defaultConfig {
        applicationId = "com.example.myapp"
        minSdk = 21  // Minimum supported Android version
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.create("release") {
                storeFile = file("my-release-key.jks")
                storePassword = "your-password"
                keyAlias = "my-key-alias"
                keyPassword = "your-key-password"
            }
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
