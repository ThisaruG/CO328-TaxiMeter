package com.thisaru.co328.taximeter;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private long startTime, endTime;
    private LatLng startPoint, stopPoint;
    private String roadType;
    private MapsActivity mapsActivity = new MapsActivity();
    private Calculator calculator;
    private TextView distanceView, fareView, sourceView, destinationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapsActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startRide(View view) {
        // Check whether the GPS is on or off
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //roadType = getRoadType();
            startTime = System.currentTimeMillis();

            if (mapsActivity.getCurrentLocation() != null) {
                startPoint = mapsActivity.getCurrentLocation();
            } else {
                startPoint = new LatLng(7.253937, 80.592111);
            }

            distanceView = (TextView)findViewById(R.id.distanceValue);
            distanceView.setText(null);
            fareView = (TextView) findViewById(R.id.feeValue);
            fareView.setText(null);
            sourceView = (TextView)findViewById(R.id.source);
            sourceView.setText(null);
            destinationView = (TextView)findViewById(R.id.destination);
            destinationView.setText(null);
        } else {
            GPSAlert();
        }
    }

    public void stopRide (View view) {
        endTime = System.currentTimeMillis();

        if (mapsActivity.getCurrentLocation() != null) {
            stopPoint = mapsActivity.getCurrentLocation();
        } else {
            stopPoint = new LatLng(7.253937, 80.592111);
        }
        calculator = new Calculator(startPoint, stopPoint, startTime, endTime);
        distanceView = (TextView)findViewById(R.id.distanceValue);
        distanceView.setText(String.valueOf(calculator.getDistance()));
        fareView = (TextView) findViewById(R.id.feeValue);
        fareView.setText(String.valueOf(calculator.getFair()));
        sourceView = (TextView)findViewById(R.id.source);
        sourceView.setText(String.valueOf(calculator.getStartTown()));
        destinationView = (TextView)findViewById(R.id.destination);
        destinationView.setText(String.valueOf(calculator.getStopTown()));
        //Toast.makeText(getApplicationContext(), ("Distance: " + calculator.getDistance() + " " + "Fare: " + calculator.getFair()), Toast.LENGTH_LONG).show();
    }

    /*private String getRoadType () {
        RadioGroup roadCondition = (RadioGroup) findViewById(R.id.roadCondition);
        if(roadCondition.getCheckedRadioButtonId()!= -1) {
            int id = roadCondition.getCheckedRadioButtonId();
            View radioButton = roadCondition.findViewById(id);
            int radioId = roadCondition.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) roadCondition.getChildAt(radioId);
            return (String)btn.getText();
        }
        else {
            return "1 - Great";
        }
    }*/

    private void GPSAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Location Services are currently switched off. Do you want to turn it on ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}