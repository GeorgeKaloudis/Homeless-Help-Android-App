package com.example.homelessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homelessapp.DTO.MarkersInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MarkerInformation extends AppCompatActivity {

    TextView location,latitudeText,longitudeText;
    EditText information;
    RadioGroup radioGroup;
    RadioButton radioButton;
    int radioButtonId;
    String gender,markerLatitude,markerLongitude,locality,country,description;
    Button createMarker,cancelButton;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_information);

        location = findViewById(R.id.location);
        latitudeText = findViewById(R.id.latitudeText);
        longitudeText = findViewById(R.id.longitudeText);
        information = findViewById(R.id.information);
        createMarker = findViewById(R.id.createMarkerButton);
        cancelButton = findViewById(R.id.cancelButton);
        radioGroup = findViewById(R.id.gender);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        markerLatitude = extras.getString("markerLatitude");
        markerLongitude = extras.getString("markerLongitude");
        locality = extras.getString("locality");
        country = extras.getString("country");

        location.setText(locality+", "+country);
        latitudeText.setText("lat: "+markerLatitude);
        longitudeText.setText("long: "+ markerLongitude);

        //Create new marker,the information of marker is saved in firebase
        createMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioButtonId);

                if(radioButton==null){
                    Toast.makeText(MarkerInformation.this, "Please select gender", Toast.LENGTH_LONG).show();
                }else{
                    gender = (String) radioButton.getText();
                    description = information.getText().toString();
                    MarkersInformation markerInformation = new MarkersInformation(markerLatitude,markerLongitude,gender,locality,country,description);
                    database = FirebaseDatabase.getInstance("https://homelessapp-57cb8-default-rtdb.firebaseio.com/");
                    ref = database.getReference("Markers");
                    ref.push().setValue(markerInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MarkerInformation.this, "Registration Successful!", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                                Toast.makeText(MarkerInformation.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }


            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }
}