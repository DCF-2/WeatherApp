package com.weatherapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.weatherapp.model.Weather
import com.weatherapp.viewmodel.MainViewModel

@Composable
fun MapPage(viewModel: MainViewModel) {
    val recife = LatLng(-8.05, -34.88)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(recife, 10f)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                viewModel.addCity(latLng)
            }
        ) {
            // ADAPTAÇÃO FIEL AO ROTEIRO (Passo 4)
            // Coleta os estados dentro do GoogleMap
            val cities = viewModel.cities.collectAsStateWithLifecycle(initialValue = emptyMap()).value
            val weathers = viewModel.weather.collectAsStateWithLifecycle(initialValue = emptyMap()).value

            cities.values.forEach { city ->
                if (city.location != null) {
                    val weather = weathers[city.name] ?: Weather.LOADING

                    // Carrega o clima quando o marcador aparece
                    LaunchedEffect(city.name) {
                        viewModel.loadWeather(city.name)
                    }

                    // Carrega o bitmap quando o clima é atualizado
                    LaunchedEffect(weather) {
                        viewModel.loadBitmap(city.name)
                    }

                    val icon = if (weather.bitmap != null) {
                        BitmapDescriptorFactory.fromBitmap(weather.bitmap!!)
                    } else {
                        BitmapDescriptorFactory.defaultMarker()
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