package ife.cs.weatherappdt.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import ife.cs.weatherappdt.R
import ife.cs.weatherappdt.fragment.CityFragment.OnListFragmentInteractionListener
import ife.cs.weatherappdt.data.City
import ife.cs.weatherappdt.data.CityList

import kotlinx.android.synthetic.main.fragment_city.view.*


class MyCityRecyclerViewAdapter(
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyCityRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as City
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_city, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = CityList.get(position)
        holder.cityName.text = item.name
        holder.countryName.text = item.country
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = CityList.getAll().size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val cityName: TextView = mView.city_name
        val countryName: TextView = mView.country_name
        val checkBox: CheckBox = mView.checkbox
    }
}
