package com.example.homelessapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Locale;

public class AddHomelessPin extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    GoogleMap mMap;
    SupportMapFragment supportMapFragment;
    Button next;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_homeless_pin);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.addPinMap);
        supportMapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        next = findViewById(R.id.nextButton);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng center = mMap.getCameraPosition().target;
                String markerLatitude = String.valueOf(center.latitude);
                String markerLongitude = String.valueOf(center.longitude);
                String locality = "",country="",address="";
                Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = geoCoder.getFromLocation(center.latitude, center.longitude, 1);
                    if (addresses.size() > 0) {

                        locality = addresses.get(0).getLocality();
                        country = addresses.get(0).getCountryName();;
                        if(locality==null)
                            locality ="-";
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(AddHomelessPin.this, MarkerInformation.class);
                Bundle parseValues = new Bundle();
                parseValues.putString("markerLatitude",markerLatitude);
                parseValues.putString("markerLongitude",markerLongitude);
                parseValues.putString("locality",locality);
                parseValues.putString("country",country);
                intent.putExtras(parseValues);

                CaptureMapScreen();
                finish();
                startActivity(intent);


            }
        });


    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        zoom();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        zoom();

        boolean success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.stylemap));

    }

    @SuppressLint("MissingPermission")
    private void zoom() {
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
            }
        });
    }

    public void CaptureMapScreen(){
        GoogleMap.SnapshotReadyCallback callback = new com.androidmapsextensions.GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;
            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                bitmap = snapshot;
                try{
                    FileOutputStream out = new FileOutputStream("/mnt/sdcard/MyMapScreen"+System.currentTimeMillis()+".png");
                    bitmap.compress(Bitmap.CompressFormat.PNG,90,out);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        mMap.snapshot(callback);
    }

}