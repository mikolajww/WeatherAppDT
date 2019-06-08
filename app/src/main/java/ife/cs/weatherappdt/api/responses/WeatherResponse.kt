package ife.cs.weatherappdt.api.responses

import com.beust.klaxon.Json

data class WeatherResponse(
    val base: String? = null, // stations
    val clouds: Clouds? = null,
    val cod: Int? = null, // 200
    val coord: Coord? = null,
    val dt: Int? = null, // 1558891432
    val id: Int? = null, // 3093133
    val main: Main? = null,
    val name: String? = null, // Lodz
    val sys: Sys? = null,
    val timezone: Int? = null, // 7200
    val visibility: Int? = null, // 10000
    val weather: List<Weather?>? = null,
    val wind: Wind? = null,
    val rain: Rain? = null,
    val snow: Snow? = null
) {
    data class Wind(
        val deg: Double? = null, // 240
        val speed: Double? = null // 5.1
    )

    data class Clouds(
        val all: Int? = null // 0
    )

    data class Sys(
        val country: String? = null, // PL
        val id: Int? = null, // 1706
        val message: Double? = null, // 0.0067
        val sunrise: Int? = null, // 1558838138
        val sunset: Int? = null, // 1558896176
        val type: Int? = null // 1
    )

    data class Coord(
        val lat: Double? = null, // 51.75
        val lon: Double? = null // 19.47
    )

    data class Main(
        val humidity: Int? = null, // 45
        val pressure: Int? = null, // 1011
        val temp: Double? = null, // 293.47
        val temp_max: Double? = null, // 294.26
        val temp_min: Double? = null // 292.59
    )

    data class Weather(
        val description: String? = null, // clear sky
        val icon: String? = null, // 01d
        val id: Int? = null, // 800
        val main: String? = null // Clear
    )

    data class Rain(
        @Json(name = "3h")
        val threeH: Double? = null // 1.812
    )

    data class Snow(
        @Json(name = "3h")
        val threeH: Double? = null
    )
}