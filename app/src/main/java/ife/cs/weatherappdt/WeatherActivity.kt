package ife.cs.weatherappdt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ife.cs.weatherappdt.api.OpenWeatherApiService
import ife.cs.weatherappdt.api.responses.WeatherResponse
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class WeatherActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        GlobalScope.launch(Dispatchers.Default, CoroutineStart.DEFAULT, null, {
            OpenWeatherApiService.fetchCurrentWeather("Lodz", "pl").also { println(it) }
        })

    }


}
