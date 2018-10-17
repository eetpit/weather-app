package org.pitkanen.weather_app_android.api

import org.pitkanen.weather_app_android.model.Model
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/data/2.5/weather")
    fun getWeatherData(@Query("APPID") APPID: String,
                     @Query("lat") lat: String,
                     @Query("lon") lon: String,
                     @Query("units") units: String): Call<Model.WeatherData>

    @GET("/data/2.5//forecast")
    fun getForecastData(@Query("APPID") APPID: String,
                       @Query("lat") lat: String,
                       @Query("lon") lon: String,
                       @Query("units") units: String): Call<Model.ForecastData>

    companion object Factory {
        fun create(): WeatherApi {
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            return retrofit.create(WeatherApi::class.java)
        }
    }
}