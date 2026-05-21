package com.example.weatherappdanial.presentation.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.weatherappdanial.R
import com.example.weatherappdanial.base_ui_utils.background.isRaining
import com.example.weatherappdanial.base_ui_utils.background.localHourForCity
import com.example.weatherappdanial.base_ui_utils.background.resolveBackground
import com.example.weatherappdanial.base_ui_utils.background.toDrawableRes
import com.example.weatherappdanial.base_ui_utils.formatter.DetailCardData
import com.example.weatherappdanial.base_ui_utils.formatter.HourlyDisplayItem
import com.example.weatherappdanial.base_ui_utils.formatter.buildHourlyItems
import com.example.weatherappdanial.base_ui_utils.formatter.formatAsHour
import com.example.weatherappdanial.base_ui_utils.formatter.formatAsTime
import com.example.weatherappdanial.base_ui_utils.formatter.formatTemp
import com.example.weatherappdanial.base_ui_utils.formatter.resolveSubtext
import com.example.weatherappdanial.base_ui_utils.formatter.toAvgCard
import com.example.weatherappdanial.base_ui_utils.formatter.toDetailCards
import com.example.weatherappdanial.base_ui_utils.formatter.toLastUpdated
import com.example.weatherappdanial.base_ui_utils.formatter.toWindDir
import com.example.weatherappdanial.domain.pref.TemperatureUnit
import com.example.weatherappdanial.domain.weather_model.DailyForeCast
import com.example.weatherappdanial.domain.weather_model.ForeCastDetails
import com.example.weatherappdanial.domain.weather_model.HourlyForeCast
import com.example.weatherappdanial.domain.weather_model.TodayData
import com.example.weatherappdanial.ui.base_theme.PrimaryTheme
import com.example.weatherappdanial.ui.base_theme.light_blue100
import com.example.weatherappdanial.ui.base_theme.white_blue_100

private val HPAD = Modifier.padding(horizontal = 16.dp)
private val VSPACE = Arrangement.spacedBy(12.dp)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherMainContent(
    state: WeatherUiState.Content,
    tempUnit: TemperatureUnit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onDismissBanner: () -> Unit,
    modifier: Modifier = Modifier,
    onOpenCities: () -> Unit
) {
    val data = state.data
    val isRaining = data.todayData.todayDescription.isRaining()
    val background = remember(isRaining, data.todayData.timezoneOffsetSec) {
        resolveBackground(isRaining, localHourForCity(data.todayData.timezoneOffsetSec))
    }
    val listState = rememberLazyListState()
    val headerAlpha by remember {
        derivedStateOf {
            val scrollOffset = listState.firstVisibleItemScrollOffset
            val firstIndex = listState.firstVisibleItemIndex
            val fadeDistance = 700f
            if (firstIndex >= 1) 0f
            else (1f - (scrollOffset / fadeDistance)).coerceIn(0f, 1f)
        }
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CompactHeader(data.todayData, headerAlpha, tempUnit)
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        onOpenCities()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_button_to_city),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.clip(CircleShape)
                )
            }
        }
    ) { padding ->
        Box(modifier = modifier.fillMaxSize()) {
            Image(
                painter = painterResource(background.toDrawableRes()),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.25f))
            )
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize(),
                indicator = {
                    PullToRefreshDefaults.Indicator(
                        isRefreshing = isRefreshing,
                        modifier = Modifier.align(Alignment.TopCenter),
                        color = PrimaryTheme.colors.textPrimary,
                        containerColor = PrimaryTheme.colors.cardGlass,
                        state = PullToRefreshState()
                    )
                }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = padding.calculateTopPadding(),
                        bottom = 32.dp
                    ),
                    verticalArrangement = VSPACE,
                    state = listState
                ) {
                    item { if (!isRefreshing) PtrHint() }

                    item {
                        Spacer(modifier = Modifier.height((36 * headerAlpha).dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    alpha = headerAlpha
                                    val scroll = listState.firstVisibleItemScrollOffset.toFloat()
                                    translationY = -(scroll * 0.35f)
                                }
                        ) {
                            Header(
                                t = data.todayData,
                                modifier = Modifier.fillMaxWidth(),
                                tempUnit = tempUnit
                            )
                            Spacer(modifier = Modifier.height((80 * headerAlpha).dp))
                        }
                    }

                    item {
                        HourlyStrip(
                            hourly = data.hourlyForeCast,
                            sunrise = data.weatherDetail.sunrise,
                            sunset = data.weatherDetail.sunset,
                            daily = data.dailyForeCast,
                            tempUnit = tempUnit
                        )
                    }

                    item {
                        DailySection(
                            daily = data.dailyForeCast,
                            modifier = Modifier
                                .fillMaxWidth()
                                .then(HPAD),
                            tempUnit = tempUnit
                        )
                    }

                    item {
                        WindSection(
                            details = data.weatherDetail,
                            modifier = Modifier
                                .fillMaxWidth()
                                .then(HPAD)
                        )
                    }

                    val cards = buildList {
                        add(data.todayData.toAvgCard())
                        addAll(data.weatherDetail.toDetailCards())
                    }
                    items(cards.chunked(2)) { pair ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .then(HPAD),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            pair.forEach { card -> DetailCard(card, Modifier.weight(1f)) }
                            if (pair.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }

                    item { Footer(data.cachedAt) }
                }
            }
            AnimatedVisibility(
                visible = state.banner != null,
                enter = slideInVertically { -it },
                exit = slideOutVertically { -it },
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                state.banner?.let { StaleBanner(it, onDismissBanner) }
            }
        }
    }
}

