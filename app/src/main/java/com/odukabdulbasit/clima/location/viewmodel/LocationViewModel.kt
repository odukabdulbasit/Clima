package com.odukabdulbasit.clima.location.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odukabdulbasit.clima.service.Constant
import com.odukabdulbasit.clima.service.WeatherApi
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LocationViewModel : ViewModel() {


    private val _temperature = MutableLiveData<String>()
    val temperature: LiveData<String> = _temperature

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isUploading = MutableLiveData<Boolean>()
    val isUploading: LiveData<Boolean> = _isUploading


    fun getMyLocationWeather(lat: Double, lon: Double) {

        updateIsUploading()
        viewModelScope.launch {
            try {
                val result = WeatherApi.retrofitService.getLocationWeather(
                    lat.toString(),
                    lon.toString(),
                    Constant.apiKey,
                    "metric"
                )
                val temperature = result.main?.temp

                updateIsUploading()
                _temperature.value = "${temperature?.toInt()}°"
                _message.value = getMessage(temperature?.toInt()!!)
                Log.i("Temperature", "$temperature")
                Log.i("result", "$result")
            } catch (e: HttpException) {
                Log.i("Weather Error", e.localizedMessage)
            }
        }
    }

    fun getWeatherByCityName(cityName: String?) {
        updateIsUploading()
        viewModelScope.launch {
            try {
                val result =
                    WeatherApi.retrofitService.getCityWeather(cityName!!, Constant.apiKey, "metric")
                val temperature = result.main?.temp

                updateIsUploading()
                _temperature.value =
                    "${temperature?.toInt()}° ${getWeatherIcon(temperature!!.toInt())}"
                _message.value = getMessage(temperature.toInt())

                Log.e("Temperature", "$temperature")
                Log.e("Result", "$result")
            } catch (e: HttpException) {
                Log.e("Weather Error", e.localizedMessage)
            }
        }
    }


    private fun getWeatherIcon(condition: Int): String {
        when {
            condition < 300 -> {
                return "🌩"
            }
            condition < 400 -> {
                return "🌧"
            }
            condition < 600 -> {
                return "☔️"
            }
            condition < 700 -> {
                return "☃️"
            }
            condition < 800 -> {
                return "🌫"
            }
            condition == 800 -> {
                return "☀️"
            }
            condition <= 804 -> {
                return "☁️"
            }
            else -> {
                return "🤷‍"
            }
        }
    }

    private fun getMessage(temp: Int): String {
        return when {
            temp > 25 -> {
                "It\'s 🍦 time"
            }
            temp > 20 -> {
                "Time for shorts and 👕"
            }
            temp < 10 -> {
                "You\'ll need 🧣 and 🧤"
            }
            else -> {
                "Bring a 🧥 just in case"
            }
        }
    }

    private fun updateIsUploading() {
        _isUploading.value = _isUploading.value != true
    }

}