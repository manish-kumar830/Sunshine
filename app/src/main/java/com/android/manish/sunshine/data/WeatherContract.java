package com.android.manish.sunshine.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class WeatherContract {

    public static final String CONTENT_AUTHORITY = "com.android.manish.sunshine";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String WEATHER_PATH = "weather";


    public static class WeatherColumn implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,WEATHER_PATH);

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + WEATHER_PATH;
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + WEATHER_PATH;

        public static final String TABLE_NAME = "weather";
        public static final String _ID = BaseColumns._ID;
        public static final String TEMPERATURE_IN_CELSIUS = "temperatureInCelsius";
        public static final String TEMPERATURE_IN_FAHRENHEIT = "temperatureInFahrenheit";
        public static final String WEATHER = "todayWeather";
        public static final String IS_DAY = "isDay";
        public static final String WIND_SPEED = "windSpeed";
        public static final String PRESSURE = "pressure";
        public static final String HUMIDITY = "humidity";
        public static final String UV_INDEX = "uvIndex";
        public static final String SUNRISE = "sunrise";
        public static final String SUNSET = "sunset";
        public static final String TIME = "time";
        public static final String SELECTION = "selection";
        public static final long HOURS = 1;
        public static final long CURRENT = 0;


    }

}