@Composable
private fun InfoSectionCard(
    headerIcon: ImageVector,
    headerLabel: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val c = PrimaryTheme.colors
    val ty = PrimaryTheme.typography

    GlassBox(modifier = modifier, padding = 0.dp) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(headerIcon, null, Modifier.size(13.dp), c.textHint)
            Spacer(Modifier.width(5.dp))
            Text(headerLabel.uppercase(), style = ty.detailCardLabel, color = c.textHint)
        }
        HorizontalDivider(
            color = c.textHint.copy(alpha = 0.15f),
            thickness = 0.5.dp
        )
        Column(Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            content()
        }
    }
}

@Composable
private fun DailySection(
    daily: List<DailyForeCast>,
    modifier: Modifier = Modifier,
    tempUnit: TemperatureUnit
) {
    val gMin = daily.minOfOrNull { it.minTemp.dropLast(1).toIntOrNull() ?: 0 } ?: -10
    val gMax = daily.maxOfOrNull { it.maxTemp.dropLast(1).toIntOrNull() ?: 0 } ?: 15

    InfoSectionCard(
        headerIcon = Icons.Default.CalendarMonth,
        headerLabel = stringResource(R.string.forecast_10_days),
        modifier = modifier
    ) {
        daily.forEachIndexed { i, day ->
            DailyRow(day, gMin, gMax, tempUnit)
            if (i < daily.lastIndex) HorizontalDivider(
                color = PrimaryTheme.colors.textHint.copy(alpha = 0.2f),
                thickness = 0.5.dp
            )
        }
    }
}

@Composable
private fun WindSection(details: ForeCastDetails, modifier: Modifier = Modifier) {
    val c = PrimaryTheme.colors
    val ty = PrimaryTheme.typography
    val wind = details.windInfo
    val context = LocalContext.current

    val rows = listOf(
        stringResource(R.string.wind) to stringResource(
            R.string.wind_speed_format,
            wind.speedKmh
        ),
        stringResource(R.string.wind_gusts) to stringResource(
            R.string.wind_speed_format,
            wind.gustKmh
        ),
        stringResource(R.string.wind_direction) to stringResource(
            R.string.wind_direction_format,
            wind.directionDeg,
            wind.directionDeg.toWindDir(context)
        )
    )

    InfoSectionCard(
        headerIcon = Icons.Default.Air,
        headerLabel = stringResource(R.string.wind),
        modifier = modifier
    ) {
        rows.forEachIndexed { i, (label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(label, style = ty.cardTextStyle, color = c.textPrimary)
                Text(value, style = ty.cardTextStyle, color = c.textPrimary)
            }
            if (i < rows.lastIndex) HorizontalDivider(
                modifier = Modifier.padding(vertical = 5.dp),
                thickness = 0.5.dp,
                color = c.textHint.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
private fun DetailCard(card: DetailCardData, modifier: Modifier = Modifier) {
    val c = PrimaryTheme.colors
    val ty = PrimaryTheme.typography
    val context = LocalContext.current
    val isUv = card.label == R.string.uv_index

    GlassBox(modifier = modifier.aspectRatio(1f), padding = 14.dp) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(card.icon),
                    contentDescription = null,
                    modifier = Modifier.size(13.dp),
                    tint = c.textHint
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    stringResource(card.label).uppercase(),
                    style = ty.detailCardLabel,
                    color = c.textHint
                )
            }
            Text(card.mainValue, style = ty.detailCardValue, color = c.textPrimary)
            if (isUv) UvGradientBar(card.mainValue.toIntOrNull() ?: 0)
            card.resolveSubtext(context)?.let {
                Text(it, style = ty.detailCardSubtext, color = c.textSecondary)
            }
        }
    }
}

