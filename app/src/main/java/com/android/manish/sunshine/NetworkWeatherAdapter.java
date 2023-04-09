package com.android.manish.sunshine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NetworkWeatherAdapter extends RecyclerView.Adapter<NetworkWeatherAdapter.NetworkViewHolder> {

    AdapterClickListener clickListener;
    List<WeatherModel> networkWeatherList;
    Context context;

    public NetworkWeatherAdapter(List<WeatherModel> networkWeatherList, Context context, AdapterClickListener clickListener) {
        this.networkWeatherList = networkWeatherList;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public NetworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.weather_day_item_list,parent,false);

        return new NetworkViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NetworkViewHolder holder, int position) {

        WeatherModel model = networkWeatherList.get(position);

        String temperature_c = model.getTemperatureInCelsius();
        String temperature_f = model.getTemperatureInFahrenheit();
        String wind_speed = model.getWindSpeed();
        String time = model.getTime();
        String weather = model.getWeather();


        holder.r_weather.setText(weather);
        holder.r_temperature.setText(temperature_c);
        holder.r_time.setText(time);
        holder.r_wind_speed.setText(wind_speed);

    }

    @Override
    public int getItemCount() {
        return networkWeatherList.size();
    }

    public class NetworkViewHolder extends RecyclerView.ViewHolder {

        TextView r_temperature, r_weather, r_wind_speed, r_time;

        public NetworkViewHolder(@NonNull View itemView, AdapterClickListener clickListener) {
            super(itemView);

            r_temperature = itemView.findViewById(R.id.r_temperature);
            r_weather = itemView.findViewById(R.id.r_weather);
            r_wind_speed = itemView.findViewById(R.id.r_wind_speed);
            r_time = itemView.findViewById(R.id.r_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onNetworkItemClick(position);
                    }

                }
            });
        }
    }
}
