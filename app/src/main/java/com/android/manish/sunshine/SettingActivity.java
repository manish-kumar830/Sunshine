package com.android.manish.sunshine;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;

public class SettingActivity extends AppCompatActivity {

    EditText location;
    Button setLocation;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        location = findViewById(R.id.prefLocation);
        setLocation = findViewById(R.id.setLocation);

        sharedPreferences = getSharedPreferences("location",MODE_PRIVATE);
        String uLocation = sharedPreferences.getString("userLocation","Hisar");
        location.setText(uLocation);


        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userLocation = location.getText().toString();

                if (!userLocation.isEmpty() && !userLocation.equals(" ")) {
                    sharedPreferences = getSharedPreferences("location",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userLocation",userLocation);
                    editor.apply();
                    finish();
                } else {
                    Toast.makeText(SettingActivity.this, "Please Enter Location", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}