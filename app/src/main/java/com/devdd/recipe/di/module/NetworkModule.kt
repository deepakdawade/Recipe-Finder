package com.devdd.recipe.di.module

import com.devdd.recipe.data.remote.retrofit.RetrofitNetworkServiceApi
import com.devdd.recipe.utils.AppBuildConfig
import com.devdd.recipe.utils.extensions.debugElseRelease
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.LoggingEventListener
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit.Builder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())

    @Provides
    @Singleton
    fun provideNetworkServiceApi(
        retrofitBuilder: Retrofit.Builder,
        appBuildConfig: AppBuildConfig
    ): RetrofitNetworkServiceApi = retrofitBuilder
        .baseUrl(appBuildConfig.BASE_URL + "/").build()
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
            .dispatcher(
                Dispatcher().apply {
                    maxRequestsPerHost = 25
                }
            )
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