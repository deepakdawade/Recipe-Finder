package com.devdd.recipe.data.preference.di

import android.content.Context
import android.content.SharedPreferences
import com.devdd.recipe.base.constants.SHARED_PREF_NAME
import com.devdd.recipe.data.preference.DataStorePreference
import com.devdd.recipe.data.preference.DataStorePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule{

    @Singleton
    @Provides
    fun providesRecipeDataStore(dataStore: DataStorePreferences): DataStorePreference = dataStore

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

}