package org.pitkanen.weather_app_android

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_row.view.*
import kotlinx.android.synthetic.main.activity_main.*
import org.pitkanen.weather_app_android.api.WeatherApi
import org.pitkanen.weather_app_android.model.Model
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    private val APP_ID = "0613fdf35a1e7d5548e0d2b3b11296cb"
    private val forecastData: MutableList<Model.WeatherData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        forecast_data_recycler.layoutManager = LinearLayoutManager(this)
        forecast_data_recycler.adapter = WeatherItemAdapter(this, forecastData)

        refresh_button.setOnClickListener {
            getWeatherData()
        }

        getWeatherData()
    }

    fun getWeatherData() {
        val weatherApi = WeatherApi.create()

        weatherApi.getWeatherData(APP_ID, "62.26023", "26.2324", "metric")
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

        weatherApi.getForecastData(APP_ID, "62.26023", "26.2324", "metric")
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
