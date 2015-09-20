package com.thisaru.co328.taximeter;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.SyncStateContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng myLocation;
    MyLocationListener locationListener = new MyLocationListener();
    private double latitude, longitude;
    private LatLng pera = new LatLng(7.253937, 80.592111);

    public String getTown() {
        getTown(getCurrentLocation());
        return town;
    }

    private String town;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener ll = new MyLocationListener();

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ll);
    }

    public void getTown (LatLng latlng) {
        double latitude = latlng.latitude;
        double longitude = latlng.longitude;
        Geocoder gcd = new Geocoder(MapsActivity.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) town = (addresses.get(0).getLocality());
        else town = "Peradeniya";
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
    }

    public LatLng getCurrentLocation() {
        return pera;
    }

    private class MyLocationListener implements LocationListener {
        double latitude = 7.253937, longitude = 80.592111, altitude;
        double speed;

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                // ---Get current location latitude, longitude, altitude & speed ---

                latitude = location.getLatitude();
                longitude = location.getLongitude();
                speed = location.getSpeed();
                altitude = location.getAltitude();
                getTown(new LatLng(latitude, longitude));
            }
        }

        public double getLatitude () {
            return latitude;
        }

        public double getLongitude () {
            return  longitude;
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}