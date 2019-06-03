package ife.cs.weatherappdt.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class City(
    @PrimaryKey var name:String,
    var country:String,
    var selected:Boolean = false
)