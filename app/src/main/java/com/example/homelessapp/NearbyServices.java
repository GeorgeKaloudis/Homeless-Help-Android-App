package com.example.homelessapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.homelessapp.SearchNearbyPlaces.GetNearbyPlacesData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.slider.Slider;

public class NearbyServices extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    BottomSheetBehavior bottomSheetBehavior;
    CardView hospital,police,market,pharmacy,bakery;
    Slider rangeSlider;
    TextView radiusText;
    GoogleMap mMap;
    SupportMapFragment supportMapFragment;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    double currentLat = 0, currentLong = 0;
    int proximity_radius = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_services);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_maps);
        supportMapFragment.getMapAsync(this);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

        LinearLayout linearLayout = findViewById(R.id.design_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);



        hospital = findViewById(R.id.hospital);
        pharmacy = findViewById(R.id.pharmachy);
        police = findViewById(R.id.police);
        market = findViewById(R.id.market);
        bakery = findViewById(R.id.bakery);
        rangeSlider = findViewById(R.id.range_slider);
        radiusText = findViewById(R.id.radius_text);

        rangeSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                mMap.clear();
                proximity_radius = Math.round(slider.getValue());
                radiusText.setText(Integer.toString(proximity_radius));
                drawCircle(new LatLng(currentLat,currentLong));
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run() {
                        mMap.clear();
                    }
                },2000);

            }
        });




        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPlaces("hospital");
            }
        });

        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPlaces("police");
            }
        });
        pharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPlaces("pharmacy");
            }
        });
        bakery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPlaces("bakery");
            }
        });
        market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPlaces("supermarket");
            }
        });




    }

    private void getCurrentLocation() {
        @SuppressLint("MissingPermission") Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentLat = location.getLatitude();
                    currentLong = location.getLongitude();
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            mMap = googleMap;
                            mMap.setMyLocationEnabled(true);
                            zoom();
                        }
                    });
                }

                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

            }
        });


    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();

            }
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        boolean success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.stylemap));

    }

    //Create the url we need for nearby search with parameters(user location,search radius,type of place)
    private String getUrl(double latitude, double longitude,String nearbyPlace){
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+proximity_radius);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyDt3eOnwUrOKyD2_9rRFZfn6GRlTr7Jd28");

        return googlePlaceUrl.toString();
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        zoom();
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

    //Save the url with the parameters that the user has selected for nearby search in an object of dataTransfer List
    private void searchPlaces(String placeType){
        mMap.clear();
        String url = getUrl(currentLat,currentLong,placeType);
        Object dataTransfer[] = new Object[4];
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;
        dataTransfer[2] = placeType;
        dataTransfer[3] = getApplicationContext();


        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(dataTransfer);

    }

    private void drawCircle(LatLng point){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(proximity_radius);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x31FFC526);

        // Border width of the circle
        circleOptions.strokeWidth(1);

        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);

    }
}