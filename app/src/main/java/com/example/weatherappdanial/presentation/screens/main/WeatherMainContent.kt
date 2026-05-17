package com.example.weatherappdanial.presentation.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.weatherappdanial.base_ui_utils.background.isRaining
import com.example.weatherappdanial.base_ui_utils.background.resolveBackground
import com.example.weatherappdanial.base_ui_utils.background.toDrawableRes
import com.example.weatherappdanial.base_ui_utils.formatter.DetailCardData
import com.example.weatherappdanial.base_ui_utils.formatter.HourlyDisplayItem
import com.example.weatherappdanial.base_ui_utils.formatter.buildHourlyItems
import com.example.weatherappdanial.base_ui_utils.formatter.formatAsHour
import com.example.weatherappdanial.base_ui_utils.formatter.formatAsTime
import com.example.weatherappdanial.base_ui_utils.formatter.toDetailCards
import com.example.weatherappdanial.base_ui_utils.formatter.toLastUpdated
import com.example.weatherappdanial.base_ui_utils.formatter.toWindDir
import com.example.weatherappdanial.domain.weather_model.DailyForeCast
import com.example.weatherappdanial.domain.weather_model.ForeCastDetails
import com.example.weatherappdanial.domain.weather_model.HourlyForeCast
import com.example.weatherappdanial.domain.weather_model.TodayData
import com.example.weatherappdanial.ui.base_theme.PrimaryTheme
import com.example.weatherappdanial.ui.base_theme.white_blue_100
private val HPAD   = Modifier.padding(horizontal = 16.dp)
private val VSPACE = Arrangement.spacedBy(12.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherMainContent(
    state           : WeatherUiState.Content,
    isRefreshing    : Boolean,
    onRefresh       : () -> Unit,
    onDismissBanner : () -> Unit,
    modifier        : Modifier = Modifier
) {
    val data= state.data
    val isRaining = data.todayData.todayDescription.isRaining()
    val background = remember(isRaining) { resolveBackground(isRaining) }

    Box(modifier = modifier.fillMaxSize()) {

        androidx.compose.foundation.Image(
            painter      = painterResource(background.toDrawableRes()),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier     = Modifier.fillMaxSize()
        )
        Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.25f)))

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh    = onRefresh,
            modifier     = Modifier.fillMaxSize(),
            indicator    = {
                PullToRefreshDefaults.Indicator(
                    isRefreshing   = isRefreshing,
                    modifier       = Modifier.align(Alignment.TopCenter),
                    color          = PrimaryTheme.colors.textPrimary,
                    containerColor = PrimaryTheme.colors.cardGlass,
                    state = PullToRefreshState()
                )
            }
        ) {
            LazyColumn(
                modifier            = Modifier.fillMaxSize(),
                contentPadding      = PaddingValues(top = 56.dp, bottom = 32.dp),
                verticalArrangement = VSPACE
            ) {
                item {
                    if (!isRefreshing) PtrHint()
                }

                item { Header(data.todayData) }

                item {
                    HourlyStrip(
                        hourly  = data.hourlyForeCast,
                        sunrise = data.weatherDetail.sunrise,
                        sunset  = data.weatherDetail.sunset
                    )
                }

                item { DailySection(data.dailyForeCast) }

                item { WindCard(data.weatherDetail, modifier = Modifier.fillMaxWidth().then(HPAD)) }

                val cards = data.weatherDetail.toDetailCards()
                items(cards.chunked(2)) { pair ->
                    Row(
                        modifier              = Modifier.fillMaxWidth().then(HPAD),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        pair.forEach { card ->
                            DetailCard(card, Modifier.weight(1f))
                        }
                        if (pair.size == 1) Spacer(Modifier.weight(1f))
                    }
                }

                item { Footer(data.cachedAt) }
            }
        }

        AnimatedVisibility(
            visible  = state.banner != null,
            enter    = slideInVertically { -it },
            exit     = slideOutVertically { -it },
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            state.banner?.let { StaleBanner(it, onDismissBanner) }
        }
    }
}



@Composable
private fun PtrHint() = Row(
    modifier              = Modifier.fillMaxWidth().padding(bottom = 4.dp),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment     = Alignment.CenterVertically
) {
    Icon(Icons.Default.KeyboardArrowDown, null,
        Modifier.size(14.dp), PrimaryTheme.colors.textHint)
    Spacer(Modifier.width(4.dp))
    Text(
        "Потяните для обновления",
        style = PrimaryTheme.typography.detailCardLabel,
        color = PrimaryTheme.colors.textHint
    )
}


