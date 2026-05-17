package com.example.weatherappdanial.presentation.screens.main


import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.GpsOff
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherappdanial.base_ui_utils.background.resolveBackground
import com.example.weatherappdanial.base_ui_utils.background.toDrawableRes
import com.example.weatherappdanial.base_ui_utils.ui.WeatherErrorScreen
import com.example.weatherappdanial.ui.base_theme.PrimaryTheme

@Composable
fun WeatherScreen(
    onRequestPermission: () -> Unit,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val state   by viewModel.uiState.collectAsStateWithLifecycle()
    val context  = LocalContext.current
    var isRefreshing by remember { mutableStateOf(false) }

    when (val s = state) {
        WeatherUiState.Loading -> WeatherLoadingScreen()

        is WeatherUiState.Content -> {

            LaunchedEffect(s.data.cachedAt, s.data) { isRefreshing = false }

            WeatherMainContent(
                state           = s,
                isRefreshing    = isRefreshing,
                onRefresh       = { isRefreshing = true; viewModel.loadWeather() },
                onDismissBanner = viewModel::dismissBanner
            )
        }

        WeatherUiState.FullScreenError.NoPermission -> WeatherErrorScreen(
            icon = Icons.Default.LocationOff,
            title = "Нет доступа к геолокации",
            description = "Разрешите приложению определять местоположение.",
            actionLabel = "Разрешить",
            onAction = onRequestPermission,
            onRefresh = viewModel::loadWeather

        )
        WeatherUiState.FullScreenError.GpsDisabled -> WeatherErrorScreen(
            icon = Icons.Default.GpsOff,
            title = "GPS отключён",
            description = "Включите службы геолокации в настройках.",
            actionLabel = "Настройки",
            onAction = { context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) },
            onRefresh = viewModel::loadWeather
        )
        WeatherUiState.FullScreenError.NoInternet -> WeatherErrorScreen(
            icon = Icons.Default.WifiOff,
            title = "Нет подключения",
            description = "Проверьте интернет-соединение и повторите.",
            actionLabel = "Повторить", onAction = viewModel::loadWeather,
            onRefresh = viewModel::loadWeather
        )
        WeatherUiState.FullScreenError.ServerError -> WeatherErrorScreen(
            icon = Icons.Default.CloudOff,
            title = "Ошибка сервера",
            description = "Сервер временно недоступен.",
            actionLabel = "Повторить", onAction = viewModel::loadWeather,
            onRefresh = viewModel::loadWeather
        )
        WeatherUiState.FullScreenError.Unknown -> WeatherErrorScreen(
            icon = Icons.Default.ErrorOutline,
            title = "Что-то пошло не так",
            description = "Попробуйте обновить приложение.",
            actionLabel = "Повторить", onAction = viewModel::loadWeather,
            onRefresh = viewModel::loadWeather
        )
    }
}


@Composable
fun WeatherLoadingScreen() {

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(
                resolveBackground(false).toDrawableRes()
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PrimaryTheme.colors.backgroundDark.copy(alpha = 0.25f))
        )

        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = PrimaryTheme.colors.textPrimary
        )
    }
}