@Composable
private fun UvGradientBar(uvValue: Int) {
    val maxUv = 11f
    val fraction = (uvValue / maxUv).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
    ) {
        Canvas(Modifier.fillMaxSize()) {
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFF4CAF50),
                        Color(0xFFFFEB3B),
                        Color(0xFFFF9800),
                        Color(0xFFF44336),
                        Color(0xFF9C27B0),
                    )
                ),
                cornerRadius = CornerRadius(4.dp.toPx())
            )
        }
        Canvas(Modifier.fillMaxSize()) {
            val cx = fraction * size.width
            drawCircle(
                color = Color.White,
                radius = 5.dp.toPx(),
                center = Offset(cx, size.height / 2f)
            )
        }
    }
}

@Composable
private fun PtrHint() = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 4.dp),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
) {
    Icon(
        Icons.Default.KeyboardArrowDown,
        null,
        Modifier.size(14.dp),
        PrimaryTheme.colors.textHint
    )
    Spacer(Modifier.width(4.dp))
    Text(
        stringResource(R.string.pull_to_refresh),
        style = PrimaryTheme.typography.detailCardLabel,
        color = PrimaryTheme.colors.textHint
    )
}

@Composable
private fun Header(
    t: TodayData,
    modifier: Modifier = Modifier,
    tempUnit: TemperatureUnit
) = Column(
    modifier = modifier
        .fillMaxWidth()
        .then(HPAD),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    val c = PrimaryTheme.colors
    val ty = PrimaryTheme.typography
    Text(t.cityName, style = ty.cityTitle, color = c.textPrimary)
    Text(
        stringResource(R.string.temperature_format, t.currentTemp.formatTemp(tempUnit)),
        style = ty.temperatureDisplay,
        color = c.textPrimary
    )
    Text(
        t.todayDescription.replaceFirstChar { it.uppercase() },
        style = ty.conditionLabel,
        color = light_blue100
    )
    Text(
        stringResource(
            R.string.max_min_temp,
            t.maxTemp.formatTemp(tempUnit),
            t.minTemp.formatTemp(tempUnit)
        ),
        style = ty.minMaxLabel,
        color = c.textSecondary
    )
}

