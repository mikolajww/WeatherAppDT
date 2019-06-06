package ife.cs.weatherappdt.fragment


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
import kotlinx.android.synthetic.main.fragment_forecast.*
import kotlinx.android.synthetic.main.fragment_forecast.view.*
import kotlinx.android.synthetic.main.fragment_forecast.loading_progress_bar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ForecastFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        GlobalScope.launch {
            launchWithLoading {
                val response = OpenWeatherApiService.fetch5DayForecast("London", "uk")
                parseForecastResponse(response)
            }
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
                println(parsedForecastList)
                println(parsedForecastList.get(0).main?.temp.toString())
                println(parsedForecastList.get(1).main?.temp.toString())
                temp1.text = parsedForecastList.get(0).main?.temp.toString()// + OpenWeatherApiService.getUnitSuffix()
                temp2.text = parsedForecastList.get(1).main?.temp.toString()// + OpenWeatherApiService.getUnitSuffix()
            }
        }


    }

    private suspend fun launchWithLoading(f:suspend () -> Unit) {
        activity?.runOnUiThread { loading_progress_bar.visibility = View.VISIBLE }
        f.invoke()
        activity?.runOnUiThread { loading_progress_bar.visibility = View.GONE }
    }

}
