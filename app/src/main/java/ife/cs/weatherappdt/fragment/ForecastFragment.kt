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
import ife.cs.weatherappdt.api.responses.ForecastResponse
import ife.cs.weatherappdt.verifyAvailableNetwork
import kotlinx.android.synthetic.main.fragment_forecast.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ForecastFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private lateinit var cityName: String
    private lateinit var countryCode: String
    private lateinit var localContext: Context
    private var cachedResponse: ForecastResponse? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }


    override fun onResume() {
        super.onResume()
        job = Job()
        if (cachedResponse != null) {
            println("Using cached response for $cityName $countryCode")
            parseForecastResponse(cachedResponse)
            return
        }
        println("Resumend $cityName $countryCode")
        displayForecast()
    }

    fun displayForecast() {
        if (verifyAvailableNetwork(requireParentFragment().requireActivity())) {
            launch {
                println("Loading $cityName $countryCode")
                launchWithLoading {
                    cachedResponse = withContext(Dispatchers.IO) {
                        OpenWeatherApiService.fetch5DayForecast(
                            cityName,
                            countryCode,
                            requireParentFragment().requireActivity()
                        )
                    }
                    parseForecastResponse(cachedResponse)
                }
            }
        } else {
            Toast.makeText(
                requireParentFragment().requireActivity(),
                "No internet connection, fetching previously saved data.",
                Toast.LENGTH_SHORT
            ).show()
            launch {
                launchWithLoading {
                    var response = async(Dispatchers.IO) {
                        OpenWeatherApiService.read5DayForecast(
                            cityName,
                            countryCode,
                            requireParentFragment().requireActivity()
                        )
                    }
                    parseForecastResponse(response.await())
                }
            }
            //read from file
        }
    }

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    private fun parseForecastResponse(forecastResponse: ForecastResponse?) {
        println("Processing $cityName $countryCode")
        if (forecastResponse == null) {
            Toast.makeText(activity, "Failed to obtain data!", Toast.LENGTH_SHORT).show()
            return
        }
        with(forecastResponse) {
            val parsedForecastList = OpenWeatherApiService.getParsedList(list)
                temp1.text = parsedForecastList.get(0).main?.temp.toString() + OpenWeatherApiService.getUnitSuffix()
                desc1.text = parsedForecastList.get(0).weather?.get(0)?.description?.toUpperCase()
                day1.text = OpenWeatherApiService.getWeekdayName(parsedForecastList.get(0).dt_txt.toString())
                Glide.with(icon1)
                    .load("http://openweathermap.org/img/w/${parsedForecastList.get(0).weather?.get(0)?.icon ?: "01d"}.png")
                    .into(icon1)

                temp2.text = parsedForecastList.get(1).main?.temp.toString() + OpenWeatherApiService.getUnitSuffix()
                desc2.text = parsedForecastList.get(1).weather?.get(0)?.description?.toUpperCase()
                day2.text = OpenWeatherApiService.getWeekdayName(parsedForecastList.get(1).dt_txt.toString())
                Glide.with(icon2)
                    .load("http://openweathermap.org/img/w/${parsedForecastList.get(1).weather?.get(0)?.icon ?: "01d"}.png")
                    .into(icon2)

                temp3.text = parsedForecastList.get(2).main?.temp.toString() + OpenWeatherApiService.getUnitSuffix()
                desc3.text = parsedForecastList.get(2).weather?.get(0)?.description?.toUpperCase()
                day3.text = OpenWeatherApiService.getWeekdayName(parsedForecastList.get(2).dt_txt.toString())
                Glide.with(icon3)
                    .load("http://openweathermap.org/img/w/${parsedForecastList.get(2).weather?.get(0)?.icon ?: "01d"}.png")
                    .into(icon3)

                temp4.text = parsedForecastList.get(3).main?.temp.toString() + OpenWeatherApiService.getUnitSuffix()
                desc4.text = parsedForecastList.get(3).weather?.get(0)?.description?.toUpperCase()
                day4.text = OpenWeatherApiService.getWeekdayName(parsedForecastList.get(3).dt_txt.toString())
                Glide.with(icon4)
                    .load("http://openweathermap.org/img/w/${parsedForecastList.get(3).weather?.get(0)?.icon ?: "01d"}.png")
                    .into(icon4)

                println("Done processing $cityName $countryCode")

        }
    }

    private suspend fun launchWithLoading(f:suspend () -> Unit) {
        if(view == null) {
            println("View is null!")
            return
        }
        loading_progress_bar1.visibility = View.VISIBLE
        f.invoke()
        loading_progress_bar1.visibility = View.GONE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        localContext = context
        arguments?.getString("CITYNAME")?.let {
            cityName = it
        }
        arguments?.getString("COUNTRYCODE")?.let {
            countryCode = it
        }
    }

    @ExperimentalCoroutinesApi
    override fun onPause() {
        super.onPause()
        println("Cancelled $cityName $countryCode")
        job.cancel()
    }
    companion object {

        @JvmStatic
        fun newInstance(cityName: String, countryCode: String) = ForecastFragment().apply {
            arguments = Bundle().apply {
                putString("CITYNAME", cityName)
                putString("COUNTRYCODE", countryCode)
            }
        }
    }

}
