package ife.cs.weatherappdt.api

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

object OpenWeatherApiService {
    private val APPID = "8835e5a77fa77f31382ae1778b90042d"
    private val currentWeatherUrl = "http://api.openweathermap.org/data/2.5/weather?q=**CITY**,**COUNTRY**&APPID=$APPID"
    private val forecast5DayUrl = "https://api.openweathermap.org/data/2.5/forecast?q=**CITY**,**COUNTRY**&mode=json&appid=$APPID"

    private val client = OkHttpClient()

    fun fetchCurrentWeather(city:String, country:String, callback: Callback) {
        val url = currentWeatherUrl.replace("**CITY**", city).replace("**COUNTRY**", country)
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(callback)
    }

    fun fetch5DayForecast(city: String, country: String, callback: Callback) {
        val url = forecast5DayUrl.replace("**CITY**", city).replace("**COUNTRY**", country)
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(callback)
    }
}