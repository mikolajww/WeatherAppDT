package ife.cs.weatherappdt.fragment


import android.content.Context
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
import kotlinx.coroutines.*

class WeatherFragment : Fragment() {

    private lateinit var cityName: String
    private lateinit var countryCode: String
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(verifyAvailableNetwork(requireParentFragment().requireActivity())) {
            ioScope.launch {
                launchWithLoading {
                    val response = OpenWeatherApiService.fetchCurrentWeather(cityName, countryCode, requireParentFragment().requireActivity())
                    parseWeatherResponse(response)
                }
            }
        }
        else {
            Toast.makeText(requireParentFragment().requireActivity(), "No internet connection, fetching previously saved data.", Toast.LENGTH_SHORT).show()
            var response = OpenWeatherApiService.readCurrentWeather(cityName, countryCode, requireParentFragment().requireActivity())
            parseWeatherResponse(response)
            //read from file
        }
    }

    private fun parseWeatherResponse(weatherResponse: WeatherResponse?) {
        if (weatherResponse == null) {
            Toast.makeText(activity, "Failed to obtain data!", Toast.LENGTH_SHORT).show()
            return
        }
        with(weatherResponse) {
            uiScope.launch {
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
        uiScope.launch { loading_progress_bar.visibility = View.VISIBLE }
        f.invoke()
        uiScope.launch { loading_progress_bar.visibility = View.GONE }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getString("CITYNAME")?.let {
            cityName = it
        }
        arguments?.getString("COUNTRYCODE")?.let {
            countryCode = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {

        @JvmStatic
        fun newInstance(cityName: String, countryCode: String) = WeatherFragment().apply {
            arguments = Bundle().apply {
                putString("CITYNAME", cityName)
                putString("COUNTRYCODE", countryCode)
            }
        }
    }
}
