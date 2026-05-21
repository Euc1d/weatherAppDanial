package com.example.weatherappdanial.presentation.screens.cities

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherappdanial.R
import com.example.weatherappdanial.base_ui_utils.background.localHourForCity
import com.example.weatherappdanial.base_ui_utils.background.resolveBackground
import com.example.weatherappdanial.base_ui_utils.background.toDrawableRes
import com.example.weatherappdanial.base_ui_utils.formatter.formatAsTime
import com.example.weatherappdanial.base_ui_utils.formatter.formatTemp
import com.example.weatherappdanial.domain.city_model.CityLocation
import com.example.weatherappdanial.domain.city_model.CitySummary
import com.example.weatherappdanial.domain.pref.TemperatureUnit
import com.example.weatherappdanial.ui.base_theme.PrimaryTheme

private val HPAD = Modifier.padding(horizontal = 16.dp)

@Composable
fun CitiesScreen(
    onCityClick: (CityLocation) -> Unit,
    onCurrentLocation: () -> Unit,
    vm: CitiesViewModel = hiltViewModel()
) {
    val tempUnit    by vm.temperatureUnit.collectAsStateWithLifecycle()
    val cities by vm.cities.collectAsStateWithLifecycle()
    val summaries by vm.summaries.collectAsStateWithLifecycle()
    val searchState by vm.searchState.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }
    var searching by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryTheme.colors.backgroundDark)
            .statusBarsPadding()
    ) {
        LazyColumn(
            modifier            = Modifier.fillMaxSize(),
            contentPadding      = PaddingValues(top = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier              = Modifier.fillMaxWidth().then(HPAD),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text("Погода",
                        style = PrimaryTheme.typography.temperatureDisplay.copy(fontSize = 32.sp),
                        color = PrimaryTheme.colors.textPrimary)
                    IconButton(onClick = { showSettings = true }) {   // ← открываем шит
                        Icon(Icons.Default.MoreHoriz, null,
                            tint = PrimaryTheme.colors.textPrimary)
                    }
                }
            }

            item {
                CityCard(
                    name              = "Текущее место",
                    summary           = null,
                    tempUnit          = tempUnit,
                    isCurrentLocation = true,
                    onClick           = onCurrentLocation,
                    onDelete          = null
                )
            }

            items(cities, key = { it.id }) { city ->
                CityCard(
                    name     = city.displayName,
                    summary  = summaries[city.id],
                    tempUnit = tempUnit,
                    onClick  = { onCityClick(city) },
                    onDelete = { vm.delete(city) }
                )
            }
        }

        AnimatedVisibility(
            visible  = searching,
            enter    = slideInVertically { it },
            exit     = slideOutVertically { it },
            modifier = Modifier.fillMaxSize()
        ) {
            SearchSheet(
                query     = query,
                onQuery   = { q -> query = q; vm.search(q) },
                state     = searchState,
                onSave    = { city ->
                    vm.save(city); searching = false; query = ""; vm.resetSearch()
                },
                onDismiss = { searching = false; query = ""; vm.resetSearch() }
            )
        }

        if (!searching) {
            SearchBarStatic(
                onFocus  = { searching = true },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .then(HPAD)
                    .padding(bottom = 16.dp)
            )
        }

        if (showSettings) {
            SettingsSheet(
                currentUnit = tempUnit,
                onUnitSelected = { vm.setTempUnit(it) },
                onDismiss   = { showSettings = false }
            )
        }
    }
}


@Composable
private fun SearchBarStatic(
    onFocus: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(PrimaryTheme.colors.cardGlass, PrimaryTheme.shapes.searchBarShape)
            .clickable(onClick = onFocus)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Search, null,
            Modifier.size(18.dp), PrimaryTheme.colors.textHint
        )
        Spacer(Modifier.width(8.dp))
        Text(
            stringResource(R.string.search_hint),
            style = PrimaryTheme.typography.cardTextStyle,
            color = PrimaryTheme.colors.textHint,
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.Default.Mic, null,
            Modifier.size(18.dp), PrimaryTheme.colors.textHint
        )
    }
}


