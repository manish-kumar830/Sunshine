package com.android.manish.sunshine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.manish.sunshine.data.WeatherContract;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    AdapterClickListener clickListener;
    Cursor cursor;
    Context context;

    public WeatherAdapter(Context context, AdapterClickListener clickListener) {

        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.weather_day_item_list,parent,false);

        return new WeatherViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {

        cursor.moveToPosition(position);
        String temperature_c = cursor.getString(cursor.getColumnIndexOrThrow(WeatherContract.WeatherColumn.TEMPERATURE_IN_CELSIUS));
        String temperature_f = cursor.getString(cursor.getColumnIndexOrThrow(WeatherContract.WeatherColumn.TEMPERATURE_IN_FAHRENHEIT));
        String wind_speed = cursor.getString(cursor.getColumnIndexOrThrow(WeatherContract.WeatherColumn.WIND_SPEED));
        String time = cursor.getString(cursor.getColumnIndexOrThrow(WeatherContract.WeatherColumn.TIME));
        String weather = cursor.getString(cursor.getColumnIndexOrThrow(WeatherContract.WeatherColumn.WEATHER));

        holder.r_weather.setText(weather);
        holder.r_temperature.setText(temperature_c);
        holder.r_time.setText(time);
        holder.r_wind_speed.setText(wind_speed);

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {

        TextView r_temperature, r_weather, r_wind_speed, r_time;

        public WeatherViewHolder(@NonNull View itemView, AdapterClickListener adapterClickListener) {
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
                        adapterClickListener.onItemClick(position);
                    }

                }
            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void swapCursor(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }

}
