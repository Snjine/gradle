// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    kotlin("android") version "1.9.22" apply false
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "8.2.2" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

// Custom tasks for building APKs
tasks.register("buildDebugApk") {
    group = "build"
    description = "Builds the debug APK"
    dependsOn(":app:assembleDebug")
}

tasks.register("buildReleaseApk") {
    group = "build"
    description = "Builds the release APK (Unsigned)"
    dependsOn(":app:assembleRelease")
}

tasks.register("signReleaseApk") {
    group = "build"
    description = "Signs the release APK"
    doLast {
        val keystoreFile = file("my-release-key.jks")
        val apkPath = file("app/build/outputs/apk/release/app-release.apk")
        val signedApkPath = file("app/build/outputs/apk/release/app-release-signed.apk")
        
        if (!keystoreFile.exists()) {
            throw GradleException("Keystore file not found: ${keystoreFile.absolutePath}")
        }
        
        exec {
            commandLine(
                "jarsigner", "-verbose", "-sigalg", "SHA1withRSA", "-digestalg", "SHA1",
                "-keystore", keystoreFile.absolutePath,
                apkPath.absolutePath, "my-key-alias"
            )
        }
        
        println("APK signed successfully!")

        // Zipalign the APK (optional but recommended)
        exec {
            commandLine("zipalign", "-v", "4", apkPath.absolutePath, signedApkPath.absolutePath)
        }

        println("APK aligned successfully: ${signedApkPath.absolutePath}")
    }
}

tasks.register("installDebugApk") {
    group = "install"
    description = "Installs the debug APK on a connected device"
    dependsOn("buildDebugApk")
    doLast {
        exec {
            commandLine("adb", "install", "app/build/outputs/apk/debug/app-debug.apk")
        }
    }
}

tasks.register("installReleaseApk") {
    group = "install"
    description = "Installs the signed release APK on a connected device"
    dependsOn("signReleaseApk")
    doLast {
        exec {
            commandLine("adb", "install", "app/build/outputs/apk/release/app-release-signed.apk")
        }
    }
}