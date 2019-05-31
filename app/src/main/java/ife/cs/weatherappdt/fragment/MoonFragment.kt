package ife.cs.weatherappdt.fragment


import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.astrocalculator.AstroCalculator
import ife.cs.weatherappdt.R
import ife.cs.weatherappdt.format
import kotlinx.android.synthetic.main.fragment_moon.*
import java.util.*


class MoonFragment : Fragment() {

    private lateinit var listener: OnGetMoonInfo
    private lateinit var v:View
    private lateinit var moonInfo:AstroCalculator.MoonInfo

    interface OnGetMoonInfo {
        fun getMoonInfo(): AstroCalculator.MoonInfo
    }

    fun setOnGetMoonInfoListener(listener: OnGetMoonInfo) {
        this.listener = listener
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_moon, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        displayMoonInfo()
    }

    fun displayMoonInfo() {
        for (tv in getAllTextViews()) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.textsize))
        }
        moonInfo = listener.getMoonInfo()
        moonrise_time.text = "Moonrise time:${newlineIfPortrait()}${moonInfo.moonrise}"
        moonset_time.text = "Moonset time:${newlineIfPortrait()}${moonInfo.moonset}"
        new_moon.text = "Next new moon:${newlineIfPortrait()}${moonInfo.nextNewMoon}"
        full_moon.text = "Next full moon:${newlineIfPortrait()}${moonInfo.nextFullMoon}"
        moon_phase.text = "Moon illumination:${newlineIfPortrait()}${moonInfo.illumination}"
        synodic_month.text = "Moon age:${newlineIfPortrait()}${moonInfo.age.format(2)}"
    }

    private fun getAllTextViews(): List<TextView> {
        return Arrays.asList(
            moonrise_time, moonset_time,
            new_moon, full_moon,
            moon_phase, synodic_month
        )
    }

    private fun newlineIfPortrait(): String {
        return if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            " "
        } else "\n"
    }

}

