package com.newproject.jhull3341.trackiteq;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jhull3341 on 3/31/2016.
 */
public class eqDatabaseService extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_EQ_SESSIONS = "eqSessions_dt";
    private static final String TABLE_EQ_GPSPOSITIONS = "eqGPSPositions_dt";
    private static final String TABLE_EQ_SETTINGS = "eqSettings_dt";
    private static final String TABLE_EQ_CUSTOM_GAITS = "eqCustomGaits_dt";

    private static final String DATABASE_NAME = "EqConditioning.db";

    private String dbPath = "";
    private String appPath = "";

    public eqDatabaseService(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        dbPath = context.getDatabasePath(DATABASE_NAME).toString();
        appPath = context.getApplicationInfo().dataDir;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        createEQSessionsTable(db);
        createEQGPSPositionsTable(db);
        createEQSettingsTable(db);
        createEQCustomGaitsTable(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQ_SESSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQ_GPSPOSITIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQ_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQ_CUSTOM_GAITS);
        onCreate(db);
    }
    //region database db creation
    private void createDB(String name) {

        // first, let's double check that there is no DB for if there is, we d'not want to re-create

        SQLiteDatabase checkdb = null;

        checkdb = SQLiteDatabase.openDatabase(appPath + "//" + name,null,SQLiteDatabase.OPEN_READONLY);
        if (checkdb == null) {
            SQLiteDatabase.openOrCreateDatabase(appPath + "//" + name,null);
        }
    }
    //endregion
    //region database table creation
    private void createEQSessionsTable(SQLiteDatabase db) {

        String CREATE_TABLE_EQ_SESSIONS = "CREATE TABLE " +
                TABLE_EQ_SESSIONS + "("
                + "session_name" + " TEXT PRIMARY KEY,"
                + "session_gait" + " TEXT,"
                + "session_time" + " INTEGER" + ")";

        db.execSQL(CREATE_TABLE_EQ_SESSIONS);

    }
    private void createEQGPSPositionsTable(SQLiteDatabase db) {

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

        String CREATE_TABLE_EQ_SETTINGS = "CREATE TABLE " +
                TABLE_EQ_SETTINGS + "("
                + "settings_key" + " TEXT,"
                + "settings_value" + " TEXT," + ")";

        db.execSQL(CREATE_TABLE_EQ_SETTINGS);

        // now add the default settings for equines

        SQLiteDatabase dbw = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("settings_key","Reminder Volume");
        values.put("settings_value","true");

        dbw.insert(TABLE_EQ_SETTINGS, null, values);

        values.put("settings_key","Transition Warning Time");
        values.put("settings_value","5");

        dbw.insert(TABLE_EQ_SETTINGS, null, values);

        values.put("settings_key","language");
        values.put("settings_value","UK");

        dbw.insert(TABLE_EQ_SETTINGS, null, values);

        values.put("settings_key","Display PACE UOM");
        values.put("settings_value","mpm");

        dbw.insert(TABLE_EQ_SETTINGS, null, values);
        values.put("settings_key","Reminder Interval");
        values.put("settings_value","3");

        dbw.insert(TABLE_EQ_SETTINGS, null, values);

        dbw.close();

    }
    private void createEQCustomGaitsTable(SQLiteDatabase db) {

        String CREATE_TABLE_EQ_CUSTOM_GAITS = "CREATE TABLE " +
                TABLE_EQ_CUSTOM_GAITS + "("
                + "custom_name" + " TEXT,"
                + "custom_category" + " TEXT,"
                + "custom_gait" + " TEXT,"
                + "custom_uom" + " TEXT,"
                + "custom_time" + " INTEGER" + ")";

        db.execSQL(CREATE_TABLE_EQ_CUSTOM_GAITS);

        // now add the default gaits for equines

        SQLiteDatabase dbw = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("custom_name","Equine Riding");
        values.put("custom_category","Equine");
        values.put("custom_gait","Walk");
        values.put("custom_uom","mpm");
        values.put("custom_time","107");

        dbw.insert(TABLE_EQ_CUSTOM_GAITS, null, values);

        values.put("custom_name","Equine Riding");
        values.put("custom_category","Equine");
        values.put("custom_gait","Trot");
        values.put("custom_uom","mpm");
        values.put("custom_time","220");

        dbw.insert(TABLE_EQ_CUSTOM_GAITS, null, values);

        values.put("custom_name","Equine Riding");
        values.put("custom_category","Equine");
        values.put("custom_gait","Beginner Novice");
        values.put("custom_uom","mpm");
        values.put("custom_time","350");

        dbw.insert(TABLE_EQ_CUSTOM_GAITS, null, values);
        values.put("custom_name","Equine Riding");
        values.put("custom_category","Equine");
        values.put("custom_gait","Novice");
        values.put("custom_uom","mpm");
        values.put("custom_time","380");

        dbw.insert(TABLE_EQ_CUSTOM_GAITS, null, values);
        values.put("custom_name","Equine Riding");
        values.put("custom_category","Equine");
        values.put("custom_gait","Training");
        values.put("custom_uom","mpm");
        values.put("custom_time","425");

        dbw.insert(TABLE_EQ_CUSTOM_GAITS, null, values);

        dbw.insert(TABLE_EQ_CUSTOM_GAITS, null, values);
        values.put("custom_name","Equine Riding");
        values.put("custom_category","Equine");
        values.put("custom_gait","Prelim");
        values.put("custom_uom","mpm");
        values.put("custom_time","525");

        dbw.insert(TABLE_EQ_CUSTOM_GAITS, null, values);

        dbw.close();

    }
    //endregion

}

