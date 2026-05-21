package com.example.weatherappdanial.di

import android.content.Context
import androidx.room.Room
import com.example.weatherappdanial.data.local.entity.weather.WeatherDao
import com.example.weatherappdanial.data.local.WeatherDataBase
import com.example.weatherappdanial.data.local.entity.city.CityDao
import com.example.weatherappdanial.data.remote.api.NominatimApiService
import com.example.weatherappdanial.data.remote.api.WeatherApiService
import com.example.weatherappdanial.data.repository.CityRepositoryImpl
import com.example.weatherappdanial.data.repository.UserPrefsRepositoryImpl
import com.example.weatherappdanial.data.repository.WeatherRepositoryImpl
import com.example.weatherappdanial.domain.pref.UserPrefsRepository
import com.example.weatherappdanial.domain.repository.CityRepository
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
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindWeatherRepository(repositoryImpl: WeatherRepositoryImpl): WeatherRepository

    @Singleton
    @Binds
    fun bindCityRepository(impl: CityRepositoryImpl): CityRepository
    @Singleton @Binds
    fun bindUserPrefsRepository(impl: UserPrefsRepositoryImpl): UserPrefsRepository


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
        fun provideCityDao(db: WeatherDataBase): CityDao = db.cityDao()

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

        @Provides @Singleton @Named("owm")
        fun provideWeatherRetrofit(converter: Converter.Factory): Retrofit =
            Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(converter)
                .build()

        @Provides @Singleton @Named("nominatim")
        fun provideNominatimRetrofit(converter: Converter.Factory): Retrofit {
            val client = OkHttpClient.Builder()
                .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .header("User-Agent", "WeatherAppDanial/1.0 (Android; contact: kdanial111@mail.ru)")
                        .header("Accept", "application/json")
                        .header("Accept-Language", "ru,en;q=0.9")
                        .build()
                    chain.proceed(request)
                }
                .build()

            return Retrofit.Builder()
                .baseUrl("https://nominatim.openstreetmap.org/")
                .client(client)
                .addConverterFactory(converter)
                .build()
        }
        @Provides @Singleton
        fun provideWeatherApi(@Named("owm") retrofit: Retrofit): WeatherApiService = retrofit.create()

        @Provides @Singleton
        fun provideNominatimApi(@Named("nominatim") retrofit: Retrofit): NominatimApiService = retrofit.create()


    }

}