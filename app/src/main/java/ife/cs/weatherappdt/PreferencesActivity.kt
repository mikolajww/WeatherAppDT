package ife.cs.weatherappdt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ife.cs.weatherappdt.data.City
import ife.cs.weatherappdt.data.CityList
import ife.cs.weatherappdt.fragment.CityFragment
import ife.cs.weatherappdt.fragment.PreferencesFragment
import kotlinx.android.synthetic.main.activity_preferences.*

class PreferencesActivity: AppCompatActivity(), CityFragment.OnListFragmentInteractionListener {

    private lateinit var cityFragment: CityFragment;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        supportFragmentManager.beginTransaction()
            .replace(R.id.preferences_container, PreferencesFragment())
            .replace(R.id.city_list_container, CityFragment().also { cityFragment = it })
            .commit()
        add_city.setOnClickListener {
            CityList.ITEMS.add(City("Gdansk", "pl"))
            cityFragment.recyclerViewAdapter.notifyDataSetChanged()
        }
    }

    override fun onListFragmentInteraction(item: City) {
    }
}