package ife.cs.weatherappdt

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import ife.cs.weatherappdt.api.OpenWeatherApiService
import ife.cs.weatherappdt.api.responses.ForecastResponse
import ife.cs.weatherappdt.api.responses.WeatherResponse
import kotlinx.android.synthetic.main.activity_test_weather.*
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.io.BufferedReader
import java.io.InputStreamReader



class TestWeatherActivity : AppCompatActivity() {

    var jsonObject: JsonObject = JsonObject()
    var weatherResponseObject: WeatherResponse? = null
    var forecastResponseObject: ForecastResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_weather)

        button.setOnClickListener {
            GlobalScope.launch(Dispatchers.Default, CoroutineStart.DEFAULT, null, {
                fetchCurrentWeatherJson()
            })
        }

        button2.setOnClickListener {
            GlobalScope.launch(Dispatchers.Default, CoroutineStart.DEFAULT, null, {
                fetch5DayForecastJson()
            })
        }

        button3.setOnClickListener {
            startActivity(Intent(this, WeatherActivity::class.java))
        }
    }

    suspend fun fetchCurrentWeatherJson() {
        println("Fetching current weather JSON...")
        val response = OpenWeatherApiService.fetchCurrentWeather("Lodz", "pl")
        println(response)
    }

    suspend fun fetch5DayForecastJson() {
        println("Fetching 5 day forecast JSON...")
        val response = OpenWeatherApiService.fetch5DayForecast("Lodz", "pl")
        println(response)
    }

    fun createFile(fileName: String?, body: String?) {
        openFileOutput(fileName, Context.MODE_PRIVATE).use {
            if (body != null) {
                it.write(body.toByteArray())
            }
        }
    }

    fun readFromFile(fileName: String?): String {
        val ctx = applicationContext
        val fileInputStream = ctx.openFileInput(fileName)
        val inputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader = BufferedReader(inputStreamReader)

        return bufferedReader.readLine()
    }
}