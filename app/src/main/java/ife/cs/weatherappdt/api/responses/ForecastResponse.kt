package ife.cs.weatherappdt.api.responses

import com.beust.klaxon.Json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    val city: City? = null,
    val cnt: Int? = null, // 40
    val cod: String? = null, // 200
    val list: List<X>,
    val message: Double? = null // 0.0074
) {
    @Serializable
    data class City(
        val coord: Coord? = null,
        val country: String? = null, // PL
        val id: Int? = null, // 3093133
        val name: String? = null, // Lodz
        val population: Int? = null, // 768755
        val timezone: Int? = null // 7200
    ) {
        @Serializable
        data class Coord(
            val lat: Double? = null, // 51.75
            val lon: Double? = null // 19.4667
        )
    }
    @Serializable
    data class X(
        val clouds: Clouds? = null,
        val dt: Long? = null, // 1559314800
        val dt_txt: String? = null, // 2019-05-31 15:00:00
        val main: Main? = null,
        val rain: Rain? = null,
        val snow: Snow? = null,
        val sys: Sys? = null,
        val weather: List<Weather?>? = null,
        val wind: Wind? = null
    ) {
        @Serializable
        data class Main(
            val grnd_level: Double? = null, // 1007.28
            val humidity: Double? = null, // 91
            val pressure: Double? = null, // 1019.86
            val sea_level: Double? = null, // 1019.86
            val temp: Double? = null, // 282.7
            val temp_kf: Double? = null, // 0
            val temp_max: Double? = null, // 282.7
            val temp_min: Double? = null // 282.7
        )
        @Serializable
        data class Weather(
            val description: String? = null, // light rain
            val icon: String? = null, // 10d
            val id: Double? = null, // 500
            val main: String? = null // Rain
        )
        @Serializable
        data class Clouds(
            val all: Double? = null // 100
        )
        @Serializable
        data class Rain(
            @Json(name = "3h")
            @SerialName(value = "3h")
            val threeH: Double? = null // 1.812
        )
        @Serializable
        data class Sys(
            val pod: String? = null // d
        )
        @Serializable
        data class Wind(
            val deg: Double? = null, // 345.657
            val speed: Double? = null // 7.38
        )
        @Serializable
        data class Snow(
            @Json(name = "3h")
            @SerialName(value = "3h")
            val threeH: Double? = null
        )
    }
}