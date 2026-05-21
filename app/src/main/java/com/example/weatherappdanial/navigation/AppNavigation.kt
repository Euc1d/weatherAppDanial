package com.example.weatherappdanial.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weatherappdanial.presentation.screens.cities.CitiesScreen
import com.example.weatherappdanial.presentation.screens.main.WeatherScreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavigation(
    onRequestPermission: () -> Unit
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.Weather.route
    ) {

        composable(AppScreens.Cities.route) {

            CitiesScreen(

                onCityClick = { city ->

                    val encodedName = URLEncoder.encode(
                        city.displayName,
                        StandardCharsets.UTF_8.toString()
                    )

                    navController.navigate(
                        AppScreens.Weather.createRoute(
                            lat = city.lat.toFloat(),
                            lon = city.lon.toFloat(),
                            cityName = encodedName
                        )
                    ) {
                        launchSingleTop = true
                    }
                },

                onCurrentLocation = {

                    navController.navigate(
                        AppScreens.Weather.createRoute()
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = AppScreens.Weather.route,
            arguments = AppScreens.Weather.arguments
        ) {

            WeatherScreen(

                onRequestPermission = onRequestPermission,

                onOpenCities = {
                    navController.navigate(AppScreens.Cities.route)
                }
            )
        }
    }
}

sealed class AppScreens {

    data object Cities : AppScreens() {
        const val route = "cities"
    }

    data object Weather : AppScreens() {

        private const val BASE_ROUTE = "weather"

        const val route =
            "$BASE_ROUTE/{lat}/{lon}/{cityName}"

        val arguments = listOf(

            navArgument("lat") {
                type = NavType.FloatType
                defaultValue = 0.0f
            },

            navArgument("lon") {
                type = NavType.FloatType
                defaultValue = 0.0f
            },

            navArgument("cityName") {
                type = NavType.StringType
                defaultValue = ""
            }
        )

        fun createRoute(
            lat: Float = 0.0f,
            lon: Float = 0.0f,
            cityName: String = ""
        ): String {

            return "$BASE_ROUTE/$lat/$lon/$cityName"
        }
    }
}