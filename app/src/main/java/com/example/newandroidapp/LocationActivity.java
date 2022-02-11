package com.example.newandroidapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback{
        private GoogleMap mMap;
        //private ActivityLocationBinding binding;
        private double lat, lon;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            /*binding = ActivityLocationBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());*/

            Intent intent = getIntent();
            lat = intent.getDoubleExtra("Lat",0);
            lon = intent.getDoubleExtra("Lon",0);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            LatLng momentalnaLokacija = new LatLng(lat, lon);
            mMap.addMarker(new MarkerOptions().position(momentalnaLokacija).title("Your location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(momentalnaLokacija));
            mMap.setMinZoomPreference(15);
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Your location"));
                    mMap.setMinZoomPreference(15);
                    Intent intent1 = new Intent(LocationActivity.this,AddNewProduct.class);
                    intent1.putExtra("NewLat", latLng.latitude);
                    intent1.putExtra("NewLon", latLng.longitude);
                    startActivity(intent1);
                }
            });
        }
}