package ife.cs.weatherappdt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ife.cs.weatherappdt.data.model.City
import ife.cs.weatherappdt.fragment.CityFragment
import ife.cs.weatherappdt.fragment.PreferencesFragment
import kotlinx.android.synthetic.main.activity_preferences.*

class PreferencesActivity: AppCompatActivity(){

    private lateinit var cityFragment: CityFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        pager.adapter = TabViewPagerAdapter(supportFragmentManager)
        tab_layout.setupWithViewPager(pager)
    }


    inner class TabViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> PreferencesFragment()
                1 -> CityFragment().also { cityFragment = it }
                else -> PreferencesFragment()
            }
        }

        override fun getCount(): Int  = 2

        override fun getPageTitle(position: Int): CharSequence {
            return when(position) {
                0 -> "Preferences"
                1 -> "Favourite cities"
                else -> "Shouldn't be visible"
            }
        }
    }
}