package com.devdd.recipe.buildsrc

object Versions {
    const val ktLint = "0.45.2"
}

object Dependencies {

    object Libraries {
        const val androidGradlePlugin = "com.android.tools.build:gradle:7.2.0"
        const val ktLint = "com.pinterest:ktlint:${Versions.ktLint}"

        const val coreLibraryDesugar = "com.android.tools:desugar_jdk_libs:1.1.5"

        const val exoplayer = "com.google.android.exoplayer:exoplayer:2.11.8"
        const val gson = "com.google.code.gson:gson:2.8.6"

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

        object Kotlin {
            private const val version = "1.6.21"
            const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
            const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
            const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"

            object Coroutines {
                private const val version = "1.6.0"
                const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
                const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
                const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
            }
        }

        object Timber {
            private const val version = "4.7.1"
            const val timber: String = "com.jakewharton.timber:timber:$version"
        }

        object Compose {
            const val snapshot = ""
            const val version = "1.2.0-beta01"
            const val activityCompose = "androidx.activity:activity-compose:1.5.0-rc01"
            const val foundation = "androidx.compose.foundation:foundation:${version}"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"
            const val ui = "androidx.compose.ui:ui:${version}"
            const val uiTooling = "androidx.compose.ui:ui-tooling:${version}"
            const val toolingPreview = "androidx.compose.ui:ui-tooling-preview:$version"
            const val runtime = "androidx.compose.runtime:runtime:${version}"
            const val runtimeLivedata = "androidx.compose.runtime:runtime-livedata:$version"
            const val material = "androidx.compose.material:material:${version}"
            const val materialThemeAdapter =
                "com.google.android.material:compose-theme-adapter:1.1.5"
            const val materialWindow =
                "androidx.compose.material3:material3-window-size-class:1.0.0-alpha10"
            const val animation = "androidx.compose.animation:animation:${version}"
            const val viewBinding = "androidx.compose.ui:ui-viewbinding:$version"
            const val graphics = "androidx.compose.animation:animation-graphics:$version"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.0"
            const val lottie = "com.airbnb.android:lottie-compose:5.0.3"

            const val uiTest = "androidx.compose.ui:ui-test-junit4:$version"
            const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:$version"

            object Accompanist {
                private const val version = "0.24.8-beta"
                const val placeholder =
                    "com.google.accompanist:accompanist-placeholder-material:$version"
                const val insets = "com.google.accompanist:accompanist-insets:$version"
                const val insetsUi = "com.google.accompanist:accompanist-insets-ui:$version"
                const val systemUiController =
                    "com.google.accompanist:accompanist-systemuicontroller:$version"
                const val pager = "com.google.accompanist:accompanist-pager:$version"
                const val accompanistPagerIndiactor =
                    "com.google.accompanist:accompanist-pager-indicators:$version"
                const val swipeToRefresh =
                    "com.google.accompanist:accompanist-swiperefresh:$version"
                const val permission = "com.google.accompanist:accompanist-permissions:$version"
                const val navigationAnimation =
                    "com.google.accompanist:accompanist-navigation-animation:$version"
                const val drawablePainter =
                    "com.google.accompanist:accompanist-drawablepainter:$version"
            }

            const val snapper = "dev.chrisbanes.snapper:snapper:0.2.0"

            object ComposeDestination {
                private const val version = "1.3.1-beta"
                const val destination =
                    "io.github.raamcosta.compose-destinations:animations-core:$version"
                const val destinationKsp = "io.github.raamcosta.compose-destinations:ksp:$version"
            }
            const val flinger = "com.github.iamjosephmj:Flinger:1.1.1"
        }

        object AndroidX {
            object Activity {
                const val coreKtx = "androidx.core:core-ktx:1.8.0-rc01"
                const val collectionKtx = "androidx.collection:collection-ktx:1.2.0"
                const val splash = "androidx.core:core-splashscreen:1.0.0-beta02"
            }

            object Room {
                private const val version = "2.2.6"
                const val compiler = "androidx.room:room-compiler:$version"
                const val ktx = "androidx.room:room-ktx:$version"
                const val runtime = "androidx.room:room-runtime:$version"
                const val ROOM_TESTING = "androidx.room:room-testing:$version"
            }

            object DataStore {
                private const val version = "1.0.0-alpha08"
                const val dataStore = "androidx.datastore:datastore-preferences:$version"
            }

            const val appcompat = "androidx.appcompat:appcompat:1.4.1"

            object Lifecycle {
                private const val version = "2.5.0-rc01"
                const val viewModelCompose =
                    "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
                const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
                const val livedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
                const val common = "androidx.lifecycle:lifecycle-common-java8:$version"
                const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            }

            object Test {
                private const val version = "1.4.0"
                const val core = "androidx.test:core:$version"
                const val runner = "androidx.test:runner:$version"
                const val rules = "androidx.test:rules:$version"

                object Ext {
                    private const val version = "1.1.2"
                    const val junit = "androidx.test.ext:junit-ktx:$version"
                }

                const val espressoCore = "androidx.test.espresso:espresso-core:3.2.0"
                const val uiAutomator = "androidx.test.uiautomator:uiautomator:2.2.0"
            }
        }

        object Hilt {
            private const val version = "2.42"

            const val gradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
            const val android = "com.google.dagger:hilt-android:$version"
            const val compiler = "com.google.dagger:hilt-compiler:$version"
            const val testing = "com.google.dagger:hilt-android-testing:$version"
            const val navigation = "androidx.hilt:hilt-navigation-compose:1.0.0"
        }

        object JUnit {
            private const val version = "4.13"
            const val junit = "junit:junit:$version"
        }


        object Coil {
            private const val version = "2.0.0"
            const val coil = "io.coil-kt:coil:$version"
            const val compose = "io.coil-kt:coil-compose:$version"
            const val svg = "io.coil-kt:coil-svg:$version"
        }
    }
}

object Urls {
    const val mavenCentralSnapshotRepo = "https://oss.sonatype.org/content/repositories/snapshots/"
    const val composeSnapshotRepo = "https://androidx.dev/snapshots/builds/" +
            "${Dependencies.Libraries.Compose.snapshot}/artifacts/repository/"
}