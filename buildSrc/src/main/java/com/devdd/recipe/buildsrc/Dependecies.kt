package com.devdd.recipe.buildsrc
object Dependencies {

    const val androidGradlePlugin = "com.android.tools.build:gradle:4.1.2"

    object BuildConfig {
        const val applicationId = "com.devdd.recipe"
        const val compileSdkVersion = 30
        const val minSdkVersion = 23
        const val targetSdk = 30
    }

    object VersionInfo {
        const val versionCode = 1
        const val versionName = "1.0"
        const val debugVersion = 1
    }

    object Libraries {

        private object Plugins {
            const val androidApplication = "com.android.application"
            const val android = "android"
            const val kapt = "kapt"
            const val androidExtention = "android.extensions"
            const val daggerHilt = "dagger.hilt.android.plugin"
            const val safeArgs = "androidx.navigation.safeargs.kotlin"
        }

        object Kotlin {
            private const val kotlinVersion = "1.4.10"
            const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
            const val kotlinGradlePlugin =
                "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
        }

        object AndroidX {
            const val appCompat = "androidx.appcompat:appcompat:1.2.0"
            const val coreKtx = "androidx.core:core-ktx:1.3.2"

            object SwipeRefreshLayout{
                const val version = "1.2.0-alpha01"
                const val swiperefreshlayout: String =
                    "androidx.swiperefreshlayout:swiperefreshlayout:$version"
            }

            object Browser{
                private const val version = "1.3.0"
                const val browser: String = "androidx.browser:browser:$version"
            }
            object FragmentKTX {
                private const val version = "1.3.0-beta02"
                const val fragmentKtx: String = "androidx.fragment:fragment-ktx:$version"
            }
            object ActivityKTX {
                private const val version = "1.2.2"
                const val activityKtx: String = "androidx.activity:activity-ktx:$version"
            }

            object ConstraintLayout {
                private const val version = "2.0.4"
                const val constraintLayout =
                    "androidx.constraintlayout:constraintlayout:$version"
            }

            object RecyclerView {
                private const val version = "1.1.0"
                const val recyclerView = "androidx.recyclerview:recyclerview:$version"
            }

            object Lifecycle{
                private const val version = "2.2.0"
                const val viewmodelKtx: String = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
                const val livedataKtx: String = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
                const val process: String = "androidx.lifecycle:lifecycle-process:$version"
            }

            object Navigation {
                private const val version = "2.2.2"
                const val navigationFragment =
                    "androidx.navigation:navigation-fragment-ktx:$version"
                const val navigationUI = "androidx.navigation:navigation-ui-ktx:$version"
                const val safeArgsGradlePlugin =
                    "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
            }

            object Room {
                private const val version = "2.3.0-beta03"
                const val room = "androidx.room:room-ktx:$version"
                const val roomRunTime = "androidx.room:room-runtime:$version"
                const val roomKapt = "androidx.room:room-compiler:$version"
            }

            object Paging3 {
                private const val version = "2.1.2"
                const val paging: String = "androidx.paging:paging-runtime-ktx:$version"
            }

            object Work {
                private const val version = "2.4.0"
                const val runtimeKtx: String = "androidx.work:work-runtime-ktx:$version"
            }

            object Testing {
                const val jUnitTest = "androidx.test.ext:junit:1.1.2"
                const val espresso = "androidx.test.espresso:espresso-core:3.3.0"
            }
        }

        object Google {
            private const val version = "1.3.0"
            private const val gSonVersion = "2.8.6"
            const val materialDesign = "com.google.android.material:material:$version"
            const val gson: String = "com.google.code.gson:gson:$gSonVersion"

            object DaggerHilt {
                private const val version = "2.33-beta"
                const val daggerHilt = "com.google.dagger:hilt-android:$version"
                const val daggerHiltViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
                const val hiltKapt = "com.google.dagger:hilt-compiler:$version"
                const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
            }


        }

        object Timber {
            private const val version = "4.7.1"
            const val timber: String = "com.jakewharton.timber:timber:$version"
        }


        object Retrofit {
            private const val version = "2.9.0"
            const val retrofit: String = "com.squareup.retrofit2:retrofit:$version"
            const val gsonConverter: String = "com.squareup.retrofit2:converter-gson:$version"
            const val kotlinConverter: String =
                "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2"
        }

        object OkHttp {
            private const val version = "4.9.0"
            const val okhttp: String = "com.squareup.okhttp3:okhttp:$version"
            const val loggingInterceptor: String =
                "com.squareup.okhttp3:logging-interceptor:$version"
            const val urlConnection: String = "com.squareup.okhttp3:okhttp-urlconnection:$version"
        }


        object Coroutines {
            private const val version = "1.4.3"
            const val coroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
            const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        }

        object Coil {
            private const val version = "1.1.0"
            const val coil: String = "io.coil-kt:coil:$version"
            const val svgCoil: String = "io.coil-kt:coil-svg:$version"
        }

        object Facebook {
            private const val version = "0.5.0"
            const val shimmerDrawable: String = "com.facebook.shimmer:shimmer:$version"
        }

        object AirBnb {
            private const val version = "3.6.0"
            const val lottie: String = "com.airbnb.android:lottie:$version"
        }

        object Junit{
            const val jUnit = "junit:junit:4.13.2"
        }
    }
}