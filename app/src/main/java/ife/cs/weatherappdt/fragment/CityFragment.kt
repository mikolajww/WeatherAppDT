package ife.cs.weatherappdt.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ife.cs.weatherappdt.adapter.MyCityRecyclerViewAdapter
import ife.cs.weatherappdt.R
import ife.cs.weatherappdt.data.CityViewModel
import ife.cs.weatherappdt.data.model.City
import kotlinx.android.synthetic.main.fragment_city_list.*


class CityFragment : Fragment() {
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null
    lateinit var recyclerViewAdapter: MyCityRecyclerViewAdapter
    private lateinit var viewModel: CityViewModel

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
            recyclerViewAdapter = MyCityRecyclerViewAdapter(listener)
            adapter = recyclerViewAdapter
        }
        viewModel = ViewModelProviders.of(this).get(CityViewModel::class.java)
        viewModel.allCities.observe(this, Observer {
            recyclerViewAdapter.setCities(it)
        })
        view.findViewById<FloatingActionButton>(R.id.add_city).setOnClickListener {
            viewModel.insert(City("Gdansk", "pl"))
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: City)
    }

}
