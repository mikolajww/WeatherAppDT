package ife.cs.weatherappdt.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ife.cs.weatherappdt.R
import ife.cs.weatherappdt.api.OpenWeatherApiService
import ife.cs.weatherappdt.api.responses.WeatherResponse
import ife.cs.weatherappdt.verifyAvailableNetwork
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class WeatherFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private lateinit var cityName: String
    private lateinit var countryCode: String
    private var shouldFetch: Boolean = true
    private var cachedResponse: WeatherResponse? = null

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onResume() {
        super.onResume()
        job = Job()
        println("Resumend $cityName $countryCode")
        if (cachedResponse != null) {
            println("Using cached response for $cityName $countryCode")
            parseWeatherResponse(cachedResponse)
            return
        }
        if(verifyAvailableNetwork(requireParentFragment().requireActivity())) {
            launch {
                launchWithLoading {
                    cachedResponse = withContext(Dispatchers.IO) {
                        OpenWeatherApiService.fetchCurrentWeather(
                            cityName,
                            countryCode,
                            requireParentFragment().requireActivity()
                        )
                    }
                    parseWeatherResponse(cachedResponse)
                }
            }
        }
        else {
            launch {
                Toast.makeText(requireParentFragment().requireActivity(), "No internet connection, fetching previously saved data.", Toast.LENGTH_SHORT).show()
                val response = async(Dispatchers.IO) { OpenWeatherApiService.readCurrentWeather(cityName, countryCode, requireParentFragment().requireActivity()) }
                parseWeatherResponse(response.await())
                //read from file
            }
        }
    }

    private fun parseWeatherResponse(weatherResponse: WeatherResponse?) {
        if (weatherResponse == null) {
            Toast.makeText(activity, "Failed to obtain data!", Toast.LENGTH_SHORT).show()
            return
        }
        with(weatherResponse) {
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
    private suspend fun launchWithLoading(f:suspend () -> Unit) {
        if(view == null) {
            println("View is null!")
            return
        }
        loading_progress_bar.visibility = View.VISIBLE
        f.invoke()
        loading_progress_bar.visibility = View.GONE
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
    }

    @ExperimentalCoroutinesApi
    override fun onPause() {
        super.onPause()
        println("Cancelled $cityName $countryCode")
        cancel()
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
