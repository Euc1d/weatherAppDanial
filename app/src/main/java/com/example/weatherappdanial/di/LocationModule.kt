package com.example.weatherappdanial.di

import com.example.weatherappdanial.data.repository.LocationRepositoryImpl
import com.example.weatherappdanial.data.repository.UserPrefsRepositoryImpl
import com.example.weatherappdanial.domain.pref.UserPrefsRepository
import com.example.weatherappdanial.domain.repository.LocationRepository
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val locationModule = module {
    single { LocationServices.getFusedLocationProviderClient(androidContext()) }
    single<LocationRepository> { LocationRepositoryImpl(androidContext(), get()) }
}
val prefsModule = module {
    single<UserPrefsRepository> { UserPrefsRepositoryImpl(androidContext()) }
}