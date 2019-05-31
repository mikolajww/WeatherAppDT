package ife.cs.weatherappdt.api.responses

data class WeatherResponse(
    val base: String?, // stations
    val clouds: Clouds?,
    val cod: Int?, // 200
    val coord: Coord?,
    val dt: Int?, // 1558891432
    val id: Int?, // 3093133
    val main: Main?,
    val name: String?, // Lodz
    val sys: Sys?,
    val timezone: Int?, // 7200
    val visibility: Int?, // 10000
    val weather: List<Weather?>?,
    val wind: Wind? = null,
    val rain: ForecastResponse.X.Rain? = null,
    val snow: ForecastResponse.X.Snow? = null
) {
    data class Wind(
        val deg: Int? = 0, // 240
        val speed: Double? // 5.1`
    )

    data class Clouds(
        val all: Int? // 0
    )

    data class Sys(
        val country: String?, // PL
        val id: Int?, // 1706
        val message: Double?, // 0.0067
        val sunrise: Int?, // 1558838138
        val sunset: Int?, // 1558896176
        val type: Int? // 1
    )

    data class Coord(
        val lat: Double?, // 51.75
        val lon: Double? // 19.47
    )

    data class Main(
        val humidity: Int?, // 45
        val pressure: Int?, // 1011
        val temp: Double?, // 293.47
        val temp_max: Double?, // 294.26
        val temp_min: Double? // 292.59
    )

    data class Weather(
        val description: String?, // clear sky
        val icon: String?, // 01d
        val id: Int?, // 800
        val main: String? // Clear
    )
}