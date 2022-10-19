package com.example.homelessapp.Adapters;

import static java.lang.Math.round;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.homelessapp.DTO.MarkersInformation;
import com.example.homelessapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context mContext;
    private final View mWindow;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private double distanceFromMarker;

    public CustomInfoWindowAdapter(Context context, double distance) {
        mContext = context;
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.info_window,null);
        this.distanceFromMarker = distance;
    }

    private void windowText(Marker marker, View view){
        ImageView gender = mWindow.findViewById(R.id.genderImage);
        TextView location = mWindow.findViewById(R.id.locationText);
        TextView distance = mWindow.findViewById(R.id.distance);
        String id = marker.getTitle();

        if(distanceFromMarker > 1000){
            String distanceText = String.format("%.02f",distanceFromMarker/1000);
            distance.setText(distanceText + " km");
        }else{
            distance.setText(String.valueOf(round(distanceFromMarker))+" m");
        }

        database = FirebaseDatabase.getInstance("https://homelessapp-57cb8-default-rtdb.firebaseio.com/");
        ref = database.getReference("Markers");
        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MarkersInformation markersInformation = snapshot.getValue(MarkersInformation.class);
                location.setText(markersInformation.getLocality()+", "+markersInformation.getCountry());
                if(markersInformation.getGender().equals("Male")){
                    gender.setImageResource(R.drawable.male);
                }else{
                    gender.setImageResource(R.drawable.female);
                }
                if(marker != null && marker.isInfoWindowShown()) {
                    marker.showInfoWindow();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        windowText(marker,mWindow);
        return mWindow;


    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        windowText(marker,mWindow);
        return mWindow;
    }
}
