package ife.cs.weatherappdt


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ife.cs.weatherappdt.api.responses.WeatherResponse

class WeatherFragment : Fragment() {

    private lateinit var listener: OnGetWeatherInfo

    interface OnGetWeatherInfo {
        fun getWeatherInfo(): WeatherResponse
    }


    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }


}
