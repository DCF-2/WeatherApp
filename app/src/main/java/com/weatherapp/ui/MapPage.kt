package com.weatherapp.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.weatherapp.viewmodel.MainViewModel
import com.weatherapp.R
import com.weatherapp.model.Weather

@Composable
fun MapPage(viewModel: MainViewModel) {
    val recife = LatLng(-8.05, -34.88)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(recife, 10f)
    }

    val context = LocalContext.current

    // 1. Carrega e define um tamanho fixo para o ícone de loading (120x120)
    // O 'remember' garante que isso só aconteça uma vez
    val loadingBitmap = remember {
        val original = BitmapFactory.decodeResource(context.resources, R.drawable.loading)
        Bitmap.createScaledBitmap(original, 120, 120, false)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                viewModel.addCity(latLng)
            }
        ) {
            val cities = viewModel.cities.collectAsStateWithLifecycle(initialValue = emptyMap()).value
            val weathers = viewModel.weather.collectAsStateWithLifecycle(initialValue = emptyMap()).value

            cities.values.forEach { city ->
                if (city.location != null) {
                    val weather = weathers[city.name] ?: Weather.LOADING

                    LaunchedEffect(city.name) {
                        viewModel.loadWeather(city.name)
                    }

                    LaunchedEffect(weather) {
                        viewModel.loadBitmap(city.name)
                    }

                    // 2. Lógica para definir e redimensionar o ícone do marcador
                    val icon = remember(weather.bitmap) {
                        if (weather.bitmap != null) {
                            // Se tiver o clima, aumenta um pouco (160x160)
                            val scaled = Bitmap.createScaledBitmap(weather.bitmap!!, 160, 160, false)
                            BitmapDescriptorFactory.fromBitmap(scaled)
                        } else {
                            // Se não, usa o loading já redimensionado
                            BitmapDescriptorFactory.fromBitmap(loadingBitmap)
                        }
                    }

                    Marker(
                        state = MarkerState(position = city.location),
                        title = city.name,
                        snippet = if (weather == Weather.LOADING) "Carregando..." else "${weather.temp}ºC - ${weather.desc}",
                        icon = icon
                    )
                }
            }
        }
    }
}