package ife.cs.weatherappdt

// woda niezagazowana

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.astrocalculator.AstroCalculator
import com.astrocalculator.AstroDateTime
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.coroutines.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

fun Double.KToC() = this - 273.15
fun Double.KToF() = this * 1.8 - 459.67
fun Double.CToF() = this * 1.8 + 32
fun Double.FToC() = (this - 32)/1.8

class MainActivity : AppCompatActivity(), MoonFragment.OnGetMoonInfo, SunFragment.OnGetSunInfo {

    private lateinit var sunFragment: SunFragment
    private lateinit var dateTime: AstroDateTime
    private lateinit var location: AstroCalculator.Location
    private lateinit var astroCalculator: AstroCalculator

    private lateinit var moonFragment: MoonFragment
    private lateinit var sharedPreferences: SharedPreferences

    private val clockTimer = Timer()
    private var refreshTimer = Timer()

    override fun onResume() {
        super.onResume()
        setupAstroCalculator()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        with(sharedPreferences) {
            if (getString("latitude", "0")!!.equals("0", ignoreCase = true) or
                getString("longitude", "0")!!.equals("0", ignoreCase = true)
            ) {
                startActivity(Intent(this@MainActivity, PreferencesActivity::class.java))
            }
        }
        Toast.makeText(
            this@MainActivity,
            "${if (verifyAvailableNetwork(this)) "" else "Not"} Connected to the internet",
            Toast.LENGTH_LONG
        ).show()
        setupAstroCalculator()
        setupToolbar()
        setupViewPager()

        clockTimer.scheduleAtFixedRate(UpdateClockTask(), 1000L, 1000L)
        refreshTimer.scheduleAtFixedRate(
            UpdateInfoTask(),
            10000L,
            (sharedPreferences.getInt("refresh_time", 1) * 60 * 1000).toLong()
        )

        weatherButton?.setOnClickListener {
            startActivity(Intent(this@MainActivity, TestWeatherActivity::class.java))
        }
    }

    private fun setupViewPager() {
        if (viewPager == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.sun_container, sunFragment)
                .replace(R.id.moon_container, moonFragment)
                .commit()
        } else {
            viewPager!!.adapter =
                object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                    override fun getItem(position: Int): Fragment {
                        return if (position == 0) sunFragment else moonFragment
                    }

                    override fun getCount() = 2
                }
        }
    }

    private fun setupToolbar() {
        with(toolbar) {
            latuitude_textView.text = sharedPreferences.getString("latitude", "0")
            longitude_textView.text = sharedPreferences.getString("longitude", "0")
            latuitude_textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.toolbarTextsize))
            longitude_textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.toolbarTextsize))
            time_textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.toolbarTextsize))
        }
        setSupportActionBar(toolbar as Toolbar)
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
            (zone.getOffset(Date().time) / 1000 / 60 / 60) - 1, zone.inDaylightTime(Date())
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
        return when (item?.itemId) {
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

    private inner class UpdateClockTask : TimerTask() {
        override fun run() {
            runOnUiThread {
                val now = Calendar.getInstance()
                with(SimpleDateFormat("dd-MM-yyyy HH:mm:ss")) {
                    toolbar.time_textView.text = this.format(now.time)
                }
            }
        }
    }

    private inner class UpdateInfoTask : TimerTask() {
        override fun run() {
            runOnUiThread {
                if (viewPager == null) {
                    sunFragment.displaySunInfo()
                    moonFragment.displayMoonInfo()
                } else {
                    viewPager.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    fun verifyAvailableNetwork(activity:AppCompatActivity):Boolean {
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return  networkInfo != null && networkInfo.isConnected
    }
}
