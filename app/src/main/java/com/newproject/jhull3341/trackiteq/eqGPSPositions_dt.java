package com.newproject.jhull3341.trackiteq;

import android.os.Parcelable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

/**
 * Created by jhull3341 on 3/30/2016.
 */
public class eqGPSPositions_dt implements Parcelable {

    public eqGPSPositions_dt() {

    }

    protected eqGPSPositions_dt(android.os.Parcel in) {
        _sessionID = in.readInt();
        _rowNumber = in.readInt();
        _lat = in.readDouble();
        _lon = in.readDouble();
        avgSpeed = in.readLong();
        gpsSpeed = in.readLong();
        spdCount = in.readLong();
        positionDate = in.readString();
        bearing = in.readFloat();
    }

    public static final Creator<eqGPSPositions_dt> CREATOR = new Creator<eqGPSPositions_dt>() {
        @Override
        public eqGPSPositions_dt createFromParcel(android.os.Parcel in) {
            return new eqGPSPositions_dt(in);
        }

        @Override
        public eqGPSPositions_dt[] newArray(int size) {
            return new eqGPSPositions_dt[size];
        }
    };
    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {

        dest.writeInt(_sessionID);
        dest.writeInt(_rowNumber);
        dest.writeDouble(_lat);
        dest.writeDouble(_lon);
        dest.writeLong(avgSpeed);
        dest.writeLong(gpsSpeed);
        dest.writeLong(spdCount);
        dest.writeString(positionDate);
        dest.writeFloat(bearing);
    }
    public static String beanToString(Object object) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringEmp = new StringWriter();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.writeValue(stringEmp, object);
        return stringEmp.toString();
    }
    //*********************************************************************************
    public Integer get_sessionID() {
        return _sessionID;
    }
    public void set_sessionID(Integer _sessionID) {
        this._sessionID = _sessionID;
    }

    public Integer get_rowNumber() {
        return _rowNumber;
    }
    public void set_rowNumber(Integer _rowNumber) {
        this._rowNumber = _rowNumber;
    }

    public double get_lat() {
        return _lat;
    }
    public void set_lat(double _lat) {
        this._lat = _lat;
    }

    public double get_lon() {
        return _lon;
    }
    public void set_lon(double _lon) {
        this._lon = _lon;
    }

    public long getAvgSpeed() {
        return avgSpeed;
    }
    public void setAvgSpeed(long avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public long getGpsSpeed() {
        return gpsSpeed;
    }
    public void setGpsSpeed(long gpsSpeed) {
        this.gpsSpeed = gpsSpeed;
    }

    public long getSpdCount() {
        return spdCount;
    }
    public void setSpdCount(long spdCount) {
        this.spdCount = spdCount;
    }

    public String getPositionDate() {
        return positionDate;
    }
    public void setPositionDate(String positionDate) {
        this.positionDate = positionDate;
    }

    public float getBearing() {
        return bearing;
    }
    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    private Integer _sessionID;
    private Integer _rowNumber;
    private double _lat;
    private double _lon;
    private long avgSpeed;
    private long gpsSpeed;
    private long spdCount;
    private String positionDate;
    private float bearing;

}

