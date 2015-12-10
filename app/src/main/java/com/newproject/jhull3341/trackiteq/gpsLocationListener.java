package com.newproject.jhull3341.trackiteq;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;

import static android.location.Location.distanceBetween;

/**
 * Created by jhull3341 on 12/10/2015.
 */
public class gpsLocationListener {
    private final static int LOCATION_REQUEST_INTERVAL = 5000;
    private final static int LOCATION_REQUEST_FASTEST_INTERVAL = 1000;

    private iGSPActivity main;
    private Context me;
    private LocationManager mlocManager;
    private LocationListener mlocListener;
    private LocationRequest mLocationRequest;

    public  Location locPrev = null;
    public  Location locCurr = null;
    public  String units = "mpm";
    public float[] d2 = {};
    public int currSpeed;
    public int Status;
    public String Provider;
    private boolean isRunning;
    private int hasFineLocationAccess;
    private int hasCourseLocationAccess;
    private final static String eTAG = "Exception";
    private static final long MIN_TIME_BW_UPDATES = 1000;
    public gpsLocationListener(iGSPActivity main, Context me) {
        this.me = me;
        this.main = main;
        isRunning = true;
        Log.i(eTAG, "gpsLocationListener");
        // GPS Position
        mlocManager = (LocationManager) ((Activity) this.main).getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();
        hasFineLocationAccess = ActivityCompat.checkSelfPermission(this.me, Manifest.permission.ACCESS_FINE_LOCATION);
        hasCourseLocationAccess = ActivityCompat.checkSelfPermission(this.me, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationAccess != PackageManager.PERMISSION_GRANTED && hasCourseLocationAccess != PackageManager.PERMISSION_GRANTED) {
        }
        //mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, 0, mlocListener);

        // GPS Position END
    }

    public void stopGPS() {
        Log.i(eTAG, "stopGPS");
        if (isRunning) {
            hasFineLocationAccess = ActivityCompat.checkSelfPermission(this.me, Manifest.permission.ACCESS_FINE_LOCATION);
            hasCourseLocationAccess = ActivityCompat.checkSelfPermission(this.me, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (hasFineLocationAccess != PackageManager.PERMISSION_GRANTED && hasCourseLocationAccess != PackageManager.PERMISSION_GRANTED) {
            }
            mlocManager.removeUpdates(mlocListener);
            this.isRunning = false;
        }
    }
    public void resumeGPS() {
        Log.i(eTAG, "resumeGPS");
        hasFineLocationAccess = ActivityCompat.checkSelfPermission(this.me, Manifest.permission.ACCESS_FINE_LOCATION);
        hasCourseLocationAccess = ActivityCompat.checkSelfPermission(this.me, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasFineLocationAccess != PackageManager.PERMISSION_GRANTED && hasCourseLocationAccess != PackageManager.PERMISSION_GRANTED) {
        }
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, 0, mlocListener);
        this.isRunning = true;
    }

    public class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Log.i(eTAG, "in class onLocationChanged");
            Double speed = 0.0;
            locCurr = location;

            if (locPrev == null) {
                locPrev = locCurr;
            } else {

                //	String speed_string = "";
                Double d1;
                Long t1;

                d1 = 0.0;   // represents distance traveled between two locations
                t1 = 0l;    // represents the time traveled between two locations

                Log.i(eTAG, "PrevPos: " + locPrev.getLatitude() + " " + locPrev.getLongitude());
                Log.i(eTAG, "currPos: " + locCurr.getLatitude() + " " + locCurr.getLongitude());

                // just for fun, see if we get distance this way
                if (locPrev != null) {
                    try {
                        distanceBetween(locCurr.getLatitude(), locCurr.getLongitude(), locPrev.getLatitude(), locPrev.getLongitude(), d2);
                    } catch (Exception ex) {
                        Log.i(eTAG, "error on method approach" + ex.getMessage());
                        //     return 0;
                    }
                }
                Log.i(eTAG,"Try calc distance");
                boolean hasSpeed;

                try {
                    hasSpeed = locCurr.hasSpeed();
                } catch (Exception ex) {
                    Log.i(eTAG, "error on hasSpeed" + ex.getMessage());
                    hasSpeed = false;
                }
                if (hasSpeed) {
                    speed = locCurr.getSpeed() * 1.0; // need to * 1.0 to get into a double for some reason...
                } else {
                    try {
                        // get the distance and time between the current position, and the previous position.
                        // using (counter - 1) % data_points doesn't wrap properly
                        d1 = distance(locCurr.getLatitude(), locCurr.getLongitude(), locPrev.getLatitude(), locPrev.getLongitude());
                        t1 = locCurr.getTime() - locPrev.getTime();
                    } catch (Exception ex) {
                        Log.i(eTAG, "error on method approach" + ex.getMessage());
                        //all good, just not enough data yet.
                    }
                    Log.i(eTAG, "d1:" + d1);
                    Log.i(eTAG, "t1:" + t1);
                    Log.i(eTAG, "speed: " + speed);
                    speed = d1 / t1; // m/s
                }

                // convert from m/s to specified units
                switch (units) {
                    case "kph":
                        speed = speed * 3.6d;
                        break;
                    case "mph":
                        speed = speed * 2.23693629d;
                        break;
                    case "knots":
                        speed = speed * 1.94384449d;
                        break;
                    case "mpm":
                        speed = speed * 60;  // 60 meters per minute = 1 meter per sec
                        break;
                    default:
                        //speed = speed;
                }
            }
            locPrev = locCurr;   // assign the current to the past and get ready for the next

            long l = Math.round(speed);
            currSpeed = (int) l;

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Status = status;
        }

        @Override
        public void onProviderEnabled(String provider) {
            Provider = provider + " enabled";
        }

        @Override
        public void onProviderDisabled(String provider) {
            Provider = provider + " disabled";
        }
    }
    // private functions
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        // have a sine great circle distance approximation, returns meters
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60; // 60 nautical miles per degree of separation
        dist = dist * 1852; // 1852 meters per nautical mile
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
