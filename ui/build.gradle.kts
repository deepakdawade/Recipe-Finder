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

        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    // Local projects
    api(project(":base"))
    api(project(":base-android"))
    api(project(":data"))
    api(project(":domain"))
    api(project(":navigation"))

    // Don't import this project anywhere else
//    implementation(project(":theme"))

    // AndroidX
    api(Libraries.AndroidX.appCompat)
    api(Libraries.AndroidX.coreKtx)
    api(Libraries.AndroidX.SwipeRefreshLayout.swiperefreshlayout)
    api(Libraries.AndroidX.ConstraintLayout.constraintLayout)
    api(Libraries.AndroidX.Paging3.paging)


    // Material Design
    api(Libraries.Google.materialDesign)

    // Timber
    api(Libraries.Timber.timber)

    // Coil
    implementation(Libraries.Coil.coil)
    implementation(Libraries.Coil.svgCoil)

    // Lottie
    api(Libraries.AirBnb.lottie)

    // Lifecycle
    api(Libraries.AndroidX.Lifecycle.viewmodelKtx)
    api(Libraries.AndroidX.Lifecycle.livedataKtx)

    // Shimmer Drawable
    implementation(Libraries.Facebook.shimmerDrawable)

    // Dagger
    api(Libraries.Google.DaggerHilt.daggerHilt)
    api(Libraries.Google.DaggerHilt.daggerHiltViewModel)
    kapt(Libraries.Google.DaggerHilt.hiltKapt)

    //Testing
    testImplementation(Libraries.Junit.jUnit)
    androidTestImplementation(Libraries.AndroidX.Testing.jUnitTest)
    androidTestImplementation(Libraries.AndroidX.Testing.espresso)

}
