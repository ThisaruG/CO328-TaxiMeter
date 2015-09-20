package com.thisaru.co328.taximeter;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

/**
 * Created by Sachin on 8/23/2015.
 */
public class Calculator extends Thread {
    private double roadCondition;
    private double distance;

    //private Database db;

    private double time;

    private double start_time;
    private double end_time;
    private double Travel_time;

    private LatLng startPoint;
    private LatLng stopPoint;

    private String startTown = "Peradeniya";

    public String getStopTown() {
        return stopTown;
    }

    public String getStartTown() {

        return startTown;
    }

    private String stopTown = "Peradeniya";

    private int initial = 50;
    private double fair;

    Calendar calendar = Calendar.getInstance();
    private int currentHour;
    MapsActivity mapsActivity;

    public Calculator(LatLng startPoint, LatLng stopPoint, double start_time , double end_time){

        /*switch (roadCondition) {
            case "1 - Great":
                roadConditionDouble = 1.0;
                break;
            case "2 - Good":
                roadConditionDouble = 1.2;
                break;
            case "3 - Bad":
                roadConditionDouble = 1.3;
                break;
            case "4 - Poor":
                roadConditionDouble = 1.5;
                break;
        };*/
        //mapsActivity.getTown(pera);
        //this.startTown = mapsActivity.getTown();

        //mapsActivity.getTown(pera);
        //this.stopTown = mapsActivity.getTown();

        roadCondition = 1.0;//db.getRoadCondition(startTown, stopTown);

        this.startPoint = startPoint;
        this.stopPoint = stopPoint;
        this. start_time = start_time;
        this. end_time = end_time;
    }

    public double getFair(){
        calc_fair();
        return fair;
    }

    public double getRoadCondition(String startTown, String stopTown){
        return roadCondition;
    }

    public double getDistance(){
        calc_distance();
        return distance;
    }

    public double getTime(){
        return time;
    }

    public double Travel_time(){
        return Travel_time;
    }

    private void calc_fair(){
        //calculating fair
        //considering the distance, the fair
        if(distance < (double) 1000){
            fair = (double) initial;
        }
        else fair = (double) initial + (distance - 1000)* (double)40;

        //considering the road condition the fair multiplied by the factor
        fair = fair * roadCondition;

        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        //considering the time from 8 pm to 5 am, 1.5 times the fair
        if(currentHour < 19 && currentHour > 6) {
            fair = fair* (double)1;
        } else {
            fair = fair * 1.5;
        }

        // if travel time is more than half and hour the amount multiplies by 1.1
        if(Travel_time > 30 * 60 * 1000) fair = fair * (double)1.1;
    }
    private void calc_distance(){
        distance = helpDistance(startPoint.latitude , startPoint.longitude , stopPoint.latitude , stopPoint.longitude);
    }

    private double helpDistance(double lat_a, double lng_a, double lat_b, double lng_b) {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance1 = earthRadius * c;

        Log.d("Distance", "Distance: " + distance1);
        int meterConversion = 1609;

        return (distance1 * meterConversion);
    }
}
