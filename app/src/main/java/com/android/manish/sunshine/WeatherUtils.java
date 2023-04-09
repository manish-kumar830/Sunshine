package com.android.manish.sunshine;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

import com.android.manish.sunshine.data.WeatherContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class  WeatherUtils {


    public static List<WeatherModel> fetchWeather(String requestedUrl) throws IOException {

        List<WeatherModel> weather = new ArrayList<>();


        URL url = createUrl(requestedUrl);
        String weatherResponse = makeHttpRequest(url);

        weather = fetchWeatherFromResponse(weatherResponse);

        return weather;

    }


    private static URL createUrl(String url) {

        URL requestUrl = null;

        try {
            requestUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return requestUrl;
    }


    private static String makeHttpRequest(URL _url) throws IOException {

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String response = null;
        urlConnection = (HttpURLConnection) _url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
        urlConnection.connect();

        int responseCode = urlConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {

            inputStream = urlConnection.getInputStream();
            response = readFromStream(inputStream);

        }
        return response;
    }

    private static String readFromStream(InputStream inputStream) {

        StringBuilder builder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        try {
            String line = reader.readLine();

            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }


    private static List<WeatherModel> fetchWeatherFromResponse(String response) {

        List<WeatherModel> weatherList = new ArrayList<>();

        if (response != null) {

            try {

                JSONObject rootObject = new JSONObject(response);
                JSONObject currentObject = rootObject.optJSONObject("current");
                JSONObject condition = currentObject.optJSONObject("condition");
                JSONObject forecast = rootObject.optJSONObject("forecast");
                JSONArray forecastDay = forecast.optJSONArray("forecastday");
                JSONObject indexObject = forecastDay.optJSONObject(0);
                JSONArray hours = indexObject.optJSONArray("hour");
                JSONObject astro = indexObject.optJSONObject("astro");



                String temp_c = currentObject.getString("temp_c")+"°C";
                String temp_f = currentObject.getString("temp_f")+"°F";
                String is_day = currentObject.getString("is_day");
                String wind_kph = currentObject.getString("wind_kph")+"km/h";
                String pressure_mb = currentObject.getString("pressure_mb")+"mbar";
                String humidity = currentObject.getString("humidity")+"%";
                String uv = currentObject.getString("uv");
                String weather = condition.getString("text");
                String sunrise = astro.getString("sunrise");
                String sunset = astro.getString("sunset");
                long selectionArgs = WeatherContract.WeatherColumn.CURRENT;

                weatherList.add(new WeatherModel(temp_c,temp_f,weather,is_day,wind_kph,pressure_mb,humidity,
                        uv,sunrise,sunset,"current", selectionArgs));





                for (int i = 0; i <= hours.length(); i++) {


                    JSONObject indexHoursObject = hours.getJSONObject(i);
                    JSONObject conditionHoursObject = indexHoursObject.getJSONObject("condition");

                    String hour_temp_c = indexHoursObject.getString("temp_c")+"°C";
                    String hour_temp_f = indexHoursObject.getString("temp_f")+"°F";
                    String hour_wind_kph = indexHoursObject.getString("wind_kph")+"km/h";
                    String hour_weather = conditionHoursObject.getString("text");
                    String hours_time = i + ":00";
                    long hour_selectionArgs = WeatherContract.WeatherColumn.HOURS;

                    weatherList.add(new WeatherModel(hour_temp_c,hour_temp_f,hour_weather," ",hour_wind_kph," "," ",
                            " "," "," ",hours_time, hour_selectionArgs));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return weatherList;
    }
}
