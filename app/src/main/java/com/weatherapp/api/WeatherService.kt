package com.weatherapp.api

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherService(private val context: Context) {

    private val imageLoader = ImageLoader.Builder(context)
        .allowHardware(false)
        .build()

    private var weatherAPI: WeatherServiceAPI

    init {
        val retrofitAPI = Retrofit.Builder()
            .baseUrl(WeatherServiceAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        weatherAPI = retrofitAPI.create(WeatherServiceAPI::class.java)
    }

    // Função auxiliar privada e síncrona para buscar localização
    private fun search(query: String): APILocation? {
        val call: Call<List<APILocation>?> = weatherAPI.search(query)
        val apiLoc = call.execute().body()
        return if (!apiLoc.isNullOrEmpty()) apiLoc[0] else null
    }

    // Transforma lat/lng em nome da cidade
    suspend fun getName(lat: Double, lng: Double): String? = withContext(Dispatchers.IO) {
        search("$lat,$lng")?.name
    }

    // Transforma nome da cidade em LatLng
    suspend fun getLocation(name: String): LatLng? = withContext(Dispatchers.IO) {
        val location = search(name)
        if (location?.lat != null && location.lon != null) {
            LatLng(location.lat!!, location.lon!!)
        } else {
            null
        }
    }

    // Busca o clima atual
    suspend fun getWeather(name: String): APICurrentWeather? = withContext(Dispatchers.IO) {
        val call: Call<APICurrentWeather?> = weatherAPI.weather(name)
        call.execute().body()
    }

    // Busca a previsão do tempo
    suspend fun getForecast(name: String): APIWeatherForecast? = withContext(Dispatchers.IO) {
        val call: Call<APIWeatherForecast?> = weatherAPI.forecast(name)
        call.execute().body()
    }

    // Baixa a imagem (ícone do clima)
    suspend fun getBitmap(imgUrl: String): Bitmap? = withContext(Dispatchers.IO) {
        val request = ImageRequest.Builder(context)
            .data(imgUrl)
            .allowHardware(false)
            .build()
        val response = imageLoader.execute(request)
        response.drawable?.toBitmap()
    }
}