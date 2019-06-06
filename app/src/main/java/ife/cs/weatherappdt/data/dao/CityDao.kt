package ife.cs.weatherappdt.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ife.cs.weatherappdt.data.model.City

@Dao
interface CityDao {
    @Query("SELECT * FROM cities")
    fun getAll(): LiveData<List<City>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg cities: City)

    @Delete
    fun delete(city: City)
}