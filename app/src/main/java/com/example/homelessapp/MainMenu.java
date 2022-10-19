package com.example.homelessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainMenu extends AppCompatActivity {

    CardView homelessPinsButton,foodButton,callButton,donateButton,addMarkerButton,settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        homelessPinsButton = findViewById(R.id.homelessPinsButton);
        foodButton = findViewById(R.id.foodButton);
        callButton = findViewById(R.id.callButton);
        donateButton = findViewById(R.id.donateButton);
        addMarkerButton = findViewById(R.id.addMarkerButton);
        settingsButton = findViewById(R.id.settingsButton);

        homelessPinsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this,MapsActivity.class);
                startActivity(intent);
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this,EmergencyCall.class);
                startActivity(intent);
            }
        });

        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this,NearbyServices.class);
                startActivity(intent);
            }
        });

        addMarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this,AddHomelessPin.class);
                startActivity(intent);
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this,Settings.class);
                startActivity(intent);
            }
        });

        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this,PayPal.class);
                startActivity(intent);
            }
        });

    }
}