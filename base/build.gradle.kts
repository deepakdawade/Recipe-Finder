import com.devdd.recipe.buildsrc.Dependencies.Libraries

plugins {
    id("kotlin")
    kotlin("kapt")
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {

    // Kotlin
    api(Libraries.Kotlin.kotlinStdLib)

    // Co-Routines
    api(Libraries.Coroutines.coroutineCore)

    // Dagger
    api(Libraries.Google.DaggerHilt.daggerHilt)
    kapt(Libraries.Google.DaggerHilt.hiltKapt)

    // Retrofit
    api(Libraries.Retrofit.retrofit)
}
