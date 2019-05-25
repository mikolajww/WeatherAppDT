package ife.cs.weatherappdt

import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.astrocalculator.AstroCalculator
import kotlinx.android.synthetic.main.fragment_moon.*
import kotlinx.android.synthetic.main.fragment_sun.*
import java.util.*

class SunFragment : Fragment() {

    private lateinit var listener: OnGetSunInfo
    private lateinit var v: View
    private lateinit var sunInfo: AstroCalculator.SunInfo

    interface OnGetSunInfo {
        fun getSunInfo(): AstroCalculator.SunInfo
    }

    fun setOnGetSunInfoListener(listener: OnGetSunInfo) {
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_sun, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        displaySunInfo()
    }

    fun displaySunInfo() {
        for (tv in getAllTextViews()) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.textsize))
        }
        sunInfo = listener.getSunInfo()
        sunrise_time.text = "Sunrise time:${(if (isLandscape()) "\n" else " ")}${sunInfo.sunrise}"
        sunrise_azimuth.text = "Sunrise azimuth:${(if (isLandscape()) "\n" else " ")}${sunInfo.azimuthRise}"
        sunset_time.text = "Sunset time:${(if (isLandscape()) "\n" else " ")}${sunInfo.sunset}"
        sunset_azimuth.text = "Sunset azimuth:${(if (isLandscape()) "\n" else " ")} ${sunInfo.azimuthSet}"
        civil_dawn.text = "Civil dawn time:${(if (isLandscape()) "\n" else " ")}${sunInfo.twilightMorning}"
        civil_dusk.text = "Civil dusk time:${(if (isLandscape()) "\n" else " ")}${sunInfo.twilightEvening}"
    }

    private fun getAllTextViews(): List<TextView> {
        return Arrays.asList(
            sunrise_time, sunrise_azimuth,
            sunset_time,sunset_azimuth,
            civil_dawn, civil_dusk
        )

    }

    private fun isLandscape() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


}
