package ru.belogurowdev.lab6.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ru.belogurowdev.lab6.R;
import ru.belogurowdev.lab6.model.List;
import ru.belogurowdev.lab6.model.Temp;
import ru.belogurowdev.lab6.model.Weather;

/**
 * Created by alexbelogurow on 08.10.2017.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private Context mContext;
    private java.util.List<ru.belogurowdev.lab6.model.List> mWeatherList;

    public WeatherAdapter(Context context) {
        mContext = context;
        mWeatherList = new ArrayList<>();
    }

    public void setWeatherList(java.util.List<List> weatherList) {
        mWeatherList = weatherList;
        notifyDataSetChanged();
    }


    public class WeatherViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewTemp;
        private TextView mTextViewDate;
        private TextView mTextViewMin;
        private TextView mTextViewMax;

        public WeatherViewHolder(View itemView) {
            super(itemView);

            mTextViewTemp = itemView.findViewById(R.id.text_item_weather_temp);
            mTextViewDate = itemView.findViewById(R.id.text_item_weather_date);
            mTextViewMin = itemView.findViewById(R.id.text_item_weather_min);
            mTextViewMax = itemView.findViewById(R.id.text_item_weather_max);
        }
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_weather, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        Temp temp = mWeatherList.get(position).getTemp();

        String celsius = "°C";

        holder.mTextViewTemp.setText(String.valueOf(temp.getDay()) + celsius);
        holder.mTextViewMin.setText(String.valueOf(temp.getMin()) + celsius);
        holder.mTextViewMax.setText(String.valueOf(temp.getMax()) + celsius);

        holder.mTextViewDate.setText(mWeatherList.get(position).getWeather().get(0).getMain());

    }

    @Override
    public int getItemCount() {
        return mWeatherList.size();
    }
}
