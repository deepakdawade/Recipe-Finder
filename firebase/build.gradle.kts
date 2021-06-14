import com.devdd.recipe.buildsrc.Dependencies.Libraries
import com.devdd.recipe.buildsrc.Dependencies.Recipe

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

android {
    compileSdkVersion(Recipe.compileSdkVersion)

    defaultConfig {
        minSdkVersion(Recipe.minSdkVersion)

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
dependencies {

    // Local projects
    implementation(project(":ui"))

    // Testing
    testImplementation(Libraries.Junit.jUnit)
    androidTestImplementation(Libraries.AndroidX.Testing.jUnitTest)
    androidTestImplementation(Libraries.AndroidX.Testing.espresso)

    // Dagger
    implementation(Libraries.Google.DaggerHilt.daggerHilt)
    kapt(Libraries.Google.DaggerHilt.hiltKapt)

    // Firebase
    implementation(Libraries.Google.Firebase.messageKtx)
}

fun formatKey(key: String): String = "\"$key\""