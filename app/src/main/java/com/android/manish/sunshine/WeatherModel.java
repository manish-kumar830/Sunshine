package com.android.manish.sunshine;

public class WeatherModel {



    private String temperatureInCelsius;
    private String temperatureInFahrenheit;
    private String weather;
    private String isDay;
    private String windSpeed;
    private String pressure;
    private String humidity;
    private String uvIndex;
    private String sunriseTime;
    private String sunsetTime;
    private String time;
    private long selectionArgs;

    public WeatherModel(String temperatureInCelsius, String temperatureInFahrenheit,
                        String weather, String isDay, String windSpeed,
                        String pressure, String humidity, String uvIndex,
                        String sunriseTime, String sunsetTime, String time, long selectionArgs) {
        this.temperatureInCelsius = temperatureInCelsius;
        this.weather = weather;
        this.temperatureInFahrenheit = temperatureInFahrenheit;
        this.isDay = isDay;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.humidity = humidity;
        this.uvIndex = uvIndex;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
        this.time = time;
        this.selectionArgs = selectionArgs;
    }


    public String getTime() {
        return time;
    }

    public long getSelectionArgs() {
        return selectionArgs;
    }

    public String getTemperatureInCelsius() {
        return temperatureInCelsius;
    }

    public String getWeather() {
        return weather;
    }

    public String getTemperatureInFahrenheit() {
        return temperatureInFahrenheit;
    }

    public String getIsDay() {
        return isDay;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getUvIndex() {
        return uvIndex;
    }

    public String getSunriseTime() {
        return sunriseTime;
    }

    public String getSunsetTime() {
        return sunsetTime;
    }
}
