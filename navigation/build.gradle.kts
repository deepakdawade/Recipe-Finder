import com.devdd.recipe.buildsrc.Dependencies.Libraries
import com.devdd.recipe.buildsrc.Dependencies.Recipe

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
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
        kotlinOptions {
            freeCompilerArgs = listOf("-module-name", "recipeNavigation")
        }

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        getByName("main").res.srcDirs("$rootDir/navigation/src/main/sharedRes")
    }
}

dependencies {
    // Local Projects
    implementation(project(":base"))

    // Navigation
    api(Libraries.AndroidX.Navigation.navigationFragment)
    implementation(Libraries.AndroidX.Navigation.navigationUI)

    //Testing
    testImplementation(Libraries.Junit.jUnit)
    androidTestImplementation(Libraries.AndroidX.Testing.jUnitTest)
    androidTestImplementation(Libraries.AndroidX.Testing.espresso)

}
