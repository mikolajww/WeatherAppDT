package ife.cs.weatherappdt.api

import com.beust.klaxon.Klaxon
import ife.cs.weatherappdt.api.responses.ForecastResponse
import ife.cs.weatherappdt.api.responses.WeatherResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun Call.await(): Response {
    return suspendCancellableCoroutine { cont ->
        cont.invokeOnCancellation {
            cancel()
        }
        enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (!cont.isCancelled) {
                    cont.resumeWithException(e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (!cont.isCancelled) {
                    cont.resume(response)
                }
            }
        })
    }
}

suspend fun OkHttpClient.execute(request: Request): Response {
    val call = this.newCall(request)
    return call.await()
}


object OpenWeatherApiService {
    private val APPID = "8835e5a77fa77f31382ae1778b90042d"
    private val currentWeatherUrl = "http://api.openweathermap.org/data/2.5/weather?q=**CITY**,**COUNTRY**&APPID=$APPID"
    private val forecast5DayUrl = "https://api.openweathermap.org/data/2.5/forecast?q=**CITY**,**COUNTRY**&mode=json&appid=$APPID"

    private val client = OkHttpClient()

    suspend fun fetchCurrentWeather(city:String, country:String): WeatherResponse? {
        val url = currentWeatherUrl.replace("**CITY**", city).replace("**COUNTRY**", country)
        val request = Request.Builder().url(url).build()
        val response = client.execute(request)
        val body = response.body()?.string() ?: return null
        return Klaxon().parse<WeatherResponse>(body)
    }

    suspend fun fetch5DayForecast(city: String, country: String): ForecastResponse? {
        val url = forecast5DayUrl.replace("**CITY**", city).replace("**COUNTRY**", country)
        val request = Request.Builder().url(url).build()
        val response = client.execute(request)
        val body = response.body()?.string() ?: return null
        return Klaxon().parse<ForecastResponse>(body)
    }
}