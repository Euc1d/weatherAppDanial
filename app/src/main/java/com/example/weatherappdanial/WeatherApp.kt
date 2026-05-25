package com.example.weatherappdanial

import android.app.Application
import com.example.weatherappdanial.di.dataModule
import com.example.weatherappdanial.di.locationModule
import com.example.weatherappdanial.di.prefsModule
import com.example.weatherappdanial.di.useCaseModule
import com.example.weatherappdanial.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class WeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@WeatherApp)
            modules(
                dataModule,
                locationModule,
                prefsModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}