@Composable
private fun SearchSheet(
    query: String,
    onQuery: (String) -> Unit,
    state: SearchUiState,
    onSave: (CityLocation) -> Unit,
    onDismiss: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryTheme.colors.backgroundDark)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(PrimaryTheme.shapes.searchBarShape)
                    .background(PrimaryTheme.colors.cardGlass, PrimaryTheme.shapes.searchBarShape)
                    .padding(horizontal = 14.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Search, null,
                    Modifier.size(18.dp), PrimaryTheme.colors.textHint
                )
                Spacer(Modifier.width(8.dp))
                Box(Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            stringResource(R.string.search_hint),
                            style = PrimaryTheme.typography.cardTextStyle,
                            color = PrimaryTheme.colors.textHint
                        )
                    }
                    BasicTextField(
                        value = query,
                        onValueChange = onQuery,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        textStyle = PrimaryTheme.typography.cardTextStyle.copy(
                            color = PrimaryTheme.colors.textPrimary
                        ),
                        cursorBrush = SolidColor(PrimaryTheme.colors.textPrimary),
                        singleLine = true
                    )
                }
                if (query.isNotEmpty()) {
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        Icons.Default.Close, null,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { onQuery("") },
                        tint = PrimaryTheme.colors.textHint
                    )
                }
            }

            Spacer(Modifier.width(10.dp))
            Text(
                stringResource(R.string.search_cancel),
                style = PrimaryTheme.typography.cardTextStyle,
                color = PrimaryTheme.colors.textPrimary,
                modifier = Modifier.clickable(onClick = onDismiss)
            )
        }

        HorizontalDivider(
            color = PrimaryTheme.colors.textHint.copy(alpha = 0.15f),
            thickness = 0.5.dp
        )

        when (state) {
            SearchUiState.Idle -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Search, null,
                        Modifier.size(48.dp), PrimaryTheme.colors.textHint
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        stringResource(R.string.search_idle_hint),
                        style = PrimaryTheme.typography.conditionLabel,
                        color = PrimaryTheme.colors.textHint
                    )
                }
            }

            SearchUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryTheme.colors.textPrimary)
            }

            SearchUiState.Empty -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.search_empty),
                    style = PrimaryTheme.typography.conditionLabel,
                    color = PrimaryTheme.colors.textSecondary
                )
            }

            is SearchUiState.Error -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.WifiOff, null,
                        Modifier.size(40.dp), PrimaryTheme.colors.textHint
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        stringResource(R.string.search_error_title),
                        style = PrimaryTheme.typography.conditionLabel,
                        color = PrimaryTheme.colors.textSecondary
                    )
                    Text(
                        stringResource(R.string.search_error_subtitle),
                        style = PrimaryTheme.typography.detailCardSubtext,
                        color = PrimaryTheme.colors.textHint
                    )
                }
            }

            is SearchUiState.Results -> LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(state.data) { city ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSave(city) }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                city.displayName,
                                style = PrimaryTheme.typography.forecastDayName,
                                color = PrimaryTheme.colors.textPrimary
                            )
                            Text(
                                "${String.format("%.4f", city.lat)}, ${
                                    String.format(
                                        "%.4f",
                                        city.lon
                                    )
                                }",
                                style = PrimaryTheme.typography.detailCardSubtext,
                                color = PrimaryTheme.colors.textHint
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            Icons.Default.Add, null,
                            tint = PrimaryTheme.colors.textSecondary
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = PrimaryTheme.colors.textHint.copy(alpha = 0.12f),
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSheet(
    currentUnit   : TemperatureUnit,
    onUnitSelected: (TemperatureUnit) -> Unit,
    onDismiss     : () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryTheme.colors.backgroundDark.copy(alpha = 0.4f))
            .clickable(
                indication            = null,
                interactionSource     = remember { MutableInteractionSource() },
                onClick               = onDismiss
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    PrimaryTheme.colors.backgroundMedium,
                    RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .navigationBarsPadding()
                .clickable(
                    indication        = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick           = {}   // перехватываем клик, чтобы не закрыть
                )
        ) {
            // Handle
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(PrimaryTheme.colors.textHint, RoundedCornerShape(2.dp))
            )

            Spacer(Modifier.height(8.dp))

            SettingsItem(
                label    = "Градусы Цельсия",
                prefix   = "°C",
                selected = currentUnit == TemperatureUnit.CELSIUS,
                onClick  = { onUnitSelected(TemperatureUnit.CELSIUS); onDismiss() }
            )

            HorizontalDivider(
                modifier  = Modifier.padding(horizontal = 16.dp),
                color     = PrimaryTheme.colors.textHint.copy(alpha = 0.15f),
                thickness = 0.5.dp
            )

            SettingsItem(
                label    = "Градусы Фаренгейта",
                prefix   = "°F",
                selected = currentUnit == TemperatureUnit.FAHRENHEIT,
                onClick  = { onUnitSelected(TemperatureUnit.FAHRENHEIT); onDismiss() }
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SettingsItem(
    label   : String,
    prefix  : String,
    selected: Boolean,
    onClick : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(prefix,
                style = PrimaryTheme.typography.cardTextStyle,
                color = PrimaryTheme.colors.textHint)
            Text(label,
                style = PrimaryTheme.typography.cardTextStyle,
                color = PrimaryTheme.colors.textPrimary)
        }
        if (selected) {
            Icon(
                Icons.Default.Check, null,
                Modifier.size(18.dp),
                tint = PrimaryTheme.colors.textPrimary
            )
        }
    }
}


