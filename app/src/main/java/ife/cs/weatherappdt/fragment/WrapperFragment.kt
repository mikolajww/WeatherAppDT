package ife.cs.weatherappdt.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ife.cs.weatherappdt.R


class WrapperFragment : Fragment() {

    private lateinit var cityName: String
    private lateinit var countryCode: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return inflater.inflate(R.layout.fragment_wrapper, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println(activity)
        childFragmentManager.beginTransaction()
            .replace(R.id.weather_frame_layout, WeatherFragment.newInstance(cityName, countryCode))
            .replace(R.id.forecast_frame_layout, ForecastFragment.newInstance(cityName, countryCode))
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
        fun newInstance(cityName: String, countryCode: String) =
            WrapperFragment().apply {
                arguments = Bundle().apply {
                    putString("CITYNAME", cityName)
                    putString("COUNTRYCODE", countryCode)
                }
            }
    }
}
