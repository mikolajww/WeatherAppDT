package ife.cs.weatherappdt.data

data class City(var name:String, var country:String, var selected:Boolean = false)

object CityList {

    val ITEMS: MutableList<City> = mutableListOf(
        City("Lodz", "pl", false),
        City("Warsaw", "pl", false)
    )

}
