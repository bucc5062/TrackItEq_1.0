package com.newproject.jhull3341.trackiteq;

import android.os.Parcelable;

/**
 * Created by jhull3341 on 3/30/2016.
 */
public class eqSessions_dt implements Parcelable {

    public eqSessions_dt() {

    }
    protected eqSessions_dt(android.os.Parcel in) {
        _planName = in.readString();
        _elementNumber = in.readInt();
        _gait = in.readString();
        _time = in.readInt();
    }

    public static final Creator<eqSessions_dt> CREATOR = new Creator<eqSessions_dt>() {
        @Override
        public eqSessions_dt createFromParcel(android.os.Parcel in) {
            return new eqSessions_dt(in);
        }

        @Override
        public eqSessions_dt[] newArray(int size) {
            return new eqSessions_dt[size];
        }
    };
    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(_planName);
        dest.writeInt(_elementNumber);
        dest.writeString(_gait);
        dest.writeInt(_time);
    }

    public String get_planName() {
        return _planName;
    }
    public void set_planName(String _planName) {
        this._planName = _planName;
    }

    public int get_elementNumber() {
        return _elementNumber;
    }
    public void set_elementNumber(int _elementNumber) {
        this._elementNumber = _elementNumber;
    }

    public String get_gait() {
        return _gait;
    }
    public void set_gait(String _gait) {
        this._gait = _gait;
    }

    public int get_time() {
        return _time;
    }
    public void set_time(int _time) {
        this._time = _time;
    }

    private String _planName;
    private int _elementNumber;
    private String _gait;
    private int _time;


}
