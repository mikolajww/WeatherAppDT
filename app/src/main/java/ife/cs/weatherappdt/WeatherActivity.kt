package ife.cs.weatherappdt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_weather.*
import okhttp3.*
import java.io.IOException

class WeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        button.setOnClickListener{
            fetchJson()
        }
    }

    fun fetchJson() {
        println("Fetching JSON...")
        val url = "http://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=8835e5a77fa77f31382ae1778b90042d"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response?.body()?.string()
                println(body)
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to fetch JSON")
            }
        })
    }
}
