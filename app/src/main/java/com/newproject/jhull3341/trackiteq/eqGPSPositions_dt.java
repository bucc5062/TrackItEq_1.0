package com.newproject.jhull3341.trackiteq;

/**
 * Created by jhull3341 on 3/30/2016.
 */
public class eqGPSPositions_dt {

    public String get_lat() {
        return _lat;
    }

    public void set_lat(String _lat) {
        this._lat = _lat;
    }

    public String get_lon() {
        return _lon;
    }

    public void set_lon(String _lon) {
        this._lon = _lon;
    }

    public int getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(int avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public int getGpsSpeed() {
        return gpsSpeed;
    }

    public void setGpsSpeed(int gpsSpeed) {
        this.gpsSpeed = gpsSpeed;
    }

    public int getSpdCount() {
        return spdCount;
    }

    public void setSpdCount(int spdCount) {
        this.spdCount = spdCount;
    }

    public String getPositionDate() {
        return positionDate;
    }

    public void setPositionDate(String positionDate) {
        this.positionDate = positionDate;
    }

    public int getBearing() {
        return bearing;
    }

    public void setBearing(int bearing) {
        this.bearing = bearing;
    }


    private String _lat;
    private String _lon;
    private int avgSpeed;
    private int gpsSpeed;
    private int spdCount;
    private String positionDate;
    private int bearing;

}
