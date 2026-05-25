package com.example.weatherappdanial.presentation.screens.main


import android.content.Intent
import android.provider.Settings
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.GpsOff
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherappdanial.R
import com.example.weatherappdanial.base_ui_utils.ui.WeatherErrorScreen
import com.example.weatherappdanial.base_ui_utils.ui.WeatherLoadingScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeatherScreen(
    onRequestPermission: () -> Unit,
    onOpenCities: () -> Unit,
    viewModel: WeatherViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var isRefreshing by remember { mutableStateOf(false) }

    when (val s = state) {
        WeatherUiState.Loading -> WeatherLoadingScreen()

        is WeatherUiState.Content -> {

            LaunchedEffect(s.data.cachedAt, s.banner) { isRefreshing = false }
            val tempUnit by viewModel.temperatureUnit.collectAsStateWithLifecycle()

            WeatherMainContent(
                state = s,
                isRefreshing = isRefreshing,
                tempUnit = tempUnit,
                onRefresh = { isRefreshing = true; viewModel.loadWeather() },
                onDismissBanner = viewModel::dismissBanner,
                onOpenCities = onOpenCities
            )
        }

        WeatherUiState.FullScreenError.NoPermission -> WeatherErrorScreen(
            icon = Icons.Default.LocationOff,
            title = stringResource(R.string.location_permission_title),
            description = stringResource(R.string.location_permission_desc),
            actionLabel = stringResource(R.string.allow),
            onAction = onRequestPermission,
            onRefresh = viewModel::loadWeather
        )

        WeatherUiState.FullScreenError.GpsDisabled -> WeatherErrorScreen(
            icon = Icons.Default.GpsOff,
            title = stringResource(R.string.gps_disabled_title),
            description = stringResource(R.string.gps_disabled_desc),
            actionLabel = stringResource(R.string.open_settings),
            onAction = {
                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            },
            onRefresh = viewModel::loadWeather
        )

        WeatherUiState.FullScreenError.NoInternet -> WeatherErrorScreen(
            icon = Icons.Default.WifiOff,
            title = stringResource(R.string.no_internet_title),
            description = stringResource(R.string.no_internet_desc),
            actionLabel = stringResource(R.string.retry),
            onAction = viewModel::loadWeather,
            onRefresh = viewModel::loadWeather
        )

        WeatherUiState.FullScreenError.ServerError -> WeatherErrorScreen(
            icon = Icons.Default.CloudOff,
            title = stringResource(R.string.server_error_title),
            description = stringResource(R.string.server_error_desc),
            actionLabel = stringResource(R.string.retry),
            onAction = viewModel::loadWeather,
            onRefresh = viewModel::loadWeather
        )

        WeatherUiState.FullScreenError.Unknown -> WeatherErrorScreen(
            icon = Icons.Default.ErrorOutline,
            title = stringResource(R.string.unknown_error_title),
            description = stringResource(R.string.unknown_error_desc),
            actionLabel = stringResource(R.string.retry),
            onAction = viewModel::loadWeather,
            onRefresh = viewModel::loadWeather
        )
    }
}

