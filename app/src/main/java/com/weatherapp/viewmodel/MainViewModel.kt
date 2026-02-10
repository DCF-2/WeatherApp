package com.weatherapp.viewmodel

import FBUser
import Route
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.LatLng
import com.weatherapp.model.City
import com.weatherapp.model.Forecast
import com.weatherapp.model.User
import com.weatherapp.model.Weather
import com.weatherapp.api.WeatherService
import com.weatherapp.api.toForecast
import com.weatherapp.api.toWeather
import com.weatherapp.db.fb.FBCity
import com.weatherapp.db.fb.FBDatabase
import com.weatherapp.db.fb.toFBCity
import com.weatherapp.monitor.ForecastMonitor
import com.weatherapp.repo.Repository

class MainViewModel(
    private val repo: Repository,
    private val monitor: ForecastMonitor,
    private val service: WeatherService
) : ViewModel(), Repository.Listener {

    private val _cities = mutableStateMapOf<String, City>()
    val cities: List<City>
        get() = _cities.values.toList().sortedBy { it.name }

    private val _weather = mutableStateMapOf<String, Weather>()
    private val _forecast = mutableStateMapOf<String, List<Forecast>?>()

    private var _page = mutableStateOf<Route>(Route.Home)
    var page: Route
        get() = _page.value
        set(tmp) { _page.value = tmp }

    private var _city = mutableStateOf<String?>(null)
    var city: String?
        get() = _city.value
        set(tmp) { _city.value = tmp }

    private val _user = mutableStateOf<User?>(null)
    val user: User?
        get() = _user.value

    val cityMap: Map<String, City>
        get() = _cities.toMap()

    init {
        repo.setListener(this)
    }

    fun remove(city: City) {
        repo.remove(city)
    }
    fun add(name: String, location: LatLng? = null) {
        repo.add(City(name = name, location = location))
    }
    override fun onUserLoaded(user: User) {
        _user.value = user
    }

    override fun onUserSignOut() {
        monitor.cancelAll()
    }
    override fun onCityAdded(city: City) {
        _cities[city.name] = city
        monitor.updateCity(city)
    }
    override fun onCityUpdated(city: City) {
        _cities.remove(city.name)
        _cities[city.name] = city
        monitor.updateCity(city)
    }
    override fun onCityRemoved(city: City) {
        _cities.remove(city.name)
        monitor.cancelCity(city)
    }
    fun addCity(name: String) {
        service.getLocation(name) { lat, lng ->
            if (lat != null && lng != null) {
                repo.add(City(name = name, location = LatLng(lat, lng)))
            }
        }
    }
    fun addCity(location: LatLng) {
        service.getName(location.latitude, location.longitude) { name ->
            if (name != null) {
                repo.add(City(name = name, location = location))
            }
        }
    }
    fun weather(name: String) = _weather.getOrPut(name) {
        loadWeather(name)
        Weather.LOADING
    }
    private fun loadWeather(name: String) {
        service.getWeather(name) { apiWeather ->
            apiWeather?.let {
                _weather[name] = apiWeather.toWeather()
                loadBitmap(name)
            }
        }
    }
    fun forecast(name: String) = _forecast.getOrPut(name) {
        loadForecast(name)
        emptyList()
    }
    private fun loadForecast(name: String) {
        service.getForecast(name) { apiForecast ->
            apiForecast?.let {
                _forecast[name] = apiForecast.toForecast()
            }
        }
    }

    fun loadBitmap(name: String) {
        _weather[name]?.let { weather ->
            service.getBitmap(weather.imgUrl) { bitmap ->
                _weather[name] = weather.copy(bitmap = bitmap)
            }
        }
    }

    fun update(city: City) {
        repo.update(city)
    }
}
class MainViewModelFactory(
    private val repo: Repository,
    private val monitor: ForecastMonitor,
    private val service: WeatherService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repo, monitor, service) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}