package com.newproject.jhull3341.trackiteq;

/**
 * Created by jhull3341 on 3/30/2016.
 */
public class eqSessions_dt {

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
