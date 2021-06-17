package com.devdd.recipe.di.module

import com.devdd.recipe.base.utils.Logger
import com.devdd.recipe.base_android.utils.logger.RecipeLogger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseRemoteConfig(): FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseCrashlytics(): FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    @Singleton
    @Provides
    fun provideLogger(firebaseCrashlytics: FirebaseCrashlytics): Logger =
        RecipeLogger(firebaseCrashlytics)

/*
    @Singleton
    @Provides
    fun provideFirebaseFireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics =
        FirebaseAnalytics.getInstance(context)

    @Singleton
    @Provides
    fun provideFirebaseStorageReference(): StorageReference =
        FirebaseStorage.getInstance().reference

    @Singleton
    @Provides
    fun provideTopicSubscriber(firebaseMessaging: FirebaseMessaging): TopicSubscriber =
        FcmTopicSubscriber(firebaseMessaging)
*/

}