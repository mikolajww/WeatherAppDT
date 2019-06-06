package ife.cs.weatherappdt.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ife.cs.weatherappdt.R
import ife.cs.weatherappdt.api.OpenWeatherApiService
import ife.cs.weatherappdt.api.responses.WeatherResponse
import ife.cs.weatherappdt.verifyAvailableNetwork
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WeatherFragment : Fragment() {

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(verifyAvailableNetwork(activity as AppCompatActivity)) {
            GlobalScope.launch {
                launchWithLoading {
                    val response = OpenWeatherApiService.fetchCurrentWeather("London", "uk", activity as AppCompatActivity)
                    parseWeatherResponse(response)
                }
            }
        }
        else {
            Toast.makeText(activity, "No internet connection, fetching previously saved data.", Toast.LENGTH_SHORT).show()
            var response = OpenWeatherApiService.readCurrentWeather("London", "uk", activity as AppCompatActivity)
            parseWeatherResponse(response)
            //read from file
        }
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    private fun parseWeatherResponse(weatherResponse: WeatherResponse?) {
        if (weatherResponse == null) {
            Toast.makeText(activity, "Failed to obtain data!", Toast.LENGTH_SHORT).show()
            return
        }
        with(weatherResponse) {
            activity?.runOnUiThread {
                city_name_weather.text = name
                city_latitude.text = "Lat: ${coord?.lat.toString()}"
                city_longitude.text = "Lon: ${coord?.lon.toString()}"
                temperature_now.text = main?.temp.toString() + OpenWeatherApiService.getUnitSuffix()
                weather_description.text = weather?.get(0)?.description?.toUpperCase()
                Glide.with(current_weather_icon)
                    .load("http://openweathermap.org/img/w/${weather?.get(0)?.icon ?: "01d"}.png")
                    .into(current_weather_icon)
            }
        }


    }
    private suspend fun launchWithLoading(f:suspend () -> Unit) {
        activity?.runOnUiThread { loading_progress_bar.visibility = View.VISIBLE }
        f.invoke()
        activity?.runOnUiThread { loading_progress_bar.visibility = View.GONE }
    }
}
