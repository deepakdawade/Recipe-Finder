import com.devdd.recipe.buildsrc.Dependencies.Libraries

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}
val buildVersion = 1
android {
    compileSdk = (32)
    defaultConfig {
        applicationId = ("com.devdd.recipe")
        minSdk = (23)
        targetSdk = (32)
        versionCode = 1
        versionName = "1.0"

        vectorDrawables.useSupportLibrary = true
        resourceConfigurations.addAll(listOf("en", "hi"))
    }

    buildTypes {
        debug {
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = formatUrl(url = "https://receipe-bool.herokuapp.com/api/v1/")
            )
        }
        release {
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = formatUrl(url = "https://receipe-bool.herokuapp.com/api/v1/")
            )
        }
        getByName("debug") {
            versionNameSuffix = "-debug$buildVersion"
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
        compose = true
        // Disable unused AGP features
//        buildConfig = false
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Libraries.Compose.version
    }

}

dependencies {
    implementation(Libraries.Kotlin.stdlib)
    implementation(Libraries.AndroidX.appcompat)
    implementation(Libraries.Kotlin.Coroutines.core)
    implementation(Libraries.Kotlin.Coroutines.android)
    implementation(Libraries.AndroidX.DataStore.dataStore)

    // compose
    implementation(Libraries.Compose.activityCompose)
    implementation(Libraries.Compose.foundation)
    implementation(Libraries.Compose.animation)
    implementation(Libraries.Compose.runtime)
    implementation(Libraries.Compose.material)
    implementation(Libraries.Compose.ui)
    implementation(Libraries.Compose.uiTooling)
    implementation(Libraries.Compose.viewBinding)
    implementation(Libraries.Compose.materialThemeAdapter)
    implementation(Libraries.Compose.constraintLayout)
    implementation(Libraries.Compose.lottie)
    implementation(Libraries.Compose.graphics)
    implementation(Libraries.Compose.Accompanist.placeholder)
    implementation(Libraries.Compose.Accompanist.systemUiController)
    implementation(Libraries.Compose.Accompanist.pager)
    implementation(Libraries.Compose.snapper)
    implementation(Libraries.Compose.Accompanist.accompanistPagerIndiactor)
    implementation(Libraries.Compose.Accompanist.swipeToRefresh)
    implementation(Libraries.Compose.Accompanist.permission)
    implementation(Libraries.Compose.Accompanist.navigationAnimation)
    implementation(Libraries.Compose.Accompanist.drawablePainter)
    implementation(Libraries.Coil.compose)

    //GSON
    implementation(Libraries.gson)

    // Ok-Http
    api(Libraries.OkHttp.okhttp)
    api(Libraries.OkHttp.loggingInterceptor)
    implementation(Libraries.OkHttp.urlConnection)

    // Retrofit Converters
    implementation(Libraries.Retrofit.gsonConverter)
    implementation(Libraries.Retrofit.kotlinConverter)

    //Hilt
    implementation(Libraries.AndroidX.Lifecycle.viewModelKtx)
    implementation(Libraries.Hilt.android)
    implementation(Libraries.Hilt.navigation)
    kapt(Libraries.Hilt.compiler)

    //Room
    implementation(Libraries.AndroidX.Room.ktx)
    implementation(Libraries.AndroidX.Room.runtime)
    kapt(Libraries.AndroidX.Room.compiler)

    //Timber
    implementation(Libraries.Timber.timber)

    androidTestImplementation(Libraries.AndroidX.Test.core)
    androidTestImplementation(Libraries.AndroidX.Test.espressoCore)
}
fun formatUrl(url: String): String = "\"$url\""
