package com.weatherapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.weatherapp.R
import com.weatherapp.viewmodel.MainViewModel

@Composable
fun HomePage(viewModel: MainViewModel) {
    Column {
        if (viewModel.city == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Blue)
                    .wrapContentSize(Alignment.Center)
            ) {
                Text(
                    text = "Selecione uma cidade!",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp
                )
            }
        } else {
            val cities = viewModel.cities.collectAsStateWithLifecycle(emptyMap()).value
            val city = cities[viewModel.city!!]

            val weather = viewModel.weather.collectAsStateWithLifecycle(emptyMap())
                .value[viewModel.city!!]

            val icon =
                if (city?.isMonitored == true) Icons.Filled.Notifications else Icons.Outlined.Notifications

            val forecasts = viewModel.forecast.collectAsStateWithLifecycle(emptyMap())
                .value[viewModel.city!!]
            // Carrega os dados quando a cidade mudar
            LaunchedEffect(viewModel.city!!) {
                viewModel.loadForecast(viewModel.city!!)
            }

            // UI
            Row {
                AsyncImage(
                    model = weather?.imgUrl,
                    modifier = Modifier.size(140.dp),
                    error = painterResource(id = R.drawable.loading),
                    contentDescription = "Imagem"
                )
                Column {
                    Spacer(modifier = Modifier.size(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = viewModel.city!!,
                            fontSize = 28.sp
                        )
                        if (city != null) {
                            Icon(
                                imageVector = icon,
                                contentDescription = "Monitorada?",
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(32.dp)
                                    .clickable {
                                        viewModel.update(city.copy(isMonitored = !city.isMonitored))
                                    }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        text = weather?.desc ?: "Carregando...",
                        fontSize = 22.sp
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(text = "Temp: ${weather?.temp}℃", fontSize = 22.sp)
                }
            }

            forecasts?.let { forecasts ->
                LazyColumn {
                    items(items = forecasts) { forecast ->
                        ForecastItem(
                            forecast,
                            onClick = { })
                    }
                }
            }
        }
    }
}