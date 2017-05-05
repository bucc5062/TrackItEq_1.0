package com.newproject.jhull3341.trackiteq;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhull3341 on 3/31/2016.
 */
public class eqDatabaseService extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_EQ_SESSIONS = "eqSessions_dt";
    private static final String TABLE_EQ_GPSPOSITIONS = "eqGPSPositions_dt";
    private static final String TABLE_EQ_SETTINGS = "eqSettings_dt";
    private static final String TABLE_EQ_CUSTOM_GAITS = "eqCustomGaits_dt";
    private static final String DATABASE_NAME = "EqConditioning_db";
    private static final String eTAG = "Exception";

    public eqDatabaseService(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.i(eTAG, "eqDatabaseService onCreate");
        createEQSessionsTable(db);
        createEQGPSPositionsTable(db);
        createEQSettingsTable(db);
        createEQCustomGaitsTable(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(eTAG, "eqDatabaseService onUpgrade");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQ_SESSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQ_GPSPOSITIONS);
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
                + "session_name" + " TEXT PRIMARY KEY,"
                + "session_gait" + " TEXT,"
                + "session_time" + " INTEGER" + ")";

        db.execSQL(CREATE_TABLE_EQ_SESSIONS);

    }
    private void createEQGPSPositionsTable(SQLiteDatabase db) {
        Log.i(eTAG, "createEQGPSPositionsTable start");
        String CREATE_TABLE_EQ_GPSPOSITIONS = "CREATE TABLE " +
                TABLE_EQ_GPSPOSITIONS + "("
                + "gps_session_id" + " TEXT PRIMARY KEY,"
                + "gps_lat" + " TEXT,"
                + "gps_lon" + " TEXT,"
                + "gps_avgSpeed" + " INTEGER,"
                + "gps_gpsSpeed" + " INTEGER,"
                + "gps_spdCount" + " INTEGER,"
                + "gps_positionDate" + " TEXT,"
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
    //region public functions
    public void deleteEQDatabase() {


        Log.i(eTAG, "eqDatabaseService deleteEQDatabase");
    }


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
                customGaits.setName(cursor.getString(0));
                customGaits.setCategory(cursor.getString(1));
                customGaits.setGait(cursor.getString(2));
                customGaits.setUom(cursor.getString(3));
                customGaits.setPace(cursor.getInt(4));

                allGaits.add(customGaits);

            } while (cursor.moveToNext());
        }

        return allGaits;

    }
    //endregion


}

