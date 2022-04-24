package com.example.rickandmortyapp.di

import android.content.Context
import com.example.rickandmortyapp.constants.Constants.BASE_URL
import com.example.rickandmortyapp.constants.Constants.CHARACTER_SERVICE
import com.example.rickandmortyapp.network.service.CharacterService
import com.example.rickandmortyapp.util.LiveDataCallAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Module for all remote api endpoints
 */
@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpCache(context: Context): Cache =
        Cache(context.cacheDir, (10 * 1024 * 1024).toLong())

    @Singleton
    @Provides
    @Named("header")
    fun provideHeaderInterceptor(): Interceptor =
        Interceptor { chain ->
            val request = chain.request()

            val newUrl = request.url.newBuilder()
                .build()
            val newRequest = request.newBuilder()
                .url(newUrl)
                .method(request.method, request.body)
                .build()
            chain.proceed(newRequest)
        }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        interceptor: Interceptor,
        @Named("header") header: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(2L, TimeUnit.SECONDS)
            .readTimeout(2L, TimeUnit.SECONDS)
            .writeTimeout(2L, TimeUnit.SECONDS)
            .addInterceptor(interceptor)

            .apply {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(loggingInterceptor)
            }
            .build()


    @Singleton
    @Provides
    @Named(CHARACTER_SERVICE)
    fun provideCharacterInterceptor(): Interceptor {
        return Interceptor { chain ->
            synchronized(this) {
                val request = chain.request()
                    .newBuilder()
                    .build()

                val response = chain.proceed(request)
                response
            }
        }
    }

    @Singleton
    @Provides
    @Named(CHARACTER_SERVICE)
    fun provideUserHttpClient(
        @Named(CHARACTER_SERVICE) interceptor: Interceptor,
        @Named("header") header: Interceptor,
    ): OkHttpClient {
        return provideOkHttpClient(interceptor, header)
    }

    @Singleton
    @Provides
    fun provideCharacterService(
        @Named(CHARACTER_SERVICE)
        okHttpClient: OkHttpClient,
        moshi: Moshi,
    ): CharacterService {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(okHttpClient)
            .build()
            .create(CharacterService::class.java)
    }
}
