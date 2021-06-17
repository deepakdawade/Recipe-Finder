package com.devdd.recipe.ui.utils

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.util.CoilUtils
import com.devdd.recipe.base_android.initializer.AppInitializer
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CoilInitializer @Inject constructor(private val okHttpClient: OkHttpClient) : AppInitializer {
    override fun init(application: Application) {
        val coilOkHttpClient = okHttpClient.newBuilder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .cacheControl(
                        CacheControl.Builder()
                            .maxStale(365, TimeUnit.DAYS)
                            .build()
                    )
                    .build()
                chain.proceed(request)
            }
            .cache(CoilUtils.createDefaultCache(application))
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

        Coil.setImageLoader {
            ImageLoader.Builder(application)
                .componentRegistry {
                    add(SvgDecoder(application))
                }
                .okHttpClient(coilOkHttpClient)
                .build()
        }
    }
}