@Composable
private fun HourlyStrip(
    hourly: List<HourlyForeCast>,
    sunrise: Long,
    sunset: Long,
    daily: List<DailyForeCast>,
    tempUnit: TemperatureUnit
) {
    val items = remember(hourly, sunrise, sunset) { buildHourlyItems(hourly, sunrise, sunset) }
    val todaySummary = daily.firstOrNull()?.summary.orEmpty()
    GlassBox(
        modifier = Modifier
            .fillMaxWidth()
            .then(HPAD),
        padding = 12.dp
    ) {
        if (todaySummary.isNotBlank()) {
            Text(
                text = todaySummary,
                style = PrimaryTheme.typography.detailCardSubtext,
                color = PrimaryTheme.colors.textSecondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            HorizontalDivider(
                color = PrimaryTheme.colors.textHint.copy(alpha = 0.2f),
                thickness = 0.5.dp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items) { item ->
                when (item) {
                    is HourlyDisplayItem.Forecast -> HourlyCell(item.data, item.isNow, tempUnit)
                    is HourlyDisplayItem.SunEvent -> SunCell(item)
                }
            }
        }
    }
}

@Composable
private fun HourlyCell(
    d: HourlyForeCast,
    isNow: Boolean,
    tempUnit: TemperatureUnit
) = Column(
    modifier = Modifier
        .width(52.dp)
        .padding(vertical = 4.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(4.dp)
) {
    Text(
        text = if (isNow) stringResource(R.string.now) else d.timeStamp.formatAsHour(),
        style = PrimaryTheme.typography.hourlyTime,
        color = PrimaryTheme.colors.textPrimary
    )
    WeatherIcon(d.icon, 24.dp)
    Text(
        stringResource(R.string.temperature_format, d.temperature.formatTemp(tempUnit)),
        style = PrimaryTheme.typography.hourlyTemp,
        color = PrimaryTheme.colors.textPrimary
    )
}

@Composable
private fun SunCell(s: HourlyDisplayItem.SunEvent) = Column(
    modifier = Modifier
        .width(52.dp)
        .padding(vertical = 4.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(4.dp)
) {
    Text(
        s.timeUnix.formatAsTime(),
        style = PrimaryTheme.typography.hourlyTime,
        color = PrimaryTheme.colors.textPrimary
    )
    Icon(
        painter = painterResource(
            if (s.isSunset) R.drawable.ic_sunset else R.drawable.ic_sun
        ),
        contentDescription = null,
        modifier = Modifier.size(24.dp),
        tint = Color(0xFFFFC107)
    )
    Text(
        stringResource(if (s.isSunset) R.string.sunset else R.string.sunrise),
        style = PrimaryTheme.typography.hourlyTime,
        color = PrimaryTheme.colors.textSecondary
    )
}

@Composable
private fun DailyRow(
    d: DailyForeCast,
    gMin: Int,
    gMax: Int,
    tempUnit: TemperatureUnit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    val ty = PrimaryTheme.typography
    val c = PrimaryTheme.colors

    val minTempInt = d.minTemp.dropLast(1).toIntOrNull() ?: 0
    val maxTempInt = d.maxTemp.dropLast(1).toIntOrNull() ?: 0

    Text(
        d.dayOfWeek,
        style = ty.forecastDayName,
        color = c.textPrimary,
        modifier = Modifier.weight(1.5f)
    )

    WeatherIcon(d.icon, 24.dp, Modifier.weight(0.6f))

    Text(
        text = minTempInt.formatTemp(tempUnit).toString()+"°",
        style = ty.forecastTempRange,
        color = c.textSecondary,
        modifier = Modifier.weight(0.7f),
        textAlign = TextAlign.End
    )

    TempBar(
        minTempInt,
        maxTempInt,
        gMin,
        gMax,
        Modifier
            .weight(1.5f)
            .padding(horizontal = 6.dp)
    )

    Text(
        text = maxTempInt.formatTemp(tempUnit).toString()+"°",
        style = ty.forecastTempRange,
        color = c.textPrimary,
        modifier = Modifier.weight(0.7f)
    )
}

@Composable
private fun TempBar(min: Int, max: Int, gMin: Int, gMax: Int, modifier: Modifier) {
    val range = (gMax - gMin).toFloat().coerceAtLeast(1f)
    Canvas(modifier.height(4.dp)) {
        drawRoundRect(Color.White.copy(alpha = 0.18f), cornerRadius = CornerRadius(4.dp.toPx()))
        val sF = (min - gMin) / range
        val eF = (max - gMin) / range
        drawRoundRect(
            brush = Brush.horizontalGradient(listOf(white_blue_100, white_blue_100)),
            topLeft = Offset(sF * size.width, 0f),
            size = Size((eF - sF) * size.width, size.height),
            cornerRadius = CornerRadius(4.dp.toPx())
        )
    }
}

@Composable
private fun Footer(cachedAt: Long) {
    val context = LocalContext.current
    var label by remember { mutableStateOf(cachedAt.toLastUpdated(context)) }

    LaunchedEffect(cachedAt) {
        while (true) {
            label = cachedAt.toLastUpdated(context)
            kotlinx.coroutines.delay(60_000L)
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(HPAD)
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Schedule, null, Modifier.size(12.dp), PrimaryTheme.colors.textHint)
        Spacer(Modifier.width(4.dp))
        Text(
            label,
            style = PrimaryTheme.typography.detailCardLabel,
            color = PrimaryTheme.colors.textHint
        )
    }
}

@Composable
private fun StaleBanner(banner: WeatherBanner, onDismiss: () -> Unit) {
    val (textRes, icon) = when (banner) {
        WeatherBanner.NoInternet -> R.string.no_connection_cached to Icons.Default.WifiOff
        WeatherBanner.ServerError -> R.string.update_error_stale to Icons.Default.CloudOff
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryTheme.colors.cardGlass)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, null, Modifier.size(18.dp), PrimaryTheme.colors.textSecondary)
        Text(
            stringResource(textRes),
            style = PrimaryTheme.typography.detailCardSubtext,
            color = PrimaryTheme.colors.textSecondary,
            modifier = Modifier.weight(1f)
        )
        IconButton(onDismiss, Modifier.size(24.dp)) {
            Icon(Icons.Default.Close, null, tint = PrimaryTheme.colors.textHint)
        }
    }
}

@Composable
private fun GlassBox(
    modifier: Modifier = Modifier,
    padding: Dp = 14.dp,
    content: @Composable ColumnScope.() -> Unit
) = Column(
    modifier = modifier
        .background(PrimaryTheme.colors.cardGlass, PrimaryTheme.shapes.detailCardShape)
        .padding(padding),
    content = content
)

@Composable
private fun CompactHeader(
    t: TodayData,
    headerAlpha: Float,
    tempUnit: TemperatureUnit
) {
    val c = PrimaryTheme.colors
    val ty = PrimaryTheme.typography

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 64.dp)
            .graphicsLayer {
                alpha = 1f - headerAlpha
            }
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = t.cityName,
                style = ty.cityTitle,
                color = c.textPrimary
            )
            Text(
                text = "${t.currentTemp.formatTemp(tempUnit)} • ${
                    t.todayDescription.replaceFirstChar { it.uppercase() }
                }",
                style = ty.detailCardSubtext,
                color = light_blue100
            )
        }
    }
}

@Composable
private fun WeatherIcon(icon: String, size: Dp, modifier: Modifier = Modifier) =
    AsyncImage("https://openweathermap.org/img/wn/$icon@2x.png", null, modifier.size(size))