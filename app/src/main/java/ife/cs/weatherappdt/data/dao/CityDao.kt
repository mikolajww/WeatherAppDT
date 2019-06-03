package ife.cs.weatherappdt.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ife.cs.weatherappdt.data.model.City

@Dao
interface CityDao {
    @Query("SELECT * FROM cities")
    fun getAll(): LiveData<List<City>>

    @Insert
    fun insertAll(vararg cities: City)

    @Delete
    fun delete(user: City)
}