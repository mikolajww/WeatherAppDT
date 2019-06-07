package ife.cs.weatherappdt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import ife.cs.weatherappdt.data.dao.CityDao
import ife.cs.weatherappdt.data.database.CityRoomDatabase
import ife.cs.weatherappdt.data.model.City
import ife.cs.weatherappdt.fragment.WeatherFragment
import ife.cs.weatherappdt.fragment.WrapperFragment
import kotlinx.android.synthetic.main.activity_weather.*


class WeatherActivity : AppCompatActivity() {

    private val database by lazy {
        CityRoomDatabase.getDatabase(applicationContext)
    }
    private lateinit var cityDao: CityDao
    private lateinit var cities: List<City>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        cityDao = database.cityDao()
        cities = cityDao.getAll().filter { it.selected }
        viewPager!!.adapter =
            object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                override fun getItem(position: Int): Fragment {
                    return WrapperFragment.newInstance(cities[position].name, cities[position].country)
                }

                override fun getCount() = cities.size
            }
    }
}
