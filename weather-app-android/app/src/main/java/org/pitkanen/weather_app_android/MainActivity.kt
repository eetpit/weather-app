package org.pitkanen.weather_app_android

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item_row.view.*
import org.pitkanen.weather_app_android.api.WeatherApi
import org.pitkanen.weather_app_android.model.Model
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    private val APP_ID = "0613fdf35a1e7d5548e0d2b3b11296cb"
    private val PERMISSION_REQUEST_CODE = 123
    private val forecastData: MutableList<Model.WeatherData> = ArrayList()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        forecast_data_recycler.layoutManager = LinearLayoutManager(this)
        forecast_data_recycler.adapter = WeatherItemAdapter(this, forecastData)

        refresh_button.setOnClickListener {
            getLocation()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()
    }

    private fun getWeatherData(lat: Double?, lon: Double?) {
        val weatherApi = WeatherApi.create()

        weatherApi.getWeatherData(APP_ID, lat.toString(), lon.toString(), "metric")
                .enqueue(object: Callback<Model.WeatherData> {
                    override fun onResponse(call: Call<Model.WeatherData>, response: Response<Model.WeatherData>) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            Glide.with(this@MainActivity).load("https://openweathermap.org/img/w/"+data?.weather?.get(0)?.icon+".png").into(weather_icon)
                            weather_description.text = data?.weather?.get(0)?.main
                            temperature.text = data?.main?.temp.toString() + "°C"
                            location_name.text = data?.name
                        }
                    }

                    override fun onFailure(call: Call<Model.WeatherData>, t: Throwable) {
                        Log.e("getWeatherData", "getWeatherData failed " + t.message)
                    }
                })

        weatherApi.getForecastData(APP_ID, lat.toString(), lon.toString(), "metric")
                .enqueue(object: Callback<Model.ForecastData> {
            override fun onResponse(call: Call<Model.ForecastData>, response: Response<Model.ForecastData>) {
                if (response.isSuccessful) {
                    val data = response.body()

                    if (null != data?.list) {
                        forecastData.clear()
                        forecastData.addAll(data.list.toMutableList())

                        forecast_data_recycler.adapter?.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<Model.ForecastData>, t: Throwable) {
               Log.e("getForecastData", "getForecastData failed " + t.message)
            }
        })
    }

    private fun getLocation() {
        val locationPermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (locationPermissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_CODE)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            getWeatherData(location?.latitude, location?.longitude)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> getLocation()
            }
        }
    }

    class WeatherItemAdapter(val context: Context, val items: List<Model.WeatherData>): RecyclerView.Adapter<WeatherItemHolder>() {
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): WeatherItemHolder {
            return WeatherItemHolder(LayoutInflater.from(context).inflate(R.layout.list_item_row, viewGroup, false))
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: WeatherItemHolder, index: Int) {
            val data = items.get(index)
            holder.dateText.text = Date(data.dt).toString()
            Glide.with(context).load("https://openweathermap.org/img/w/"+data.weather?.get(0).icon+".png").into(holder.icon)
            holder.tempText.text = data.main.temp.toString() + "°C"
        }

    }

    class WeatherItemHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val dateText = itemView.date_text
        val icon = itemView.weather_icon
        val tempText = itemView.temp_text
    }
}
