import com.devdd.recipe.buildsrc.Dependencies.Libraries
import com.devdd.recipe.buildsrc.Dependencies.Recipe

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

android {
    compileSdkVersion(Recipe.compileSdkVersion)

    defaultConfig {
        minSdkVersion(Recipe.minSdkVersion)

        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    // Material Design
    api(Libraries.Google.materialDesign)

    //Testing
    testImplementation(Libraries.Junit.jUnit)
    androidTestImplementation(Libraries.AndroidX.Testing.jUnitTest)
    androidTestImplementation(Libraries.AndroidX.Testing.espresso)

}
