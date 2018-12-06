package org.pitkanen.weather_app_android.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_row.view.*
import org.pitkanen.weather_app_android.R
import org.pitkanen.weather_app_android.model.Model
import java.text.SimpleDateFormat
import java.util.*

class WeatherItemAdapter(val context: Context, val items: List<Model.WeatherData>): RecyclerView.Adapter<WeatherItemAdapter.WeatherItemHolder>() {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy")
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): WeatherItemHolder {
        return WeatherItemHolder(LayoutInflater.from(context).inflate(R.layout.list_item_row, viewGroup, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: WeatherItemHolder, index: Int) {
        val data = items.get(index)
        holder.dateText.text = dateFormat.format(Date(data.dt * 1000))
        Glide.with(context).load("https://openweathermap.org/img/w/"+data.weather?.get(0).icon+".png").into(holder.icon)
        holder.tempText.text = data.main.temp.toString() + "°C"
    }

    class WeatherItemHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val dateText = itemView.date_text
        val icon = itemView.weather_icon
        val tempText = itemView.temp_text
    }

}