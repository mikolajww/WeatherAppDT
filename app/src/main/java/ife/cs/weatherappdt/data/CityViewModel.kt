package ife.cs.weatherappdt.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ife.cs.weatherappdt.data.database.CityRoomDatabase
import ife.cs.weatherappdt.data.model.City
import ife.cs.weatherappdt.data.repository.CityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CityRepository
    val allCities: LiveData<List<City>>

    init {
        val cityDao = CityRoomDatabase.getDatabase(application).cityDao()
        repository = CityRepository(cityDao)
        allCities = repository.allCities
    }

    fun insert(city: City) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(city)
    }
}