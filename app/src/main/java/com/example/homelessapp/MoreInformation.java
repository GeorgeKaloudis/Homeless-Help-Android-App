package com.example.homelessapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homelessapp.DTO.MarkersInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MoreInformation extends AppCompatActivity {

    TextView location,latitude,longitude,gender,description;
    String destinationLat,destinationLong,currentLat,currentLong;

    ImageView back,snapshot;
    Bitmap bitmap;
    Button showDirections;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_information);

        showDirections = findViewById(R.id.showDirections);
        location = findViewById(R.id.locationInformation);
        latitude = findViewById(R.id.latidudeInformation);
        longitude = findViewById(R.id.longitudeInformation);
        gender = findViewById(R.id.genderInformation);
        description = findViewById(R.id.descriptionInformation);
        back = findViewById(R.id.informationBackButton);
        snapshot = findViewById(R.id.snapshot);


        Intent intent = getIntent();
        String markerId = intent.getStringExtra("markerID");
        currentLat = intent.getStringExtra("lat");
        currentLong = intent.getStringExtra("long");

        if(getIntent().hasExtra("byteArray")){
            Bitmap b = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"),0,getIntent()
                            .getByteArrayExtra("byteArray").length);
            snapshot.setImageBitmap(bitmap);
        }

        //Take from firebase the information of a specific marker that has been selected from user
        database = FirebaseDatabase.getInstance("https://homelessapp-57cb8-default-rtdb.firebaseio.com/");
        ref = database.getReference("Markers");
        ref.child(markerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MarkersInformation markersInformation = snapshot.getValue(MarkersInformation.class);
                location.setText(markersInformation.getLocality()+", "+markersInformation.getCountry());
                latitude.setText("lat: " + markersInformation.getLatitude());
                longitude.setText("long: " + markersInformation.getLongitude());
                gender.setText(markersInformation.getGender());
                description.setText(markersInformation.getInformation());
                destinationLat = markersInformation.getLatitude();
                destinationLong = markersInformation.getLongitude();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        showDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+currentLat+","+currentLong+"&daddr="+destinationLat+","+destinationLong;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });


    }
}