package com.devdd.recipe.data.retrofit.di

import com.devdd.recipe.base.utils.AppBuildConfig
import com.devdd.recipe.base.utils.extensions.debugElseRelease
import com.devdd.recipe.data.retrofit.service.RetrofitNetworkServiceApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.LoggingEventListener
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit.Builder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())

    @Provides
    @Singleton
    fun provideNetworkServiceApi(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient,
        appBuildConfig: AppBuildConfig
    ): RetrofitNetworkServiceApi = retrofitBuilder
        .client(okHttpClient)
        .baseUrl(appBuildConfig.BASE_URL)
        .build()
        .create(RetrofitNetworkServiceApi::class.java)


    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor?,
        loggingEventListener: LoggingEventListener.Factory?,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                if (httpLoggingInterceptor != null) {
                    addInterceptor(httpLoggingInterceptor)
                }
                if (loggingEventListener != null) {
                    eventListenerFactory(loggingEventListener)
                }
            }
            .connectionPool(ConnectionPool(10, 2, TimeUnit.MINUTES))
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(config: AppBuildConfig)
            : HttpLoggingInterceptor? = config.BUILD_TYPE.debugElseRelease(
        whenDebugging = {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }, onProduction = { null }
    )

    @Provides
    @Singleton
    fun provideHttpEventListener(config: AppBuildConfig)
            : LoggingEventListener.Factory? = config.BUILD_TYPE.debugElseRelease(
        whenDebugging = { LoggingEventListener.Factory() },
        onProduction = { null }
    )

}