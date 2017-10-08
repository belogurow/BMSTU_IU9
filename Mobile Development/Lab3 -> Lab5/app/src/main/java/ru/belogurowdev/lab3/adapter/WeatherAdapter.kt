package ru.belogurowdev.lab3.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.belogurowdev.lab3.R
import ru.belogurowdev.lab3.model.Weather

/**
 * Created by alexbelogurow on 20.09.17.
 */
class WeatherAdapter(private var context: Context,
                     private var weatherList: ArrayList<Weather>) :
        RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mTextViewCity: TextView? = null
        var mTextViewTemp: TextView? = null

        init {
            mTextViewCity = itemView.findViewById(R.id.item_text_weather_city)
            mTextViewTemp = itemView.findViewById(R.id.item_text_weather_temp)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_weather, parent,false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder?, position: Int) {
        val weather = weatherList[position]

        holder?.mTextViewCity?.text = weather.main
        holder?.mTextViewTemp?.text = weather.temp
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }
}

