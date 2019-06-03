package ife.cs.weatherappdt.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ife.cs.weatherappdt.data.dao.CityDao
import ife.cs.weatherappdt.data.model.City

@Database(entities = [City::class], version = 1)
abstract class CityRoomDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao

    companion object {
        @Volatile
        private var INSTANCE: CityRoomDatabase? = null

        fun getDatabase(context: Context): CityRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CityRoomDatabase::class.java,
                    "cities_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}