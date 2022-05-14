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
        buildConfig = false
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Libraries.AndroidX.Compose.version
    }

}

dependencies {
    implementation(Libraries.Kotlin.stdlib)
    implementation(Libraries.Kotlin.Coroutines.android)

//    coreLibraryDesugaring(Libraries.coreLibraryDesugar)
    implementation(Libraries.AndroidX.Activity.activityCompose)
    implementation(Libraries.AndroidX.Lifecycle.navigation)
    implementation(Libraries.AndroidX.appcompat)
    implementation(Libraries.AndroidX.Compose.runtime)
    implementation(Libraries.AndroidX.Compose.runtimeLivedata)
    implementation(Libraries.AndroidX.Compose.foundation)
    implementation(Libraries.AndroidX.Compose.material)
    implementation(Libraries.AndroidX.Compose.materialWindow)
    implementation(Libraries.AndroidX.Compose.layout)
    implementation(Libraries.AndroidX.Compose.animation)
    implementation(Libraries.AndroidX.Compose.toolingPreview)
    implementation(Libraries.AndroidX.Lifecycle.viewModelCompose)
    debugImplementation(Libraries.AndroidX.Compose.tooling)

    implementation(Libraries.AndroidX.Lifecycle.viewModelKtx)
    implementation(Libraries.Hilt.android)
    implementation(Libraries.Hilt.navigation)
    kapt(Libraries.Hilt.compiler)

    implementation(Libraries.Coil.coilCompose)

    debugImplementation(Libraries.AndroidX.Compose.uiTestManifest)

    androidTestImplementation(Libraries.JUnit.junit)
    androidTestImplementation(Libraries.AndroidX.Test.core)
    androidTestImplementation(Libraries.AndroidX.Test.runner)
    androidTestImplementation(Libraries.AndroidX.Test.espressoCore)
    androidTestImplementation(Libraries.AndroidX.Test.rules)
    androidTestImplementation(Libraries.AndroidX.Test.Ext.junit)
    androidTestImplementation(Libraries.Kotlin.Coroutines.test)
    androidTestImplementation(Libraries.AndroidX.Compose.uiTest)
    androidTestImplementation(Libraries.Hilt.android)
    androidTestImplementation(Libraries.Hilt.testing)
    kaptAndroidTest(Libraries.Hilt.compiler)
}
fun formatUrl(url: String): String = "\"$url\""
