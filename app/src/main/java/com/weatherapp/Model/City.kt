package com.weatherapp.Model

import androidx.lifecycle.ViewModel

data class City (
    val name : String,
    val weather: String? = null,
    val location: String? = null
)