package com.example.weatherappdanial.base_ui_utils.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weatherappdanial.base_ui_utils.background.resolveBackground
import com.example.weatherappdanial.base_ui_utils.background.toDrawableRes
import com.example.weatherappdanial.ui.base_theme.PrimaryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherErrorScreen(
    icon: ImageVector,
    title: String,
    description: String,
    actionLabel: String,
    onAction: () -> Unit,
    onRefresh: () -> Unit,
) {

    val pullState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        state = pullState,
        modifier = Modifier.fillMaxSize(),
        indicator = {
            PullToRefreshDefaults.Indicator(
                isRefreshing = isRefreshing,
                state = pullState,
                modifier = Modifier.align(Alignment.TopCenter),
                color = PrimaryTheme.colors.textPrimary,
                containerColor = PrimaryTheme.colors.cardGlass
            )
        }
    ) {

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
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.25f))
            )

            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = PrimaryTheme.colors.textSecondary
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = title,
                        style = PrimaryTheme.typography.cityTitle,
                        color = PrimaryTheme.colors.textPrimary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = description,
                        style = PrimaryTheme.typography.conditionLabel,
                        color = PrimaryTheme.colors.textSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Потяните вниз для обновления",
                        style = PrimaryTheme.typography.detailCardLabel,
                        color = PrimaryTheme.colors.textHint
                    )

                    Spacer(Modifier.height(32.dp))

                    Button(
                        onClick = onAction,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryTheme.colors.cardGlass,
                            contentColor = PrimaryTheme.colors.textPrimary
                        ),
                        shape = PrimaryTheme.shapes.buttonsShape,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(48.dp)
                    ) {
                        Text(
                            text = actionLabel,
                            style = PrimaryTheme.typography.cardTextStyle
                        )
                    }
                }

            }
        }
    }
}