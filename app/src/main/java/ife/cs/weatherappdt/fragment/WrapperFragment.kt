package ife.cs.weatherappdt.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ife.cs.weatherappdt.R
import kotlinx.android.synthetic.main.fragment_wrapper.*


class WrapperFragment : Fragment() {

    private lateinit var cityName: String
    private lateinit var countryCode: String

    private val weatherFragment by lazy {
        WeatherFragment.newInstance(cityName, countryCode)
    }
    private val forecastFragment by lazy {
        ForecastFragment.newInstance(cityName, countryCode)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wrapper, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe_refresh.setOnRefreshListener {
            Toast.makeText(context, "Refreshed the data!", Toast.LENGTH_SHORT).show()
            forecastFragment.displayForecast()
            weatherFragment.displayWeather()
            swipe_refresh.isRefreshing = false
        }
        childFragmentManager.beginTransaction()
            .replace(R.id.weather_frame_layout, weatherFragment )
            .replace(R.id.forecast_frame_layout, forecastFragment )
            .commit()
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

    companion object {
        @JvmStatic
        fun newInstance(cityName: String, countryCode: String):WrapperFragment {
            //println("WrapperFragment newInstance")

            return WrapperFragment().apply {
                arguments = Bundle().apply {
                    putString("CITYNAME", cityName)
                    putString("COUNTRYCODE", countryCode)
                }
            }
        }
    }

}
