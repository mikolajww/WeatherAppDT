package ife.cs.weatherappdt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ife.cs.weatherappdt.R
import ife.cs.weatherappdt.data.model.City
import kotlinx.android.synthetic.main.fragment_city.view.*


class MyCityRecyclerViewAdapter(
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyCityRecyclerViewAdapter.ViewHolder>() {

    private var cities: List<City> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_city, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cities[position]
        holder.cityName.text = item.name
        holder.countryName.text = item.country
        holder.checkBox.isChecked = item.selected
        with(holder.mView) {
            tag = item
            setOnClickListener { v ->
                mListener?.interact(position)
            }
        }
    }

    override fun getItemCount(): Int = cities.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val cityName: TextView = mView.city_name_weather
        val countryName: TextView = mView.country_name
        val checkBox: CheckBox = mView.checkbox
    }

    internal fun setCities(newCities: List<City>) {
        cities = newCities
        notifyDataSetChanged()
    }

    interface OnListFragmentInteractionListener {
        fun interact(position: Int)
    }
}
