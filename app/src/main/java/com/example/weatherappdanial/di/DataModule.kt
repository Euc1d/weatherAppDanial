package com.example.weatherappdanial.di

import androidx.room.Room
import com.example.weatherappdanial.data.local.WeatherDataBase
import com.example.weatherappdanial.data.remote.api.NominatimApiService
import com.example.weatherappdanial.data.remote.api.WeatherApiService
import com.example.weatherappdanial.data.repository.CityRepositoryImpl
import com.example.weatherappdanial.data.repository.WeatherRepositoryImpl
import com.example.weatherappdanial.domain.repository.CityRepository
import com.example.weatherappdanial.domain.repository.WeatherRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), WeatherDataBase::class.java, "weather_db")
            .fallbackToDestructiveMigration(true)
            .build()
    }

    single { get<WeatherDataBase>().weatherDao() }
    single { get<WeatherDataBase>().cityDao() }

    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    single<Converter.Factory> {
        get<Json>().asConverterFactory("application/json".toMediaType())
    }

    single<Retrofit>(named("owm")) {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(get())
            .build()
    }

    single<Retrofit>(named("nominatim")) {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .header("User-Agent", "WeatherAppDanial/1.0")
                        .build()
                )
            }
            .build()

        Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .client(client)
            .addConverterFactory(get())
            .build()
    }

    single<WeatherApiService>   { get<Retrofit>(named("owm")).create() }
    single<NominatimApiService> { get<Retrofit>(named("nominatim")).create() }

    single<WeatherRepository> { WeatherRepositoryImpl(get(), get()) }
    single<CityRepository>    { CityRepositoryImpl(get(), get(), get()) }
}