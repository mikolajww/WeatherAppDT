package ife.cs.weatherappdt.data.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import ife.cs.weatherappdt.data.dao.CityDao
import ife.cs.weatherappdt.data.model.City

class CityRepository(private val cityDao: CityDao) {

    val allCities: LiveData<List<City>> = cityDao.getAll()

    @WorkerThread
    suspend fun insert(city: City) {
        cityDao.insertAll(city)
    }
}