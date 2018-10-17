package org.pitkanen.weather_app_android.model

object Model {
    data class Main(val temp: Double)
    data class Weather(val main: String, val icon: String)
    data class WeatherData(val dt: Long, val weather: List<Weather>, val main: Main, val name: String)
    data class ForecastData(val list: List<WeatherData>)
}