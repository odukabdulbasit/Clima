package com.odukabdulbasit.clima.service

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.odukabdulbasit.clima.models.WeatherModel
import com.odukabdulbasit.clima.service.Constant.Companion.openWeatherMapURL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("/data/2.5/weather")
    suspend fun getCityWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): WeatherModel


    @GET("/data/2.5/weather")
    suspend fun getLocationWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String,
        @Query("units") units: String
    ): WeatherModel
}

object WeatherApi {

    private val builder = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()


    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(builder))
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(openWeatherMapURL)
        .build()

    val retrofitService: WeatherService by lazy {
        retrofit.create(WeatherService::class.java)
    }
}

class Constant {
    companion object {
        const val apiKey = "3fca0bf1ab6c3782e80dff4a8061d41e"
        const val openWeatherMapURL = "https://api.openweathermap.org"
    }
}