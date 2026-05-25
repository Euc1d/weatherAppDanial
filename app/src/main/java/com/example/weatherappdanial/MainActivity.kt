package com.example.weatherappdanial

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.weatherappdanial.navigation.AppNavigation
import com.example.weatherappdanial.presentation.screens.main.WeatherViewModel
import com.example.weatherappdanial.ui.base_theme.PrimaryTheme


class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.any { it }
        if (granted) {
            viewModel.loadWeather()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrimaryTheme {
                AppNavigation(
                    onRequestPermission = {
                        permissionLauncher.launch(arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ))
                    }
                )
            }
        }
    }
}