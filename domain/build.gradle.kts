import com.devdd.recipe.buildsrc.Dependencies.Recipe
import com.devdd.recipe.buildsrc.Dependencies.Libraries

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

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    // Local projects
    implementation(project(":base"))
    implementation(project(":base-android"))
    implementation(project(":data"))

    // AndroidX
    implementation(Libraries.AndroidX.Paging3.paging)


    // Dagger
    kapt(Libraries.Google.DaggerHilt.hiltKapt)

    //Testing
    testImplementation(Libraries.Junit.jUnit)
    androidTestImplementation(Libraries.AndroidX.Testing.jUnitTest)
    androidTestImplementation(Libraries.AndroidX.Testing.espresso)

}
