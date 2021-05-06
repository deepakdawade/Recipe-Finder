import com.devdd.recipe.buildsrc.Dependencies.Recipe
import com.devdd.recipe.buildsrc.Dependencies.Libraries

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

        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    buildTypes {
        getByName("release") {}

        getByName("debug") {}
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    // Local projects
    implementation(project(":base"))

    // AndroidX
    api(Libraries.AndroidX.coreKtx)
    implementation(Libraries.AndroidX.Paging3.paging)

    // Timber
    api(Libraries.Timber.timber)

    // Coil
    implementation(Libraries.Coil.coil)

    // Dagger
    api(Libraries.Google.DaggerHilt.daggerHilt)
    api(Libraries.Google.DaggerHilt.daggerHiltViewModel)
    kapt(Libraries.Google.DaggerHilt.hiltKapt)

    // Testing
    testImplementation(Libraries.Junit.jUnit)
    androidTestImplementation(Libraries.AndroidX.Testing.jUnitTest)
    androidTestImplementation(Libraries.AndroidX.Testing.espresso)
}
