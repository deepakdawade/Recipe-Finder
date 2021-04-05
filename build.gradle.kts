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

tasks.register("clean",  Delete::class)  {
    delete(rootProject.buildDir)
}
