package ife.cs.weatherappdt

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import ife.cs.weatherappdt.api.OpenWeatherApiService
import ife.cs.weatherappdt.api.responses.ForecastResponse
import ife.cs.weatherappdt.api.responses.WeatherResponse
import kotlinx.android.synthetic.main.activity_test_weather.*
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
            fetchCurrentWeatherJson()
        }

        button2.setOnClickListener {
            fetch5DayForecastJson()
        }

        button3.setOnClickListener {

        }
    }

    fun fetchCurrentWeatherJson() {
        println("Fetching current weather JSON...")
        OpenWeatherApiService.fetchCurrentWeather("Lodz", "pl", object : Callback {
            val fileName = "CurrentWeather"
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()!!.string()

                println(body)
                /*
                val parser = Parser.default()
                jsonObject = parser.parse(StringBuilder(body)) as JsonObject
                println(jsonObject)*/
                weatherResponseObject = Klaxon().parse<WeatherResponse>(body)
                createFile(fileName, body)
                println(weatherResponseObject)
            }

            override fun onFailure(call: Call, e: IOException) {
                println(readFromFile(fileName))
                println("Failed to fetch JSON")
            }
        })
    }

    fun fetch5DayForecastJson() {
        println("Fetching 5 day forecast JSON...")
        OpenWeatherApiService.fetch5DayForecast("Lodz", "pl", object : Callback {
            val fileName = "5DayForecast"
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()!!.string()
                println(body)
                forecastResponseObject = Klaxon().parse<ForecastResponse>(body)
                createFile(fileName, body)
                println(forecastResponseObject)
                println(forecastResponseObject?.list?.get(0)?.main?.temp?.KToC())
            }

            override fun onFailure(call: Call, e: IOException) {
                println(readFromFile(fileName))
                println("Failed to fetch JSON")
            }
        })
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