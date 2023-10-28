package com.example.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap googleMap;

    private List<Marker> markers = new ArrayList<>();

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);



    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        // You can customize the map and add markers, polylines, etc. here.

        // Enable the My Location layer on the map
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
        googleMap.setMyLocationEnabled(true);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                // Clear existing markers
                Utils.clearMarkers(markers);

                // Add a marker at the clicked location
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title("Your Marker Title")
                        .snippet("Marker Description");

                // Add the new marker to the map and the list
                Marker newMarker = map.addMarker(markerOptions);
                markers.add(newMarker);
            }
        });

        // Set up a location change listener to zoom to the user's location
        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(@NonNull Location location) {
                // Get the user's current location
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                // Zoom to the user's location
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.5f));

                // Remove the listener to avoid unnecessary zooming
                googleMap.setOnMyLocationChangeListener(null);
            }
            });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    // Handle other lifecycle methods like onPause, onDestroy, etc.
}
