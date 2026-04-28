package com.example.weatherappdanial.presentation.test

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherappdanial.domain.weather_model.WeatherData
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WeatherTestScreen(
    onRequestPermission: () -> Unit,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1B2D))
            .padding(16.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🌤 Weather Test",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                onRequestPermission()
                viewModel.loadWeather()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2979FF)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📍 Запросить локацию и погоду", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedContent(
            targetState = uiState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "weather_state"
        ) { state ->
            when (state) {
                is WeatherUiState.Idle -> StatusCard("Нажми кнопку выше 👆", Color(0xFF37474F))
                is WeatherUiState.LoadingLocation -> LoadingCard("Получаем локацию...")
                is WeatherUiState.LoadingWeather -> LoadingCard("Загружаем погоду...")
                is WeatherUiState.Error -> StatusCard("❌ ${state.message}", Color(0xFFB71C1C))
                is WeatherUiState.Success -> WeatherDataCard(state.data)
            }
        }
    }
}

@Composable
private fun StatusCard(text: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            modifier = Modifier.padding(16.dp),
            fontSize = 15.sp
        )
    }
}

@Composable
private fun LoadingCard(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2E45)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircularProgressIndicator(
                color = Color(0xFF2979FF),
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp
            )
            Text(text, color = Color.White, fontSize = 15.sp)
        }
    }
}

@Composable
private fun WeatherDataCard(data: WeatherData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // TODAY
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2E45)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Label("📍 Сегодня")
                InfoRow("Город", data.todayData.cityName)
                InfoRow("Температура", "${data.todayData.currentTemp}°C")
                InfoRow("Макс/Мин", "${data.todayData.maxTemp}° / ${data.todayData.minTemp}°")
                InfoRow("Описание", data.todayData.todayDescription)
            }
        }

        // DETAILS
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2E45)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Label("🌡 Детали")
                val d = data.weatherDetail
                InfoRow("Ощущается как", "${d.feelsLike}°C")
                InfoRow("Влажность", "${d.humidity}%")
                InfoRow("Давление", "${d.pressure} гПа")
                InfoRow("УФ индекс", "${d.UvIndex}")
                InfoRow("Видимость", "${d.visibility} м")
                InfoRow("Точка росы", "${d.dewPoint}°C")
                InfoRow("Ветер", "${d.windInfo.speedKmh} км/ч, порывы ${d.windInfo.gustKmh} км/ч")
                InfoRow("Напр. ветра", "${d.windInfo.directionDeg}°")
                InfoRow("Восход", formatTime(d.sunrise))
                InfoRow("Закат", formatTime(d.sunset))
            }
        }

        // HOURLY
        if (data.hourlyForeCast.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2E45)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Label("🕐 Почасовой (первые 5)")
                    data.hourlyForeCast.take(5).forEach { h ->
                        InfoRow(formatTime(h.timeStamp), "${h.temperature}°C — ${h.description}")
                    }
                }
            }
        }

        // DAILY
        if (data.dailyForeCast.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2E45)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Label("📅 По дням")
                    data.dailyForeCast.forEach { day ->
                        InfoRow(day.dayOfWeek, "${day.minTemp} / ${day.maxTemp}")
                    }
                }
            }
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(
        text = text,
        color = Color(0xFF64B5F6),
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color(0xFF90A4AE), fontSize = 13.sp)
        Text(value, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

private fun formatTime(unixSeconds: Long): String =
    SimpleDateFormat("HH:mm", Locale.getDefault())
        .format(Date(unixSeconds * 1000))