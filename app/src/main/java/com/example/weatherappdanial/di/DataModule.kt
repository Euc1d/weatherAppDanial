package com.example.weatherappdanial.di

import android.content.Context
import androidx.room.Room
import com.example.weatherappdanial.data.local.WeatherDao
import com.example.weatherappdanial.data.local.WeatherDataBase
import com.example.weatherappdanial.data.remote.api.WeatherApiService
import com.example.weatherappdanial.data.repository.WeatherRepositoryImpl
import com.example.weatherappdanial.domain.repository.WeatherRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindWeatherRepository(repositoryImpl: WeatherRepositoryImpl): WeatherRepository

    companion object{

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): WeatherDataBase =
            Room.databaseBuilder(
                context,
                WeatherDataBase::class.java,
                "weather_db"
            ).fallbackToDestructiveMigration(true).build()

        @Provides
        fun provideWeatherDao(db: WeatherDataBase): WeatherDao = db.weatherDao()

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
                .baseUrl("https://api.openweathermap.org/")//на проде так не буду делать просто лень её нужно вывести и настраивать в билдконфинге(
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

}