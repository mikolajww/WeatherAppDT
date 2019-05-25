package ife.cs.weatherappdt

// woda niezagazowana

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.astrocalculator.AstroCalculator
import com.astrocalculator.AstroDateTime
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.view.*
import java.text.SimpleDateFormat
import java.util.*

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

class MainActivity : AppCompatActivity(), MoonFragment.OnGetMoonInfo, SunFragment.OnGetSunInfo {

    private lateinit var sunFragment: SunFragment
    private lateinit var dateTime:AstroDateTime
    private lateinit var location: AstroCalculator.Location
    private lateinit var astroCalculator: AstroCalculator

    private lateinit var moonFragment: MoonFragment
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

    private val clockTimer = Timer()
    private var refreshTimer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupAstroCalculator()
        setSupportActionBar(toolbar as Toolbar)

        if(viewPager == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.sun_container, sunFragment)
                .replace(R.id.moon_container, moonFragment)
                .commit()
        }
        else {
            viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                override fun getItem(position: Int): Fragment {
                    return if(position == 0) sunFragment else moonFragment
                }
                override fun getCount() = 2
            }
        }

        clockTimer.scheduleAtFixedRate(UpdateClockTask(), 1000L, 1000L)
        refreshTimer.scheduleAtFixedRate(UpdateInfoTask(),1000L,(sharedPreferences.getInt("refresh_time", 1) * 60 * 1000).toLong())
    }

    private fun setupAstroCalculator() {
        toolbar.latuitude_textView.text = "Latitude: ${sharedPreferences.getString("latitude", "0")}"
        toolbar.longitude_textView.text = "Longitude: ${sharedPreferences.getString("longitude", "0")}"
        val zone = TimeZone.getTimeZone("Europe/Warsaw")
        TimeZone.setDefault(zone)
        val now = Calendar.getInstance()
        dateTime = AstroDateTime(
            now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH),
            now.get(Calendar.HOUR), now.get(Calendar.MINUTE), now.get(Calendar.SECOND),
            (zone.getOffset(Date().time) / 1000 / 60 / 60 ) - 1, zone.inDaylightTime(Date())
        )
        location = AstroCalculator.Location(
            sharedPreferences.getString("latitude", "0").toDouble(),
            sharedPreferences.getString("longitude", "0").toDouble()
        )
        moonFragment = MoonFragment()
        sunFragment = SunFragment()
        astroCalculator = AstroCalculator(dateTime, location)
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is MoonFragment -> fragment.setOnGetMoonInfoListener(this)
            is SunFragment -> fragment.setOnGetSunInfoListener(this)
            else -> Log.d(this.javaClass.simpleName, "No such fragment supported")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.preferences_menu_button -> {
                startActivity(Intent(this, PreferencesActivity::class.java))
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun getSunInfo(): AstroCalculator.SunInfo {
        setupAstroCalculator()
        return astroCalculator.sunInfo
    }

    override fun getMoonInfo(): AstroCalculator.MoonInfo {
        setupAstroCalculator()
        return astroCalculator.moonInfo
    }

    inner class UpdateClockTask: TimerTask() {
        override fun run() {
            runOnUiThread {
                val now = Calendar.getInstance()
                with(SimpleDateFormat("dd-MM-yyyy HH:mm:ss")) {
                    toolbar.time_textView.text = this.format(now.time)
                }
            }
        }
    }

    inner class UpdateInfoTask: TimerTask() {
        override fun run() {
            runOnUiThread{
                sunFragment.displaySunInfo()
                moonFragment.displayMoonInfo()
            }
        }
    }
}
