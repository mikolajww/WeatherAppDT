package ife.cs.weatherappdt

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_test_weather.*
import okhttp3.*
import java.io.IOException

class TestWeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_weather)

        button.setOnClickListener{
            fetchCurrentWeatherJson()
        }

        button2.setOnClickListener{
            fetch5DayForecastJson()
        }
    }

    fun fetchCurrentWeatherJson() {
        println("Fetching current weather JSON...")
        val url = "http://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=8835e5a77fa77f31382ae1778b90042d"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                println(body)
                val filename = "myfile"
                openFileOutput(filename, Context.MODE_PRIVATE).use {
                    if (body != null) {
                        it.write(body.toByteArray())
                    }
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to fetch JSON")
            }
        })
    }

    fun fetch5DayForecastJson() {
        println("Fetching 5 day forecast JSON...")
        val url = "https://api.openweathermap.org/data/2.5/forecast?q=Lodz,pl&mode=json&appid=8835e5a77fa77f31382ae1778b90042d"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                println(body)
                val filename = "myfile"
                openFileOutput(filename, Context.MODE_PRIVATE).use {
                    if (body != null) {
                        it.write(body.toByteArray())
                    }
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to fetch JSON")
            }
        })
    }
}
