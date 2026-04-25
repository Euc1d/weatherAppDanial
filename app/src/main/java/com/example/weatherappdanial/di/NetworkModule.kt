package com.example.weatherappdanial.di

import com.example.weatherappdanial.data.remote.api.WeatherApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json{
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }
    @Provides
    @Singleton
    fun provideConverter(json: Json): Converter.Factory{
        return json.asConverterFactory(
            "application/json".toMediaType()
        )
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        converter: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")//на проде так не буду делать просто лень(
            .addConverterFactory(converter)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(
        retrofit: Retrofit
    ): WeatherApiService {
        return retrofit.create()
    }
}