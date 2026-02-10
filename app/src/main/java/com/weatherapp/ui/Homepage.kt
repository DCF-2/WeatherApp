package com.weatherapp.ui

import BottomNavItem.HomeButton.icon
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
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.weatherapp.R
import com.weatherapp.viewmodel.MainViewModel


@Composable
fun HomePage(viewModel: MainViewModel) {
    Column {
        val cityName = viewModel.city

        if (cityName == null) {
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
            // Parte Superior: Detalhes da Cidade e Clima Atual
            Row {
                AsyncImage(
                    model = viewModel.weather(cityName).imgUrl,
                    modifier = Modifier.size(140.dp),
                    error = painterResource(id = R.drawable.loading),
                    contentDescription = "Imagem"
                )
                Column {
                    Spacer(modifier = Modifier.size(12.dp))

                    // Recupera o objeto City completo para verificar isMonitored
                    val currentCity = viewModel.cityMap[cityName]

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = cityName,
                            fontSize = 28.sp
                        )
                        // Só exibimos o ícone se a cidade existir no mapa
                        if (currentCity != null) {
                            Icon(
                                imageVector = if (currentCity.isMonitored)
                                    Icons.Filled.Notifications
                                else
                                    Icons.Outlined.Notifications,
                                contentDescription = "Monitorada?",
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(32.dp)
                                    .clickable {
                                        viewModel.update(currentCity.copy(isMonitored = !currentCity.isMonitored))
                                    }
                            )
                        }
                    }

                    val weather = viewModel.weather(cityName)
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        text = weather.desc,
                        fontSize = 22.sp
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(text = "Temp: " + weather.temp + "℃", fontSize = 22.sp)
                }
            }

            // Parte Inferior: Lista de Previsão (Corrigido o crash aqui)
            // Só executa se viewModel.forecast retornar uma lista válida
            viewModel.forecast(cityName)?.let { forecasts ->
                LazyColumn {
                    items(items = forecasts) { forecast ->
                        ForecastItem(
                            forecast,
                            onClick = { }
                        )
                    }
                }
            }
        }
    }
}