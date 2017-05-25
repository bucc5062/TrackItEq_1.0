package com.newproject.jhull3341.trackiteq;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jhull3341 on 3/31/2016.
 */
public class eqDatabaseService extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 11;
    private static final String TABLE_EQ_SESSIONS = "eqSessions_dt";
    private static final String TABLE_EQ_GPSPOSITION_MASTER = "eqGPSPosition_Mst";
    private static final String TABLE_EQ_GPSPOSITIONS = "eqGPSPositions_dt";
    private static final String TABLE_EQ_SETTINGS = "eqSettings_dt";
    private static final String TABLE_EQ_CUSTOM_GAITS = "eqCustomGaits_dt";
    private static final String DATABASE_NAME = "EqConditioning_db";
    private static final String eTAG = "Exception";

    private enum GPS_MASTER_COLUMNS {
        gps_session_id,
        gps_session_name,
        gps_session_date
    }
    private enum GPS_COLUMNS {
        gps_session_id,
        gps_row_num,
        gps_lat,
        gps_lon,
        gps_avgSpeed,
        gps_gpsSpeed,
        gps_spdCount,
        gps_positionDate,
        gps_bearing;
    }
    private enum GAITS_COLUMNS {
        Name,
        Category,
        Gait,
        Uom,
        Pace;
    }
    public eqDatabaseService(Context context, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.i(eTAG, "eqDatabaseService onCreate");
        createEQSessionsTable(db);
        createEQGPSPositionsMaster(db);
        createEQGPSPositionsTable(db);
        createEQSettingsTable(db);
        createEQCustomGaitsTable(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(eTAG, "eqDatabaseService onUpgrade");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQ_SESSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQ_GPSPOSITIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQ_GPSPOSITION_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQ_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQ_CUSTOM_GAITS);
        onCreate(db);
    }

    @Override
    public synchronized void close() {
        Log.i(eTAG, "eqDatabaseService close");
        super.close();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.i(eTAG, "eqDatabaseService onOpen");
        super.onOpen(db);
    }

    //region database table creation
    private void createEQSessionsTable(SQLiteDatabase db) {
        Log.i(eTAG, "createEQSessionsTable start");
        String CREATE_TABLE_EQ_SESSIONS = "CREATE TABLE " +
                TABLE_EQ_SESSIONS + "("
                + "session_name" + " TEXT,"
                + "session_element_number" + " INTEGER,"
                + "session_gait" + " TEXT,"
                + "session_time" + " INTEGER" + ")";

        db.execSQL(CREATE_TABLE_EQ_SESSIONS);

    }

    private void createEQGPSPositionsMaster(SQLiteDatabase db) {
        Log.i(eTAG, "createEQGPSPositionsMaster start");
        String CREATE_TABLE_EQ_GPSPOSITIONS = "CREATE TABLE " +
                TABLE_EQ_GPSPOSITION_MASTER + " ("
                + "gps_session_id" + " INTEGER PRIMARY KEY,"
                + "gps_session_name" + " TEXT,"
                + "gps_date_created" + " LONG)";

        try {
            db.execSQL(CREATE_TABLE_EQ_GPSPOSITIONS);
        } catch (Exception ex) {
            Log.i(eTAG, ex.getStackTrace().toString());
            Log.i(eTAG, "createEQGPSPositionsMaster error");
        }


    }
    private void createEQGPSPositionsTable(SQLiteDatabase db) {
        Log.i(eTAG, "createEQGPSPositionsTable start");
        String CREATE_TABLE_EQ_GPSPOSITIONS = "CREATE TABLE " +
                TABLE_EQ_GPSPOSITIONS + "("
                + "gps_session_id" + " INTEGER KEY,"
                + "gps_row_num" + " INTEGER,"
                + "gps_lat" + " TEXT,"
                + "gps_lon" + " TEXT,"
                + "gps_avgSpeed" + " INTEGER,"
                + "gps_gpsSpeed" + " INTEGER,"
                + "gps_spdCount" + " INTEGER,"
                + "gps_positionDate" + " LONG,"
                + "gps_bearing" + " INTEGER" + ")";

        db.execSQL(CREATE_TABLE_EQ_GPSPOSITIONS);
        ;
    }
    private void createEQSettingsTable(SQLiteDatabase db) {
        Log.i(eTAG, "createEQSettingsTable start");
        String CREATE_TABLE_EQ_SETTINGS = "CREATE TABLE " +
                TABLE_EQ_SETTINGS + "("
                + "settings_key" + " TEXT,"
                + "settings_value" + " TEXT" + ")";

        db.execSQL(CREATE_TABLE_EQ_SETTINGS);

        // now add the default settings for equines
        // SQLiteDatabase dbw = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("settings_key","Reminder Volume");
        values.put("settings_value","true");

        db.insert(TABLE_EQ_SETTINGS, null, values);

        values.put("settings_key","Transition Warning Time");
        values.put("settings_value","5");

        db.insert(TABLE_EQ_SETTINGS, null, values);

        values.put("settings_key","language");
        values.put("settings_value","UK");

        db.insert(TABLE_EQ_SETTINGS, null, values);

        values.put("settings_key","Display PACE UOM");
        values.put("settings_value","mpm");

        db.insert(TABLE_EQ_SETTINGS, null, values);
        values.put("settings_key","Reminder Interval");
        values.put("settings_value","3");

        db.insert(TABLE_EQ_SETTINGS, null, values);

        //db.close();

    }
    private void createEQCustomGaitsTable(SQLiteDatabase db) {
        Log.i(eTAG, "createEQCustomGaitsTable start");
        String CREATE_TABLE_EQ_CUSTOM_GAITS = "CREATE TABLE " +
                TABLE_EQ_CUSTOM_GAITS + "("
                + "custom_name" + " TEXT,"
                + "custom_category" + " TEXT,"
                + "custom_gait" + " TEXT,"
                + "custom_uom" + " TEXT,"
                + "custom_pace" + " INTEGER" + ")";

        db.execSQL(CREATE_TABLE_EQ_CUSTOM_GAITS);

        // now add the default gaits for equines

        //SQLiteDatabase dbw = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("custom_name","Equine Riding");
        values.put("custom_category","Equine");
        values.put("custom_gait","Walk");
        values.put("custom_uom","mpm");
        values.put("custom_pace","107");

        db.insert(TABLE_EQ_CUSTOM_GAITS, null, values);

        values.put("custom_name","Equine Riding");
        values.put("custom_category","Equine");
        values.put("custom_gait","Trot");
        values.put("custom_uom","mpm");
        values.put("custom_pace","220");

        db.insert(TABLE_EQ_CUSTOM_GAITS, null, values);

        values.put("custom_name","Equine Riding");
        values.put("custom_category","Equine");
        values.put("custom_gait","Beginner Novice");
        values.put("custom_uom","mpm");
        values.put("custom_pace","350");

        db.insert(TABLE_EQ_CUSTOM_GAITS, null, values);
        values.put("custom_name","Equine Riding");
        values.put("custom_category","Equine");
        values.put("custom_gait","Novice");
        values.put("custom_uom","mpm");
        values.put("custom_pace","380");

        db.insert(TABLE_EQ_CUSTOM_GAITS, null, values);
        values.put("custom_name","Equine Riding");
        values.put("custom_category","Equine");
        values.put("custom_gait","Training");
        values.put("custom_uom","mpm");
        values.put("custom_pace","425");

        db.insert(TABLE_EQ_CUSTOM_GAITS, null, values);

        values.put("custom_name","Equine Riding");
        values.put("custom_category","Equine");
        values.put("custom_gait","Prelim");
        values.put("custom_uom","mpm");
        values.put("custom_pace","525");

        db.insert(TABLE_EQ_CUSTOM_GAITS, null, values);

        //.close();

    }
    //endregion

    public void deleteEQDatabase() {


        Log.i(eTAG, "eqDatabaseService deleteEQDatabase");
    }

    // region public functions GPS

    public void insertCurrentGPSData(String planName,ArrayList<eqGPSPositions_dt> allPoints) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {

            db.beginTransaction();

            // set up a master row to indentify the GPS data
            Integer sessionId = insertCurrentGPSMaster(planName);
            Integer rowNumber = 0;

            // now process all the gps dat  into the DB table
            for (eqGPSPositions_dt gpsDatum : allPoints) {
                ContentValues values = new ContentValues();

                values.put("gps_session_id",sessionId);
                values.put("gps_row_num",rowNumber);
                values.put("gps_lat",gpsDatum.get_lat());
                values.put("gps_lon",gpsDatum.get_lon());
                values.put("gps_avgSpeed",gpsDatum.getAvgSpeed());
                values.put("gps_gpsSpeed",gpsDatum.getGpsSpeed());
                values.put("gps_spdCount",gpsDatum.getSpdCount());
                values.put("gps_positionDate",DateTextToLong(gpsDatum.getPositionDate()));
                values.put( "gps_bearing",gpsDatum.getBearing());

                db.insert(TABLE_EQ_GPSPOSITIONS, null, values);

                rowNumber += 1;

            }
        } catch (Exception ex) {
            db.endTransaction();
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }

    }
    public void deleteCurrentGPSData(String planName, String createDate) {

        SQLiteDatabase db = this.getWritableDatabase();

        Integer sessionID = getSessionID(planName,createDate);

        db.delete(TABLE_EQ_SESSIONS,"gps_session_id=?",new String[] {sessionID.toString()});

    }
    public ArrayList<eqGPSPositions_dt> getGPSData(String planName, String createDate) {

        Integer sessionID = getSessionID(planName,createDate);

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * from " + TABLE_EQ_GPSPOSITIONS + " WHERE gps_session_id = " + sessionID.toString() + " ORDER BY gps_row_num";

        Cursor cursor = db.rawQuery(sql,null);
        ArrayList<eqGPSPositions_dt> allPoints = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                eqGPSPositions_dt apoint = new eqGPSPositions_dt();

                apoint.set_sessionID(cursor.getInt(GPS_COLUMNS.gps_session_id.ordinal()));
                apoint.set_rowNumber(cursor.getInt(GPS_COLUMNS.gps_row_num.ordinal()));
                apoint.set_lat(cursor.getDouble(GPS_COLUMNS.gps_lat.ordinal()));
                apoint.set_lon(cursor.getDouble(GPS_COLUMNS.gps_lon.ordinal()));
                apoint.setAvgSpeed(cursor.getLong(GPS_COLUMNS.gps_avgSpeed.ordinal()));
                apoint.setGpsSpeed(cursor.getLong(GPS_COLUMNS.gps_gpsSpeed.ordinal()));
                apoint.setSpdCount(cursor.getLong(GPS_COLUMNS.gps_spdCount.ordinal()));
                apoint.setPositionDate(DateLongToText(cursor.getLong(GPS_COLUMNS.gps_positionDate.ordinal())));
                apoint.setBearing(cursor.getInt(GPS_COLUMNS.gps_bearing.ordinal()));

                allPoints.add(apoint);

            } while (cursor.moveToNext());
        }

        return allPoints;
    }
    public ArrayList<eqGPSDataMaster> getGPSList() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_EQ_GPSPOSITION_MASTER + " ORDER BY gps_date_created DESC";

        try {
             cursor = db.rawQuery(selectQuery,null);
        } catch (Exception ex) {
            Log.i(eTAG, "Ex: " + ex.getStackTrace().toString());
            Log.i(eTAG, "eqDatabaseService error");
        }


        ArrayList<eqGPSDataMaster> allRuns = new ArrayList<>();   // create a holder for all the plans

        // process through the retrieved grouped data and prepare a list
        if (cursor.moveToFirst()) {
            do {
                eqGPSDataMaster aline = new eqGPSDataMaster();

                aline.set_gps_session_id(cursor.getInt(GPS_MASTER_COLUMNS.gps_session_id.ordinal()));
                aline.set_gps_session_name(cursor.getString(GPS_MASTER_COLUMNS.gps_session_name.ordinal()));
                aline.set_gps_session_date(DateLongToText(cursor.getLong(GPS_MASTER_COLUMNS.gps_session_date.ordinal())));

                allRuns.add(aline);                                   // add the row to the list

            } while (cursor.moveToNext());
        }
        return allRuns;
    }

    // private functions used by the public methods
    private Integer insertCurrentGPSMaster(String PlanName) {

        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());

        Integer nextID = getNextSessionID();

        ContentValues values = new ContentValues();

        values.put("gps_session_name",PlanName);
        values.put("gps_session_id",nextID);
        values.put("gps_date_created",DateTextToLong(currentDateandTime));

        db.insert(TABLE_EQ_GPSPOSITION_MASTER, null, values);

        return nextID;
    }
    private Integer getSessionID(String planName, String createDate) {

        SQLiteDatabase db = this.getWritableDatabase();

        Long lDate = DateTextToLong(createDate);

        String sql = "SELECT gps_session_id from " + TABLE_EQ_GPSPOSITION_MASTER + " WHERE gps_session_name = '" + planName + "' AND gps_date_created = " + lDate.toString() + "";
        Integer sessionID = 0;

        try{
            Cursor cursor = db.rawQuery(sql,null);
            if (cursor != null) {
                cursor.moveToFirst();
                sessionID = cursor.getInt(0);
            }
        } catch (Exception ex) {
            sessionID = 1;
        }

        return sessionID;

    }
    private Integer getNextSessionID() {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * from " + TABLE_EQ_GPSPOSITION_MASTER + " ORDER BY gps_session_id DESC";
        Integer nextID = 0;

        try{
            Cursor cursor = db.rawQuery(sql,null);
            if (cursor != null) {
                cursor.moveToFirst();
                Log.i(eTAG, "cursor(0): " + cursor.getInt(0));
                Log.i(eTAG, "cursor(1): " + cursor.getInt(1));
                Log.i(eTAG, "cursor(2): " + cursor.getInt(2));
                nextID = cursor.getInt(0);
                nextID += 1;
            }
        } catch (Exception ex) {
            nextID = 1;
        }

        return nextID;

    }
    private String DateLongToText(long inVal) {

        String dateString = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(inVal));

        return dateString;

    }
    private Long DateTextToLong(String inval) {

        long dateLong = 0;

        try {
            dateLong = new SimpleDateFormat("yyyyMMdd_HHmmss").parse(inval).getTime();

        } catch (ParseException Pex) {

        }

        return dateLong;

    }
    //endregion
    //region public functions Plans

    public void insertCurrentPlanElement(eqSessions_dt plan) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("session_name",plan.get_planName());
        values.put("session_element_number",plan.get_elementNumber());
        values.put("session_gait",plan.get_gait());
        values.put("session_time",plan.get_time());

        db.insert(TABLE_EQ_SESSIONS, null, values);

    }
    public void deleteCurrentPlan(String planName) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EQ_SESSIONS,"session_name=?",new String[] {planName});

    }
    public ArrayList<eqSessions_dt> getCurrentPlan(String planName) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_EQ_SESSIONS + " WHERE session_name = '" + planName + "' ORDER BY session_element_number";

        Cursor cursor = db.rawQuery(selectQuery,null);
        ArrayList<eqSessions_dt> allElements = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                eqSessions_dt element = new eqSessions_dt();
                element.set_planName(cursor.getString(0));
                element.set_elementNumber(cursor.getInt(1));
                element.set_gait(cursor.getString(2));
                element.set_time(cursor.getInt(3));

                allElements.add(element);

            } while (cursor.moveToNext());
        }
        return allElements;
    }
    public ArrayList<HashMap<String, String>> getPlanList() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT session_name, count(session_name) as numElements FROM " + TABLE_EQ_SESSIONS + " GROUP BY session_name";

        Cursor cursor = db.rawQuery(selectQuery,null);

        ArrayList<HashMap<String, String>> allPlans = new ArrayList<>();   // create a holder for all the plans

        // process through the retrieved grouped data and prepare a list
        HashMap<String, String> planLine;
        if (cursor.moveToFirst()) {
            do {

                planLine = new HashMap<String, String>();               // create a holder for a row
                planLine.put("keyName",cursor.getString(0));                  // put the col data in
                planLine.put("keyENum", Integer.toString(cursor.getInt(1)));      // put the col data in

                allPlans.add(planLine);                                   // add the row to the list

            } while (cursor.moveToNext());
        }
            return allPlans;
    }

    //endregion
    //region public functions Gaits

    public void insertCustomGait(eqCustomGaits_dt gait) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("custom_name",gait.getName());
        values.put("custom_category",gait.getCategory());
        values.put("custom_gait",gait.getGait());
        values.put("custom_uom",gait.getUom());
        values.put("custom_pace",gait.getPace());

        db.insert(TABLE_EQ_CUSTOM_GAITS, null, values);

    }
    public void deleteCustomGait(String gaitName) {

        SQLiteDatabase db = this.getWritableDatabase();

        String sqlDeleteGait = " DELETE FROM " + TABLE_EQ_CUSTOM_GAITS + " WHERE custom_name = '" + gaitName + "'";
        db.delete(TABLE_EQ_CUSTOM_GAITS,"custom_gait=?",new String[] {gaitName});
    }
    public void clearAllGaits() {

        SQLiteDatabase db = this.getWritableDatabase();

        Log.i(eTAG, "clearAllGaits");

        String sqlDelete = "DELETE FROM " + TABLE_EQ_CUSTOM_GAITS;
        db.execSQL(sqlDelete);
    }
    public List<eqCustomGaits_dt> getAllGaits() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_EQ_CUSTOM_GAITS + " ORDER BY custom_pace";

        Cursor cursor = db.rawQuery(selectQuery,null);
        List<eqCustomGaits_dt> allGaits = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                eqCustomGaits_dt customGaits = new eqCustomGaits_dt();
                customGaits.setName(cursor.getString(GAITS_COLUMNS.Name.ordinal()));
                customGaits.setCategory(cursor.getString(GAITS_COLUMNS.Category.ordinal()));
                customGaits.setGait(cursor.getString(GAITS_COLUMNS.Gait.ordinal()));
                customGaits.setUom(cursor.getString(GAITS_COLUMNS.Uom.ordinal()));
                customGaits.setPace(cursor.getInt(GAITS_COLUMNS.Pace.ordinal()));

                allGaits.add(customGaits);

            } while (cursor.moveToNext());
        }

        return allGaits;

    }

    //endregion


}

