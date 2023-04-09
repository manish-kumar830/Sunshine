package com.android.manish.sunshine.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.android.manish.sunshine.data.WeatherContract.WeatherColumn;
import androidx.annotation.Nullable;

public class WeatherDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weatherdb";
    private static final int DATABASE_VERSION = 1;

    public WeatherDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String QUERY_SQL = "CREATE TABLE " + WeatherColumn.TABLE_NAME +" ( "
                + WeatherColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + WeatherColumn.WEATHER + " TEXT,"
                + WeatherColumn.TEMPERATURE_IN_CELSIUS + " TEXT,"
                + WeatherColumn.TEMPERATURE_IN_FAHRENHEIT + " TEXT,"
                + WeatherColumn.IS_DAY + " TEXT,"
                + WeatherColumn.WIND_SPEED + " TEXT,"
                + WeatherColumn.PRESSURE + " TEXT,"
                + WeatherColumn.TIME + " TEXT,"
                + WeatherColumn.HUMIDITY + " TEXT,"
                + WeatherColumn.SUNRISE + " TEXT,"
                + WeatherColumn.SUNSET + " TEXT,"
                + WeatherColumn.SELECTION + " INTEGER,"
                + WeatherColumn.UV_INDEX + " TEXT );";

        sqLiteDatabase.execSQL(QUERY_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherColumn.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

}
