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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //println("Created wrapper in onCreate $cityName $countryCode")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //println("Created wrapper in onViewCreated with $cityName $countryCode")
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

    override fun onDestroyView() {
        super.onDestroyView()
        //println("WrapperFragment onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        //println("WrapperFragment onDestroy")
    }
}