@Composable
private fun Header(t: TodayData) = Column(
    modifier            = Modifier.fillMaxWidth().then(HPAD),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    val c  = PrimaryTheme.colors
    val ty = PrimaryTheme.typography
    Text(t.cityName,                                              style = ty.cityTitle,          color = c.textPrimary)
    Text("${t.currentTemp}°",                                    style = ty.temperatureDisplay, color = c.textPrimary)
    Text(t.todayDescription.replaceFirstChar { it.uppercase() }, style = ty.conditionLabel,    color = c.textSecondary)
    Text("Макс.: ${t.maxTemp}°, мин.: ${t.minTemp}°",           style = ty.minMaxLabel,        color = c.textSecondary)
}


@Composable
private fun HourlyStrip(hourly: List<HourlyForeCast>, sunrise: Long, sunset: Long) {
    val items = remember(hourly, sunrise, sunset) { buildHourlyItems(hourly, sunrise, sunset) }
    GlassBox(modifier = Modifier.fillMaxWidth().then(HPAD), padding = 12.dp) {
        LazyRow(
            contentPadding        = PaddingValues(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items) { item ->
                when (item) {
                    is HourlyDisplayItem.Forecast -> HourlyCell(item.data)
                    is HourlyDisplayItem.SunEvent -> SunCell(item)
                }
            }
        }
    }
}

@Composable
private fun HourlyCell(d: HourlyForeCast) = Column(
    modifier            = Modifier.width(52.dp).padding(vertical = 4.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(4.dp)
) {
    Text(d.timeStamp.formatAsHour(), style = PrimaryTheme.typography.hourlyTime, color = PrimaryTheme.colors.textPrimary)
    WeatherIcon(d.icon, 24.dp)
    Text("${d.temperature}°",        style = PrimaryTheme.typography.hourlyTemp, color = PrimaryTheme.colors.textPrimary)
}

@Composable
private fun SunCell(s: HourlyDisplayItem.SunEvent) = Column(
    modifier            = Modifier.width(52.dp).padding(vertical = 4.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(4.dp)
) {
    Text(s.timeUnix.formatAsTime(),
        style = PrimaryTheme.typography.hourlyTime,
        color = PrimaryTheme.colors.textPrimary)
    Icon(
        if (s.isSunset) Icons.Default.WbTwilight else Icons.Default.WbSunny,
        null, Modifier.size(24.dp), Color(0xFFFFC107)
    )
    Text(
        if (s.isSunset) "Закат" else "Восход",
        style = PrimaryTheme.typography.hourlyTime,
        color = PrimaryTheme.colors.textSecondary
    )
}


@Composable
private fun DailySection(daily: List<DailyForeCast>) {
    val gMin = daily.minOfOrNull { it.minTemp.dropLast(1).toIntOrNull() ?: 0 } ?: -10
    val gMax = daily.maxOfOrNull { it.maxTemp.dropLast(1).toIntOrNull() ?: 0 } ?: 15

    GlassBox(modifier = Modifier.fillMaxWidth().then(HPAD), padding = 0.dp) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            daily.forEachIndexed { i, day ->
                DailyRow(day, gMin, gMax)
                if (i < daily.lastIndex) HorizontalDivider(
                    color = PrimaryTheme.colors.textHint.copy(alpha = 0.2f),
                    thickness = 0.5.dp
                )
            }
        }
    }
}

