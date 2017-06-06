package com.newproject.jhull3341.trackiteq;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jhull3341 on 6/6/2017.
 *
 * This small class allows the passage od GPS data from one activity to another
 */

public class GPSData implements Parcelable {

    private List<eqGPSPositions_dt> objects;

    public GPSData() {

    }

    public GPSData(Parcel in) {

        objects = in.createTypedArrayList(eqGPSPositions_dt.CREATOR);
    }

    public static final Parcelable.Creator<GPSData> CREATOR = new Parcelable.Creator<GPSData>() {
        @Override
        public GPSData createFromParcel(Parcel in) {
            return new GPSData(in);
        }

        @Override
        public GPSData[] newArray(int size) {
            return new GPSData[size];
        }
    };
    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(objects);
    }


}
