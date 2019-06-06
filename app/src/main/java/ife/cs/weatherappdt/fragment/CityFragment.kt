package ife.cs.weatherappdt.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ife.cs.weatherappdt.R
import ife.cs.weatherappdt.adapter.MyCityRecyclerViewAdapter
import ife.cs.weatherappdt.data.dao.CityDao
import ife.cs.weatherappdt.data.database.CityRoomDatabase
import ife.cs.weatherappdt.data.model.City
import kotlinx.android.synthetic.main.fragment_city_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.FieldPosition


class CityFragment : Fragment(), MyCityRecyclerViewAdapter.OnListFragmentInteractionListener {

    private var columnCount = 1

    //private var listener: OnListFragmentInteractionListener? = null
    lateinit var recyclerViewAdapter: MyCityRecyclerViewAdapter
    private var cachedCities: MutableList<City> = mutableListOf()
    private val database by lazy {
        CityRoomDatabase.getDatabase(context!!)
    }
    private lateinit var cityDao: CityDao
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_city_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            recyclerViewAdapter = MyCityRecyclerViewAdapter(this@CityFragment)
            adapter = recyclerViewAdapter
        }
        cityDao = database.cityDao()
        GlobalScope.launch {
            cachedCities = cityDao.getAll() as MutableList<City>
            recyclerViewAdapter.setCities(cachedCities)
        }

        view.findViewById<FloatingActionButton>(R.id.add_city).setOnClickListener {
            val cityName = city_name_edit_text.text.toString()
            val countryCode = country_code_edit_text.text.toString()
            if( cityName.isNotBlank() &&
                countryCode.isNotBlank() ) {
                cachedCities.add(City(cityName, countryCode))
                city_name_edit_text.setText("")
                country_code_edit_text.setText("")
                recyclerViewAdapter.setCities(cachedCities)
            }
        }
        return view
    }

    override fun interact(position: Int) {
        cachedCities[position].selected = !cachedCities[position].selected
        println("interacted with positon $position")
        recyclerViewAdapter.setCities(cachedCities)
        GlobalScope.launch {
            cityDao.replaceAll(*cachedCities.toTypedArray())
            println("Saved to DB!")
        }
    }


}
