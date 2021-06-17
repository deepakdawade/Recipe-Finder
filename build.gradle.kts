// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(com.devdd.recipe.buildsrc.Dependencies.androidGradlePlugin)
        classpath(com.devdd.recipe.buildsrc.Dependencies.Libraries.Kotlin.kotlinGradlePlugin)
        classpath(com.devdd.recipe.buildsrc.Dependencies.Libraries.Google.DaggerHilt.hiltGradlePlugin)
        classpath(com.devdd.recipe.buildsrc.Dependencies.Libraries.AndroidX.Navigation.safeArgsGradlePlugin)

        //play service
        classpath(com.devdd.recipe.buildsrc.Dependencies.Libraries.Google.PlayServices.gradlePlugin)

//        classpath(com.devdd.recipe.buildsrc.Dependencies.Libraries.Google.Firebase.performancePlugin)
        classpath(com.devdd.recipe.buildsrc.Dependencies.Libraries.Google.Firebase.crashlyticsPlugin)

    // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

subprojects {

    tasks.withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
        //enable compilation in a separate daemon process
        options.isFork = true
    }

    tasks.withType<Test> {
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 }
            ?: 1
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            // Treat all Kotlin warnings as errors
            gradle.taskGraph.whenReady {
                //allWarningsAsErrors = hasTask(":app:assembleDebug").not()
                allWarningsAsErrors = false
            }

            // Enable experimental coroutine api
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xopt-in=kotlinx.coroutines.FlowPreview",
                "-Xopt-in=kotlin.Experimental",
                "-Xallow-jvm-ir-dependencies",
                "-Xjvm-default=all"
            )

            jvmTarget = "1.8"
        }
    }
}
tasks.register("clean",  Delete::class)  {
    delete(rootProject.buildDir)
}
