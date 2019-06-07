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
import ife.cs.weatherappdt.api.responses.ForecastResponse
import ife.cs.weatherappdt.verifyAvailableNetwork
import kotlinx.android.synthetic.main.fragment_forecast.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ForecastFragment : Fragment() {

    private lateinit var cityName: String
    private lateinit var countryCode: String
    private lateinit var localContext: Context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(verifyAvailableNetwork(requireParentFragment().requireActivity())) {
            GlobalScope.launch {
                launchWithLoading {
                    val response = OpenWeatherApiService.fetch5DayForecast(cityName, countryCode, requireParentFragment().requireActivity())
                    parseForecastResponse(response)
                }
            }
        }
        else {
            Toast.makeText(requireParentFragment().requireActivity(), "No internet connection, fetching previously saved data.", Toast.LENGTH_SHORT).show()
            var response = OpenWeatherApiService.read5DayForecast(cityName, countryCode, requireParentFragment().requireActivity())
            parseForecastResponse(response)
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
        if (forecastResponse == null) {
            Toast.makeText(activity, "Failed to obtain data!", Toast.LENGTH_SHORT).show()
            return
        }
        with(forecastResponse) {
            activity?.runOnUiThread {
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
            }
        }


    }

    private suspend fun launchWithLoading(f:suspend () -> Unit) {
        activity?.runOnUiThread { loading_progress_bar1.visibility = View.VISIBLE }
        f.invoke()
        activity?.runOnUiThread { loading_progress_bar1.visibility = View.GONE }
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
