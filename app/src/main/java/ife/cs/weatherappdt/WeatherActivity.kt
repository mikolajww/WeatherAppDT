package ife.cs.weatherappdt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ife.cs.weatherappdt.api.OpenWeatherApiService
import ife.cs.weatherappdt.api.responses.WeatherResponse
import ife.cs.weatherappdt.fragment.WeatherFragment
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.coroutines.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class WeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        if(verifyAvailableNetwork(this@WeatherActivity)) {
        GlobalScope.launch{

                OpenWeatherApiService.fetchCurrentWeather("Lodz", "pl", this@WeatherActivity).also { println(it) }
            }

        }
        else {
            Toast.makeText(this@WeatherActivity, "No internet connection, fetching previously saved data.", Toast.LENGTH_SHORT).show()
            OpenWeatherApiService.readCurrentWeather("Lodz", "pl", this@WeatherActivity)
            //read from file
        }
    }
}
