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


    private val _tempature = MutableLiveData<String>()
    val tempature: LiveData<String> = _tempature

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    /*
    * Weather
    * Condition
    * City name
    * */
    fun getMyLocationWeather(){
        viewModelScope.launch{
            try {
                val result = WeatherApi.retrofitService.getLocationWeather("44.34", "10.99", Constant.apiKey, "metric")
                val tempature = result.main?.temp

                _tempature.value = "${tempature?.toInt()}°"
                _message.value = getMessage(tempature?.toInt()!!)
                Log.e("Tempature", "$tempature")
                Log.e("result", "$result")
            } catch (e: HttpException) {
                Log.e("Weather Error", e.localizedMessage)
            }
        }
    }

    fun getWeatherByCityName(cityName: String?) {
        viewModelScope.launch {
            try {
                val result = WeatherApi.retrofitService.getCityWeather("london", Constant.apiKey, "metric")
                val tempature = result.main?.temp

                _tempature.value = "${tempature?.toInt()}°"
                _message.value = getMessage(tempature?.toInt()!!)

                Log.e("Tempature", "$tempature")
                Log.e("result", "$result")
            }catch (e: HttpException){
                Log.e("Weather Error", e.localizedMessage)
            }
        }
    }


     fun getWeatherIcon(condition: Int): String{
        if (condition < 300) {
            return "🌩"
        } else if (condition < 400) {
            return "🌧"
        } else if (condition < 600) {
            return "☔️"
        } else if (condition < 700) {
            return "☃️"
        } else if (condition < 800) {
            return "🌫"
        } else if (condition == 800) {
            return "☀️"
        } else if (condition <= 804) {
            return "☁️"
        } else {
            return "🤷‍"
        }
    }

    fun getMessage(temp: Int): String {
        if (temp > 25) {
            return "It\'s 🍦 time"
        } else if (temp > 20) {
            return "Time for shorts and 👕"
        } else if (temp < 10) {
            return "You\'ll need 🧣 and 🧤"
        } else {
            return "Bring a 🧥 just in case"
        }
    }

}