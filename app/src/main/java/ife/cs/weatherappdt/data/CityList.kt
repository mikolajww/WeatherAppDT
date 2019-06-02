package ife.cs.weatherappdt.data

data class City(var name:String, var country:String, var selected:Boolean = false)

object CityList {

    private val ITEMS: MutableList<City> = mutableListOf(
        City("Lodz", "pl", false),
        City("Warsaw", "pl", false)
    )

    fun add(city: City) = ITEMS.add(city)
    fun get(position: Int) = ITEMS[position]
    fun getAll(): List<City> = ITEMS
}