@Composable
private fun DailyRow(d: DailyForeCast, gMin: Int, gMax: Int) = Row(
    modifier          = Modifier.fillMaxWidth().padding(vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    val ty = PrimaryTheme.typography; val c = PrimaryTheme.colors
    Text(d.dayOfWeek, style = ty.forecastDayName,  color = c.textPrimary,   modifier = Modifier.weight(1.5f))
    WeatherIcon(d.icon, 20.dp, Modifier.weight(0.6f))
    Text(d.minTemp,   style = ty.forecastTempRange, color = c.textSecondary, modifier = Modifier.weight(0.7f), textAlign = TextAlign.End)
    TempBar(
        d.minTemp.dropLast(1).toIntOrNull() ?: 0,
        d.maxTemp.dropLast(1).toIntOrNull() ?: 0,
        gMin, gMax,
        Modifier.weight(1.5f).padding(horizontal = 6.dp)
    )
    Text(d.maxTemp,   style = ty.forecastTempRange, color = c.textPrimary,   modifier = Modifier.weight(0.7f))
}

@Composable
private fun TempBar(min: Int, max: Int, gMin: Int, gMax: Int, modifier: Modifier) {
    val range = (gMax - gMin).toFloat().coerceAtLeast(1f)
    Canvas(modifier.height(4.dp)) {
        drawRoundRect(Color.White.copy(alpha = 0.18f), cornerRadius = CornerRadius(4.dp.toPx()))
        val sF = (min - gMin) / range
        val eF = (max - gMin) / range
        drawRoundRect(
            brush        = Brush.horizontalGradient(listOf(white_blue_100, white_blue_100)),
            topLeft      = Offset(sF * size.width, 0f),
            size         = Size((eF - sF) * size.width, size.height),
            cornerRadius = CornerRadius(4.dp.toPx())
        )
    }
}


@Composable
private fun WindCard(details: ForeCastDetails, modifier: Modifier = Modifier) {
    val c  = PrimaryTheme.colors
    val ty = PrimaryTheme.typography
    val wind = details.windInfo

    GlassBox(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Air, null, Modifier.size(13.dp), c.textHint)
            Spacer(Modifier.width(5.dp))
            Text("ВЕТЕР", style = ty.detailCardLabel, color = c.textHint)
        }
        Spacer(Modifier.height(10.dp))

        // Строки
        val rows = listOf(
            "Ветер"        to "${wind.speedKmh} км/ч",
            "Порывы ветра" to "${wind.gustKmh} км/ч",
            "Направление"  to "${wind.directionDeg}° ${wind.directionDeg.toWindDir()}"
        )
        rows.forEachIndexed { i, (label, value) ->
            Row(
                modifier              = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(label, style = ty.cardTextStyle, color = c.textPrimary)
                Text(value, style = ty.cardTextStyle, color = c.textPrimary)
            }
            if (i < rows.lastIndex) HorizontalDivider(
                modifier  = Modifier.padding(vertical = 5.dp),
                thickness = 0.5.dp,
                color     = c.textHint.copy(alpha = 0.2f)
            )
        }
    }
}


@Composable
private fun DetailCard(card: DetailCardData, modifier: Modifier = Modifier) {
    val c  = PrimaryTheme.colors
    val ty = PrimaryTheme.typography
    GlassBox(
        modifier = modifier.aspectRatio(1f),
        padding  = 14.dp
    ) {
        Column(
            modifier            = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(card.label.uppercase(), style = ty.detailCardLabel,   color = c.textHint)
            Text(card.mainValue,         style = ty.detailCardValue,   color = c.textPrimary)
            card.subtext?.let {
                Text(it,                 style = ty.detailCardSubtext, color = c.textSecondary)
            }
        }
    }
}



@Composable
private fun Footer(cachedAt: Long) {
    var label by remember { mutableStateOf(cachedAt.toLastUpdated()) }
    LaunchedEffect(cachedAt) {
        while (true) {
            label = cachedAt.toLastUpdated()
            kotlinx.coroutines.delay(60_000L)
        }
    }
    Row(
        modifier              = Modifier.fillMaxWidth().then(HPAD).padding(top = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Schedule, null, Modifier.size(12.dp), PrimaryTheme.colors.textHint)
        Spacer(Modifier.width(4.dp))
        Text(label, style = PrimaryTheme.typography.detailCardLabel, color = PrimaryTheme.colors.textHint)
    }
}


@Composable
private fun StaleBanner(banner: WeatherBanner, onDismiss: () -> Unit) {
    val (text, icon) = when (banner) {
        WeatherBanner.NoInternet  -> "Нет соединения. Показаны кешированные данные." to Icons.Default.WifiOff
        WeatherBanner.ServerError -> "Ошибка обновления. Данные могут быть устаревшими." to Icons.Default.CloudOff
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryTheme.colors.cardGlass)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, null, Modifier.size(18.dp), PrimaryTheme.colors.textSecondary)
        Text(text, style = PrimaryTheme.typography.detailCardSubtext,
            color = PrimaryTheme.colors.textSecondary, modifier = Modifier.weight(1f))
        IconButton(onDismiss, Modifier.size(24.dp)) {
            Icon(Icons.Default.Close, null, tint = PrimaryTheme.colors.textHint)
        }
    }
}


@Composable
private fun GlassBox(
    modifier : Modifier = Modifier,
    padding  : Dp = 14.dp,
    content  : @Composable ColumnScope.() -> Unit
) = Column(
    modifier = modifier
        .background(PrimaryTheme.colors.cardGlass, PrimaryTheme.shapes.detailCardShape)
        .padding(padding),
    content = content
)

@Composable
private fun WeatherIcon(icon: String, size: Dp, modifier: Modifier = Modifier) =
    AsyncImage("https://openweathermap.org/img/wn/$icon@2x.png", null, modifier.size(size))
