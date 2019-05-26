package ife.cs.weatherappdt.responses

import com.beust.klaxon.Json

data class ForecastResponse(
    val city: City?,
    val cnt: Int?, // 40
    val cod: String?, // 200
    val list: List<X?>?,
    val message: Double? // 0.0074
) {
    data class City(
        val coord: Coord?,
        val country: String?, // PL
        val id: Int?, // 3093133
        val name: String?, // Lodz
        val population: Int?, // 768755
        val timezone: Int? // 7200
    ) {
        data class Coord(
            val lat: Double?, // 51.75
            val lon: Double? // 19.4667
        )
    }

    data class X(
        val clouds: Clouds?,
        val dt: Int?, // 1559314800
        val dt_txt: String?, // 2019-05-31 15:00:00
        val main: Main?,
        val rain: Rain?,
        val sys: Sys?,
        val weather: List<Weather?>?,
        val wind: Wind?
    ) {
        data class Main(
            val grnd_level: Double?, // 1007.28
            val humidity: Int?, // 91
            val pressure: Double?, // 1019.86
            val sea_level: Double?, // 1019.86
            val temp: Double?, // 282.7
            val temp_kf: Int?, // 0
            val temp_max: Double?, // 282.7
            val temp_min: Double? // 282.7
        )

        data class Weather(
            val description: String?, // light rain
            val icon: String?, // 10d
            val id: Int?, // 500
            val main: String? // Rain
        )

        data class Clouds(
            val all: Int? // 100
        )

        data class Rain(
            @Json(name = "3h")
            val threeH: Double? // 1.812
        )

        data class Sys(
            val pod: String? // d
        )

        data class Wind(
            val deg: Double?, // 345.657
            val speed: Double? // 7.38
        )
    }
}