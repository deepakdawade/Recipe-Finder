import com.devdd.recipe.buildsrc.Dependencies.BuildConfig
import com.devdd.recipe.buildsrc.Dependencies.VersionInfo
import com.devdd.recipe.buildsrc.Dependencies.Libraries

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}
kapt {
    correctErrorTypes = true
}

android {
    compileSdkVersion(BuildConfig.compileSdkVersion)
    buildToolsVersion("30.0.3")
    defaultConfig {
        applicationId(BuildConfig.applicationId)
        minSdkVersion(BuildConfig.minSdkVersion)
        targetSdkVersion(BuildConfig.targetSdk)
        versionCode = VersionInfo.versionCode
        versionName = VersionInfo.versionName

        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    buildTypes {
        getByName("debug") {
            versionNameSuffix = "-debug" + VersionInfo.debugVersion
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isShrinkResources = false
            buildConfigField(
                    type = "String",
                    name = "BASE_URL",
                    value = formatUrl(url = "https://receipe-bool.herokuapp.com/api/v1/")
            )

        }
        getByName("release") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )

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

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        dataBinding = true
    }

    // Do not package unnecessary files
    packagingOptions {
        // Exclude licenses
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        // Exclude notices
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        // Exclude asi
        exclude("META-INF/ASL2.0")
        // Exclude AndroidX version files
        exclude("META-INF/*.version")
        // Exclude consumer proguard files
        exclude("META-INF/proguard/*")
        // Exclude the Firebase/Fabric/other random properties files
        exclude("/*.properties")
        exclude("META-INF/*.properties")
    }
}

dependencies {

    implementation(Libraries.Kotlin.kotlinStdLib)
    implementation(Libraries.AndroidX.appCompat)
    implementation(Libraries.Google.materialDesign)
    implementation(Libraries.AndroidX.ConstraintLayout.constraintLayout)
    implementation(Libraries.AndroidX.SwipeRefreshLayout.swiperefreshlayout)

    // Navigation
    implementation(Libraries.AndroidX.Navigation.navigationFragment)
    implementation(Libraries.AndroidX.Navigation.navigationUI)

    // Co-Routines
    implementation(Libraries.Coroutines.coroutineAndroid)
    implementation(Libraries.Coroutines.coroutineCore)

    //Lottie
    implementation(Libraries.AirBnb.lottie)

    // Dagger
    implementation(Libraries.Google.DaggerHilt.daggerHilt)
    implementation(Libraries.Google.DaggerHilt.daggerHiltViewModel)
    kapt(Libraries.Google.DaggerHilt.hiltKapt)

    // Retrofit
    implementation(Libraries.Retrofit.retrofit)

    // Retrofit Converters
    implementation(Libraries.Retrofit.gsonConverter)
    implementation(Libraries.Retrofit.kotlinConverter)

    // Gson
    implementation(Libraries.Google.gson)

    // Ok-Http
    implementation(Libraries.OkHttp.okhttp)
    implementation(Libraries.OkHttp.loggingInterceptor)
    implementation(Libraries.OkHttp.urlConnection)

    // Timber
    implementation(Libraries.Timber.timber)

    //Room
    implementation(Libraries.AndroidX.Room.room)
    implementation(Libraries.AndroidX.Room.roomRunTime)
    kapt(Libraries.AndroidX.Room.roomKapt)

    // LiveData
    implementation(Libraries.AndroidX.Lifecycle.livedataKtx)

    // viewModel
    implementation(Libraries.AndroidX.Lifecycle.viewmodelKtx)

    // Fragment
    implementation(Libraries.AndroidX.FragmentKTX.fragmentKtx)

    // Coil
    implementation(Libraries.Coil.coil)
    implementation(Libraries.Coil.svgCoil)

    //Testing
    testImplementation(Libraries.Junit.jUnit)
    androidTestImplementation(Libraries.AndroidX.Testing.jUnitTest)
    androidTestImplementation(Libraries.AndroidX.Testing.espresso)
}
fun formatUrl(url: String): String = "\"$url\""