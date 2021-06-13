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

        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    buildTypes {
        getByName("release") {
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = formatUrl(url = "https://receipe-bool.herokuapp.com/api/v1/")
            )
        }

        getByName("debug") {
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = formatUrl(url = "https://receipe-bool.herokuapp.com/api/v1/")
            )
        }
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

    // AndroidX
    implementation(Libraries.AndroidX.Paging3.paging)

    // Gson
    api(Libraries.Google.gson)

    // Dagger
    kapt(Libraries.Google.DaggerHilt.hiltKapt)

    //Room
    api(Libraries.AndroidX.Room.room)
    api(Libraries.AndroidX.Room.roomRunTime)
    kapt(Libraries.AndroidX.Room.roomKapt)

    //DataStore
    api(Libraries.AndroidX.DataStore.dataStore)

    //Firebase
    implementation(Libraries.Google.Firebase.messageKtx)

    // Ok-Http
    api(Libraries.OkHttp.okhttp)
    api(Libraries.OkHttp.loggingInterceptor)
    implementation(Libraries.OkHttp.urlConnection)

    // Retrofit Converters
    implementation(Libraries.Retrofit.gsonConverter)
    implementation(Libraries.Retrofit.kotlinConverter)

    //Testing
    testImplementation(Libraries.Junit.jUnit)
    androidTestImplementation(Libraries.AndroidX.Testing.jUnitTest)
    androidTestImplementation(Libraries.AndroidX.Testing.espresso)

}
fun formatUrl(url: String): String = "\"$url\""