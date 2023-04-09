package com.android.manish.sunshine.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WeatherProvider extends ContentProvider {

    WeatherDbHelper dbHelper;
    private static final int WITHOUT_ID = 100;
    private static final int WITH_ID = 101;
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY,WeatherContract.WEATHER_PATH,WITHOUT_ID);
        sUriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY,WeatherContract.WEATHER_PATH+"/*",WITH_ID);
    }

    @Override
    public boolean onCreate() {

        dbHelper = new WeatherDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match){

            case WITH_ID:
                selection = WeatherContract.WeatherColumn.SELECTION+"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(WeatherContract.WeatherColumn.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,null);
                break;
            default:
                throw new IllegalStateException("Error");
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        int match = sUriMatcher.match(uri);
        switch (match){
            case WITHOUT_ID:
                return WeatherContract.WeatherColumn.CONTENT_LIST_TYPE;
            case WITH_ID:
                return WeatherContract.WeatherColumn.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Error");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValue) {

        int match = sUriMatcher.match(uri);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long rowIns = 0;

        switch (match){

            case WITHOUT_ID:
                rowIns = database.insert(WeatherContract.WeatherColumn.TABLE_NAME,null,contentValue);
                break;
            default:
                throw new IllegalStateException("Error");

        }

        getContext().getContentResolver().notifyChange(uri,null);

        return ContentUris.withAppendedId(uri,rowIns);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowDel = 0;

        switch (match){
            case WITHOUT_ID:
                rowDel = database.delete(WeatherContract.WeatherColumn.TABLE_NAME,null,null);
                break;
            default:
                throw new IllegalStateException("Error");
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return rowDel;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