@Composable
private fun CityCard(
    name: String,
    summary: CitySummary?,
    tempUnit         : TemperatureUnit,
    isCurrentLocation: Boolean = false,
    onClick: () -> Unit,
    onDelete: (() -> Unit)?
) {
    val c = PrimaryTheme.colors
    val ty = PrimaryTheme.typography


    val background = remember(summary?.timezoneOffsetSec, summary?.description) {
        val hour = summary?.let { localHourForCity(it.timezoneOffsetSec) }
        resolveBackground(false, hour)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(HPAD)
            .clip(PrimaryTheme.shapes.detailCardShape)
            .clickable(onClick = onClick)
    ) {

        Image(
            painter = painterResource(background.toDrawableRes()),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(c.cardGlass.copy(alpha = 0.28f))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(end = if (onDelete != null) 28.dp else 0.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                Column(Modifier.weight(1f)) {

                    if (isCurrentLocation) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.MyLocation,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = c.textHint
                            )

                            Spacer(Modifier.width(4.dp))

                            Text(
                                stringResource(R.string.current_location_label),
                                style = ty.detailCardLabel,
                                color = c.textHint
                            )
                        }
                    }

                    Text(
                        text = name,
                        style = ty.cityTitle,
                        color = c.textPrimary
                    )

                    summary?.let {
                        Text(
                            it.updatedAt.formatAsTime(),
                            style = ty.detailCardSubtext,
                            color = c.textSecondary
                        )
                    }
                }

                summary?.let {
                    Text(
                        "${it.currentTemp.formatTemp(tempUnit)}°",
                        style = ty.temperatureDisplay.copy(fontSize = 48.sp),
                        color = c.textPrimary
                    )
                }
            }

            summary?.let {
                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        it.description.replaceFirstChar { ch -> ch.uppercase() },
                        style = ty.conditionLabel,
                        color = c.textSecondary
                    )

                    Text(
                        stringResource(
                            R.string.max_min_label,
                            it.maxTemp.formatTemp(tempUnit),
                            it.minTemp.formatTemp(tempUnit),
                        ),
                        style = ty.minMaxLabel,
                        color = c.textSecondary
                    )
                }
            }
        }

        onDelete?.let {
            IconButton(
                onClick = it,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(32.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = null,
                    tint = c.textHint
                )
            }
        }
    }
}