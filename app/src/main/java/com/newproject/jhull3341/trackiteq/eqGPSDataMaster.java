package com.newproject.jhull3341.trackiteq;

/**
 * Created by jhull3341 on 5/22/2017.
 */

public class eqGPSDataMaster {

    public eqGPSDataMaster() {

    }

    public int get_gps_session_id() {
        return _gps_session_id;
    }

    public void set_gps_session_id(int _gps_session_id) {
        this._gps_session_id = _gps_session_id;
    }

    public String get_gps_session_name() {
        return _gps_session_name;
    }

    public void set_gps_session_name(String _gps_session_name) {
        this._gps_session_name = _gps_session_name;
    }

    public String get_gps_session_date() {
        return _gps_session_date;
    }

    public void set_gps_session_date(String _gps_session_date) {
        this._gps_session_date = _gps_session_date;
    }

    private int _gps_session_id;
    private String _gps_session_name;
    private String _gps_session_date;

}
