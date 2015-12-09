package com.newproject.jhull3341.trackiteq;
import android.location.Location;
import static android.location.Location.*;
import android.util.Log;

/**
 * Created by jhull3341 on 12/8/2015.
 */
public class gpsLocator {

    public static Location locPrev = null;
    public static float[] d2 = {};
    private final static String eTAG = "Exception";

    public gpsLocator() {

    }
    public static int LocationChanged(Location locCurr, String units) {

        Double speed = 0.0;

        if (locPrev == null) {
            locPrev = locCurr;
        } else {
            Double[][] positions = {};
            Long[] times = {};

             //	String speed_string = "";
            Double d1;
            Long t1;

            d1 = 0.0;   // represents distance traveled between two locations
            t1 = 0l;    // represents the time traveled between two locations


            Log.i(eTAG,"PrevPos: " + locPrev.getLatitude() + " " + locPrev.getLongitude());
            Log.i(eTAG,"currPos: " + locCurr.getLatitude() + " " + locCurr.getLongitude());

            // just for fun, see if we get distance this way
            if (locPrev != null) {
                try {
                    distanceBetween(locCurr.getLatitude(), locCurr.getLongitude(), locPrev.getLatitude(), locPrev.getLongitude(), d2);
                } catch (Exception ex) {
                    Log.i(eTAG,ex.getMessage());
              //     return 0;
                }
            }

            if (locCurr.hasSpeed()) {
                speed = locCurr.getSpeed() * 1.0; // need to * 1.0 to get into a double for some reason...
            } else {
                try {
                    // get the distance and time between the current position, and the previous position.
                    // using (counter - 1) % data_points doesn't wrap properly
                    d1 = distance(locCurr.getLatitude(), locCurr.getLongitude(), locPrev.getLatitude(), locPrev.getLongitude());
                    t1 = locCurr.getTime() - locPrev.getTime();
                } catch (NullPointerException e) {
                    //all good, just not enough data yet.
                }
                Log.i(eTAG,"d1:" + d1);
                Log.i(eTAG,"t1:" + t1);
                Log.i(eTAG,"speed: " + speed);
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
        return (int) l;
    }

    // private functions
    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        // have a sine great circle distance approximation, returns meters
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60; // 60 nautical miles per degree of separation
        dist = dist * 1852; // 1852 meters per nautical mile
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
