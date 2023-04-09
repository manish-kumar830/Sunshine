package com.android.manish.sunshine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.usage.NetworkStatsManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.manish.sunshine.data.WeatherContract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterClickListener {

    private final String BASE_URL = "https://api.weatherapi.com/v1/forecast.json?key=a98c28eb635e4ceb9d974800221112";
    private String location = "Hisar";
    private String REQUEST_URL;
    TextView temperature, weather, sunset, sunrise, humidity, uvIndex, windSpeed, pressure, showLocation;
    SharedPreferences sharedPreferences;
    private static final int LOADER_ID = 1;
    WeatherAdapter adapter;
    RecyclerView day_recycler_view;
    int position = RecyclerView.NO_POSITION;
    LinearLayout loadingScreen,mainScreen;
    Calendar calendar;
    TextView showDayStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperature = findViewById(R.id.temperature);
        weather = findViewById(R.id.weather);
        sunset = findViewById(R.id.sunset_time);
        sunrise = findViewById(R.id.sunrise_time);
        humidity = findViewById(R.id.humidity);
        uvIndex = findViewById(R.id.uv_index);
        showDayStatus = findViewById(R.id.showDayStatus);
        windSpeed = findViewById(R.id.wind_speed);
        pressure = findViewById(R.id.pressure);
        showLocation = findViewById(R.id.location);
        loadingScreen = findViewById(R.id.loadingScreen);
        mainScreen = findViewById(R.id.mainScreen);
        day_recycler_view = findViewById(R.id.dayRecyclerView);

        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String sDay = dayOfWeek(day);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        String sMonth = monthOfYear(month);
        int date = calendar.get(Calendar.DATE);
        String showDate = sDay+", "+sMonth+" "+date+", "+year;

        showDayStatus.setText(showDate);

        sharedPreferences = getSharedPreferences("location",MODE_PRIVATE);
        location = sharedPreferences.getString("userLocation","Hisar");
        showLocation.setText(location);

        REQUEST_URL = BASE_URL + "&q=" + location + "&days=1&aqi=yes&alerts=no";

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {

            Uri current_uri = ContentUris.withAppendedId(WeatherContract.WeatherColumn.CONTENT_URI, WeatherContract.WeatherColumn.CURRENT);

            String[] projection = {
                    WeatherContract.WeatherColumn.TEMPERATURE_IN_CELSIUS,
                    WeatherContract.WeatherColumn.WEATHER,
                    WeatherContract.WeatherColumn.HUMIDITY,
                    WeatherContract.WeatherColumn.PRESSURE,
                    WeatherContract.WeatherColumn.SUNRISE,
                    WeatherContract.WeatherColumn.SUNSET,
                    WeatherContract.WeatherColumn.TEMPERATURE_IN_FAHRENHEIT,
                    WeatherContract.WeatherColumn.UV_INDEX,
                    WeatherContract.WeatherColumn.WIND_SPEED
            };
            Cursor cursor = getContentResolver().query(current_uri, projection, null, null, null);

            while (cursor.moveToNext()) {

                String temp_c = cursor.getString(cursor.getColumnIndexOrThrow(WeatherContract.WeatherColumn.TEMPERATURE_IN_CELSIUS));
                String weather_c = cursor.getString(cursor.getColumnIndexOrThrow(WeatherContract.WeatherColumn.WEATHER));
                String temp_f_c = cursor.getString(cursor.getColumnIndexOrThrow(WeatherContract.WeatherColumn.TEMPERATURE_IN_FAHRENHEIT));
                String humidity_c = cursor.getString(cursor.getColumnIndexOrThrow(WeatherContract.WeatherColumn.HUMIDITY));
                String pressure_c = cursor.getString(cursor.getColumnIndexOrThrow(WeatherContract.WeatherColumn.PRESSURE));
                String sunrise_c = cursor.getString(cursor.getColumnIndexOrThrow(WeatherContract.WeatherColumn.SUNRISE));
                String sunset_c = cursor.getString(cursor.getColumnIndexOrThrow(WeatherContract.WeatherColumn.SUNSET));
                String uv_index_c = cursor.getString(cursor.getColumnIndexOrThrow(WeatherContract.WeatherColumn.UV_INDEX));
                String wind_speed_c = cursor.getString(cursor.getColumnIndexOrThrow(WeatherContract.WeatherColumn.WIND_SPEED));



                temperature.setText(temp_c);
                weather.setText(weather_c);
                humidity.setText(humidity_c);
                pressure.setText(pressure_c);
                sunrise.setText(sunrise_c);
                sunset.setText(sunset_c);
                uvIndex.setText(uv_index_c);
                windSpeed.setText(wind_speed_c);
            }

            day_recycler_view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            day_recycler_view.setHasFixedSize(false);
            adapter = new WeatherAdapter(this,this);
            day_recycler_view.setAdapter(adapter);

            getSupportLoaderManager().initLoader(LOADER_ID,null,this).forceLoad();

        } else {
            WeatherTask weatherTask = new WeatherTask();
            weatherTask.execute();

        }




    }



    @Override
    protected void onRestart() {
        super.onRestart();

        sharedPreferences = getSharedPreferences("location",MODE_PRIVATE);
        location = sharedPreferences.getString("userLocation","Hisar");
        showLocation.setText(location);
        REQUEST_URL = BASE_URL + "&q=" + location + "&days=1&aqi=yes&alerts=no";

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {

            Toast.makeText(this, "Network Not Connected", Toast.LENGTH_SHORT).show();

        } else {
            WeatherTask weatherTask = new WeatherTask();
            weatherTask.execute();

        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = {
                WeatherContract.WeatherColumn.TEMPERATURE_IN_CELSIUS,
                WeatherContract.WeatherColumn.WEATHER,
                WeatherContract.WeatherColumn.TEMPERATURE_IN_FAHRENHEIT,
                WeatherContract.WeatherColumn.TIME,
                WeatherContract.WeatherColumn.WIND_SPEED
        };

        Uri uri = ContentUris.withAppendedId(WeatherContract.WeatherColumn.CONTENT_URI, WeatherContract.WeatherColumn.HOURS);

        return new CursorLoader(this,uri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        if (cursor.getCount() != 0) {

            mainScreen.setVisibility(View.VISIBLE);
            loadingScreen.setVisibility(View.GONE);

            adapter.swapCursor(cursor);
            if (position == RecyclerView.NO_POSITION) position = 0;
            day_recycler_view.smoothScrollToPosition(position);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Position : "+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetworkItemClick(int position) {
        Toast.makeText(this, "Position : "+position, Toast.LENGTH_SHORT).show();
    }


    public class WeatherTask extends AsyncTask<Void, Void, List<WeatherModel>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingScreen.setVisibility(View.VISIBLE);
            mainScreen.setVisibility(View.GONE);
        }


        @Override
        protected List<WeatherModel> doInBackground(Void... voids) {

            List<WeatherModel> values = new ArrayList<>();
            try {
                values = WeatherUtils.fetchWeather(REQUEST_URL);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return values;

        }

        @Override
        protected void onPostExecute(List<WeatherModel> weatherList) {
            super.onPostExecute(weatherList);

            if (weatherList.size() > 0) {
                int deleted = getContentResolver().delete(WeatherContract.WeatherColumn.CONTENT_URI,null,null);

                WeatherModel currentWeatherModel = weatherList.get(0);
                String currentTemp = currentWeatherModel.getTemperatureInCelsius();
                String currentWindSpeed = currentWeatherModel.getWindSpeed();
                String currentPressure = currentWeatherModel.getPressure();
                String sunriseFromCurrentWeather = currentWeatherModel.getSunriseTime();
                String sunsetFromCurrentWeather = currentWeatherModel.getSunsetTime();
                String currentWeather = currentWeatherModel.getWeather();
                String currentHumidity = currentWeatherModel.getHumidity();
                String currentUvIndex = currentWeatherModel.getUvIndex();

                temperature.setText(currentTemp);
                weather.setText(currentWeather);
                humidity.setText(currentHumidity);
                pressure.setText(currentPressure);
                sunrise.setText(sunriseFromCurrentWeather);
                sunset.setText(sunsetFromCurrentWeather);
                uvIndex.setText(currentUvIndex);
                windSpeed.setText(currentWindSpeed);

                List<WeatherModel> networkWeatherList = new ArrayList<>();


                for (int i = 1; i < weatherList.size(); i++) {

                    WeatherModel model = weatherList.get(i);
                    String n_weather = model.getWeather();
                    String n_temp_c = model.getTemperatureInCelsius();
                    String n_temp_f = model.getTemperatureInFahrenheit();
                    String n_wind_speed = model.getWindSpeed();
                    String n_time = model.getTime();
                    String n_is_day = model.getIsDay();
                    String n_pressure = model.getPressure();
                    String n_humidity = model.getHumidity();
                    String n_uv_index = model.getUvIndex();
                    String n_sunrise = model.getSunriseTime();
                    String n_sunset = model.getSunsetTime();
                    long n_selection = model.getSelectionArgs();


                    networkWeatherList.add(new WeatherModel(n_temp_c,n_temp_f,n_weather,n_is_day,
                                                            n_wind_speed,n_pressure,n_humidity,n_uv_index,
                                                            n_sunrise,n_sunset,n_time,n_selection));

                }

                NetworkWeatherAdapter n_adapter = new NetworkWeatherAdapter(networkWeatherList,MainActivity.this, MainActivity.this);

                day_recycler_view.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
                day_recycler_view.setHasFixedSize(false);
                day_recycler_view.setAdapter(n_adapter);

                loadingScreen.setVisibility(View.GONE);
                mainScreen.setVisibility(View.VISIBLE);



                if (deleted != -1) {

                    int rowInserted = 0;
                    for (int i = 0; i < weatherList.size(); i++) {

                        WeatherModel model = weatherList.get(i);
                        String weather = model.getWeather();
                        String temp_c = model.getTemperatureInCelsius();
                        String temp_f = model.getTemperatureInFahrenheit();
                        String wind_speed = model.getWindSpeed();
                        String time = model.getTime();
                        String is_day = model.getIsDay();
                        String pressure = model.getPressure();
                        String humidity = model.getHumidity();
                        String uv_index = model.getUvIndex();
                        String sunrise = model.getSunriseTime();
                        String sunset = model.getSunsetTime();
                        long selection = model.getSelectionArgs();


                        ContentValues currentContentValue = new ContentValues();
                        currentContentValue.put(WeatherContract.WeatherColumn.WEATHER, weather);
                        currentContentValue.put(WeatherContract.WeatherColumn.TEMPERATURE_IN_CELSIUS, temp_c);
                        currentContentValue.put(WeatherContract.WeatherColumn.TEMPERATURE_IN_FAHRENHEIT, temp_f);
                        currentContentValue.put(WeatherContract.WeatherColumn.WIND_SPEED, wind_speed);
                        currentContentValue.put(WeatherContract.WeatherColumn.TIME, time);
                        currentContentValue.put(WeatherContract.WeatherColumn.IS_DAY, is_day);
                        currentContentValue.put(WeatherContract.WeatherColumn.PRESSURE, pressure);
                        currentContentValue.put(WeatherContract.WeatherColumn.HUMIDITY, humidity);
                        currentContentValue.put(WeatherContract.WeatherColumn.UV_INDEX, uv_index);
                        currentContentValue.put(WeatherContract.WeatherColumn.SUNRISE, sunrise);
                        currentContentValue.put(WeatherContract.WeatherColumn.SUNSET, sunset);
                        currentContentValue.put(WeatherContract.WeatherColumn.SELECTION, selection);

                        Uri uri = getContentResolver().insert(WeatherContract.WeatherColumn.CONTENT_URI, currentContentValue);
                        if (uri != null) {
                            rowInserted++;
                        } else {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            } else {
                Toast.makeText(MainActivity.this, "Location Error", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.setting:
                Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
                break;
            default:
                Toast.makeText(this, "Nothing", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private String dayOfWeek(int day){

        switch (day){

            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                return "Error";

        }
    }

    private String monthOfYear(int month){

        switch (month){

            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return "Error";

        }
    }


}