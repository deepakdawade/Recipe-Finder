import com.devdd.recipe.buildsrc.Dependencies.Libraries
import com.devdd.recipe.buildsrc.Dependencies.Recipe
import com.devdd.recipe.buildsrc.Dependencies.VersionInfo

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
    id("androidx.navigation.safeargs.kotlin")
}
kapt {
    correctErrorTypes = true
}

android {
    compileSdkVersion(Recipe.compileSdkVersion)
    buildToolsVersion("30.0.3")
    defaultConfig {
        applicationId(Recipe.applicationId)
        minSdkVersion(Recipe.minSdkVersion)
        targetSdkVersion(Recipe.targetSdk)
        versionCode = VersionInfo.versionCode
        versionName = VersionInfo.versionName

        vectorDrawables.useSupportLibrary = true
        resConfigs("en", "hi")
        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    buildTypes {
        getByName("debug") {
            versionNameSuffix = "-debug" + VersionInfo.debugVersion
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isShrinkResources = false

        }
        getByName("release") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        dataBinding = true
    }

    // Do not package unnecessary files
    packagingOptions {
        // Exclude licenses
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        // Exclude notices
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        // Exclude asi
        exclude("META-INF/ASL2.0")
        // Exclude AndroidX version files
        exclude("META-INF/*.version")
        // Exclude consumer proguard files
        exclude("META-INF/proguard/*")
        // Exclude the Firebase/Fabric/other random properties files
        exclude("/*.properties")
        exclude("META-INF/*.properties")
    }
}

dependencies {

    implementation(project(":ui"))

    // Dagger
    kapt(Libraries.Google.DaggerHilt.hiltKapt)


}
fun formatUrl(url: String): String = "\"$url\""