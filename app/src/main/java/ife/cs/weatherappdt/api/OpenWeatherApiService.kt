package ife.cs.weatherappdt.api

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import com.beust.klaxon.Klaxon
import ife.cs.weatherappdt.MainActivity
import ife.cs.weatherappdt.api.responses.ForecastResponse
import ife.cs.weatherappdt.api.responses.WeatherResponse
import kotlinx.coroutines.*
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import okhttp3.*
import java.io.IOException
import java.io.StringReader
import java.time.LocalDateTime
import java.util.*
import java.io.*
import java.text.SimpleDateFormat
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


@UnstableDefault
object OpenWeatherApiService {
    private val APPID = "8835e5a77fa77f31382ae1778b90042d"
    private val currentWeatherUrl = "http://api.openweathermap.org/data/2.5/weather?q=**CITY**,**COUNTRY**&APPID=$APPID"
    private val forecast5DayUrl = "https://api.openweathermap.org/data/2.5/forecast?q=**CITY**,**COUNTRY**&mode=json&appid=$APPID"

    private val client = OkHttpClient()

    enum class Units { C, F, K }
    var unit: Units = OpenWeatherApiService.Units.C

    suspend fun fetchCurrentWeather(city:String, country:String, context: Context): WeatherResponse? {
            val url = currentWeatherUrl.replace("**CITY**", city)
                .replace("**COUNTRY**", country)
                .setUnit(unit)
            val request = Request.Builder().url(url).build()
            val response = client.execute(request)
            val body = response.body()?.string() ?: return null
            //val obj = Klaxon().parseJsonObject(StringReader(body))
            createFile("${city}_${country}CurrentWeather.json", body, context)
            //return Klaxon().parseFromJsonObject<WeatherResponse>(obj)
            return Json.parse(WeatherResponse.serializer(), body)
    }

    suspend fun fetch5DayForecast(city: String, country: String, context: Context): ForecastResponse? {
        val url = forecast5DayUrl.replace("**CITY**", city)
                                 .replace("**COUNTRY**", country)
                                 .setUnit(unit)
        val request = Request.Builder().url(url).build()
        val response = client.execute(request)
        val body = response.body()?.string() ?: return null
        //val obj = Klaxon().parseJsonObject(StringReader(body))
        createFile("${city}_${country}5DayForecast.json", body, context)
        //return Klaxon().parseFromJsonObject<ForecastResponse>(obj)
        return Json.parse(ForecastResponse.serializer(), body)
    }

    fun readCurrentWeather(city:String, country:String, context: Context): WeatherResponse? {
        return try {
            val body = readFromFile("${city}_${country}CurrentWeather.json", context)
            val obj = Klaxon().parseJsonObject(StringReader(body))
            Klaxon().parseFromJsonObject<WeatherResponse>(obj)
        }catch (e: FileNotFoundException) {
            null
        }
    }

    fun read5DayForecast(city: String, country: String, context: Context): ForecastResponse? {
        return try {
            val body = readFromFile("${city}_${country}5DayForecast.json", context)
            val obj = Klaxon().parseJsonObject(StringReader(body))
            Klaxon().parseFromJsonObject<ForecastResponse>(obj)
        }catch (e: FileNotFoundException) {
            null
        }
    }

    private fun String.setUnit(unit: Units): String {
        return when(unit) {
            Units.C -> "$this&units=metric"
            Units.F -> "$this&units=imperial"
            Units.K -> this
        }
    }

    fun getParsedList(forecastList: List<ForecastResponse.X>):List<ForecastResponse.X> {

        val current = Calendar.getInstance()

        val currentDay = current.get(Calendar.DAY_OF_MONTH)
        val currentMonth = current.get(Calendar.MONTH) + 1
        val currentYear = current.get(Calendar.YEAR)
        var parsedForecastList:MutableList<ForecastResponse.X> = mutableListOf()

        var daysAheadCount = 1
        var targetForecast = "$currentYear-${formatDoubleDigitDayOrMonth(currentMonth)}-${formatDoubleDigitDayOrMonth(currentDay + daysAheadCount)} 12:00:00"

        for(element in forecastList) {
//            println("Target: $targetForecast")
//            println("Considered: ${element.dt_txt}")
//            println("\n")
            if(element.dt_txt.equals(targetForecast)) {
                parsedForecastList.add(element)
                daysAheadCount++
                targetForecast = "$currentYear-${formatDoubleDigitDayOrMonth(currentMonth)}-${formatDoubleDigitDayOrMonth(currentDay + daysAheadCount)} 12:00:00"
            }
            if(daysAheadCount > 4) { break }
        }
        return parsedForecastList
    }

    fun formatDoubleDigitDayOrMonth(dayOrMonth:Int):String {
        if(dayOrMonth < 10) {
            return "0$dayOrMonth"
        }
        return "$dayOrMonth"
    }

    fun getWeekdayName(dateString:String):String {
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString)
        return SimpleDateFormat("EE").format(date)
    }

    fun getUnitSuffix(): String = when(unit) {
        OpenWeatherApiService.Units.C -> "°C"
        OpenWeatherApiService.Units.F -> "°F"
        OpenWeatherApiService.Units.K -> "K"
    }

    private fun createFile(fileName: String?, body: String?, context:Context) {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            if (body != null) {
                it.write(body.toByteArray())
            }
        }
    }
    @Throws(FileNotFoundException::class)
    private fun readFromFile(fileName: String?, context: Context): String? {
        val ctx = context.applicationContext
        val fileInputStream = ctx.openFileInput(fileName)
        val inputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        return bufferedReader.readLine()
    }
}