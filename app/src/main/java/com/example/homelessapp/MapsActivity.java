package com.example.homelessapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.homelessapp.Adapters.CustomInfoWindowAdapter;
import com.example.homelessapp.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    double currentLat = 0, currentLong = 0;
    private Dialog dialog;
    LatLng latLng;
    Button cancel;
    Marker mMarker;
    TextView distance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        distance = (TextView) findViewById(R.id.distance);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        zoom();

        //Import the design style of the map
        boolean success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.stylemap));

        mMap.setOnMarkerClickListener(this);



        //Check if permission of location is enabled else it asks from user for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
            zoom();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            }
        }

        database = FirebaseDatabase.getInstance("https://homelessapp-57cb8-default-rtdb.firebaseio.com/");
        ref = database.getReference("Markers");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    String x = (String) s.child("latitude").getValue();
                    String y = (String) s.child("longitude").getValue();

                    BitmapDrawable markerdrawble = (BitmapDrawable)getResources().getDrawable(R.drawable.homelessmarker);
                    Bitmap b = markerdrawble.getBitmap();
                    Bitmap marker = Bitmap.createScaledBitmap(b, 100, 100, false);

                    mMarker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(x), Double.parseDouble(y)))
                            .title(s.getKey())
                            .icon(BitmapDescriptorFactory.fromBitmap(marker)));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //TODO Add map snapshot to MoreInformation class
       mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
           @Override
           public void onInfoWindowClick(@NonNull Marker marker) {
               Intent intent = new Intent(MapsActivity.this,MoreInformation.class);
               intent.putExtra("markerID",marker.getTitle());
               intent.putExtra("lat",String.valueOf(latLng.latitude));
               intent.putExtra("long",String.valueOf(latLng.longitude));
               startActivity(intent);

           }
       });



    }

    @SuppressLint("MissingPermission")
    private void enableUserLocation() {
        mMap.setMyLocationEnabled(true);
    }

    @SuppressLint("MissingPermission")
    private void zoom() {
       Task<Location> locationTask = mFusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                latLng = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
            }
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        zoom();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                zoom();
            } else {

            }
        }
    }


    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        calculateDistance(latLng,marker);

        CustomInfoWindowAdapter customInfoWindow = new CustomInfoWindowAdapter(this,calculateDistance(latLng,marker));
        mMap.setInfoWindowAdapter(customInfoWindow);
        return false;
    }


    //Find the distance between the user location and the clicked marker using the function distanceTo
    //Setting the start point which is the user location and the second point which is the marker we want to see the distance
    private double calculateDistance(LatLng userLocation,Marker marker){
        Location startPoint = new Location("Location A");
        startPoint.setLatitude(marker.getPosition().latitude);
        startPoint.setLongitude(marker.getPosition().longitude);

        Location endPoint = new Location("Location B");
        endPoint.setLatitude(userLocation.latitude);
        endPoint.setLongitude(userLocation.longitude);

        double distanceFromMarker = startPoint.distanceTo(endPoint);

        return distanceFromMarker;
    }
}