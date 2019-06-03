package ife.cs.weatherappdt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ife.cs.weatherappdt.data.model.City
import ife.cs.weatherappdt.fragment.CityFragment
import ife.cs.weatherappdt.fragment.PreferencesFragment
import kotlinx.android.synthetic.main.activity_preferences.*

class PreferencesActivity: AppCompatActivity(), CityFragment.OnListFragmentInteractionListener {

    private lateinit var cityFragment: CityFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        supportFragmentManager.beginTransaction()
            .replace(R.id.preferences_container, PreferencesFragment())
            .replace(R.id.city_list_container, CityFragment().also { cityFragment = it })
            .commit()
        add_city.setOnClickListener {

        }
    }

    override fun onListFragmentInteraction(item: City) {
    }
}