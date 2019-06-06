package ife.cs.weatherappdt.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ife.cs.weatherappdt.data.model.City

@Dao
interface CityDao {
    @Query("SELECT * FROM cities")
    fun getAll(): List<City>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg cities: City)

    @Query("DELETE FROM cities")
    fun deleteAll()

    @Transaction
    fun replaceAll(vararg cities:City) {
        deleteAll()
        insertAll(*cities)
    }

    @Delete
    fun delete(city: City)
}