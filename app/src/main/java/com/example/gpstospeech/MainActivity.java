package com.example.gpstospeech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextToSpeech tts;
    Button button;
    TextView tw1, tw2;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        tw1 = (TextView) findViewById(R.id.textView);
        tw2 = (TextView) findViewById(R.id.textView2);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
    }

    private double abs(double f) {
        if(f >= 0)
            return f;
        else
            return -1*f;
    }

    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    tw1.setText((String)("Latitude: " + location.getLatitude()));
                    tw2.setText((String)("Longitude: " + location.getLongitude()));
                    boolean north = false, east = false;
                    if(location.getLatitude() >= 0) {
                        north = true;
                    }
                    if(location.getLongitude() >= 0) {
                        east = true;
                    }
                    String toSpeak = "Your current location is, " + abs(location.getLatitude()) + " degrees " + (north?"north, ":"south, ") + abs(location.getLongitude()) + " degrees " + (east?"east":"west");
//                    System.out.println(toSpeak);
                    tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }
}