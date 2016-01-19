package com.newproject.jhull3341.trackiteq;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class TrackItEqDisplayActivity extends AppCompatActivity
        implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient client;
    GoogleApiClient mGoogleApiClient;

    private static final String eTAG = "Exception";
    private ArrayList<String> currentPlan;
    private long preTime = 5;
    private long planTime = 0;   // stored as secs
    private long legTime = 0;    // time remaining in a leg, stored in secs
    private String legGait = ""; // current gait to perform
    private String nextGait = ""; // Next gait to perform
    private int legNumber = 0;
    private boolean openSession = false;
    private boolean timerRunning = false;


    Location locPrev;
    TextToSpeech sayTime;           // use to annouce minutes remaining in leg
    final Context context = this;
    private SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    private int soundID;
    float actVolume, maxVolume, volume;
    private AudioManager audioManager;

    private Boolean loaded = false;
    private Boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private final static int LOCATION_REQUEST_INTERVAL = 1000;
    private final static int LOCATION_REQUEST_FASTEST_INTERVAL = 500;
    private final static String PACE_SPEED_UNITS = "kph";

    // constants for saving data on orientation change
    private final static String SAVE_TOTAL_TIME = "totalTime";
    private final static String SAVE_LEG_TIME = "legTime";
    private final static String SAVE_CURRENT_PLAN = "currentPlan";
    private final static String SAVE_OPEN_SESSION = "OpenSession";
    private final static String SAVE_LEG_NUMBER = "legNumber";
    private final static String SAVE_LEG_GAIT = "legGait";
    private final static String SAVE_TIMER_RUNNING = "timerRunning";
    private final static String SAVE_PRE_TIME = "preTime";
    private final static String SAVE_NEXT_GAIT = "nextGait";
// this is a change

    private TextView txtPace;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {

            // do something here to display

            processTime();    // process what to be done on a sec by sec basis
            try {
                timerHandler.postDelayed(this, 1000);
            } catch (Exception ex){

            }

        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(eTAG, "onCreate");

        setTitle(R.string.activityMainTitle);
        setContentView(R.layout.activity_track_it_eq_display);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // First we need to check availability of play services
        if (checkPlayServices()) {
            Log.i(eTAG, "passed check services");
            // Building the GoogleApi client
            // buildGoogleApiClient();
        }
        setUpGoogleApiClientIfNeeded();
        createLocationRequest();

        setActivityMainListeners();  // set the various handers for the display
        soundStuff();          // Load the sounds and sound processing
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        if (savedInstanceState != null) {

            Log.i(eTAG, "onCreate orientation change");
            planTime = savedInstanceState.getLong(SAVE_TOTAL_TIME);
            legGait = savedInstanceState.getString(SAVE_LEG_GAIT);
            nextGait = savedInstanceState.getString(SAVE_NEXT_GAIT);
            legNumber = savedInstanceState.getInt(SAVE_LEG_NUMBER);
            legTime = savedInstanceState.getLong(SAVE_LEG_TIME);
            openSession = savedInstanceState.getBoolean(SAVE_OPEN_SESSION);
            timerRunning = savedInstanceState.getBoolean(SAVE_TIMER_RUNNING);
            preTime = savedInstanceState.getLong(SAVE_PRE_TIME);

            if (legGait != "") {
                TextView legText = (TextView) findViewById(R.id.txtLegTime);
                TextView totText = (TextView) findViewById(R.id.txtTotalTime);
                totText.setText(displayTime(planTime));
                setLegColor();
                legText.setText(String.format("%1s %2s", gaitLetter(legGait), displayTime(legTime)));
                if (openSession) {   // if we had opened plan we need to manage the play action buttons on the switch
                    LinearLayout btnActions = (LinearLayout) findViewById((R.id.lyActionButtons));
                    btnActions.setVisibility(View.VISIBLE);
                    setActionButtons(getString(R.string.startButtonPushed));
                }
                if (timerRunning) {  // if we had the timer running, we need to restart it to keep things going.
                    timerHandler.postDelayed(timerRunnable, 0);
                }
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.i("Exception", "Set up menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_exit:
                this.finishAffinity();
                break;
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
      //  LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(eTAG,"onLocationChanged provider: " + location.getProvider() + " ");
        LocationChanged(convertSpeed(location,"mpm"));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(eTAG, "onSaveInstanceState");
        outState.putLong(SAVE_TOTAL_TIME,planTime);
        outState.putString(SAVE_LEG_GAIT,legGait);
        outState.putString(SAVE_NEXT_GAIT, nextGait);
        outState.putInt(SAVE_LEG_NUMBER,legNumber);
        outState.putLong(SAVE_LEG_TIME,legTime);
        outState.putBoolean(SAVE_OPEN_SESSION,openSession);
        outState.putStringArrayList(SAVE_CURRENT_PLAN,currentPlan);
        outState.putBoolean(SAVE_TIMER_RUNNING,timerRunning);
        outState.putLong(SAVE_PRE_TIME,preTime);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(eTAG, "onRestoreInstanceState");
        planTime = savedInstanceState.getLong(SAVE_TOTAL_TIME);
        legGait = savedInstanceState.getString(SAVE_LEG_GAIT);
        legNumber = savedInstanceState.getInt(SAVE_LEG_NUMBER);
        legTime = savedInstanceState.getLong(SAVE_LEG_TIME);
        openSession = savedInstanceState.getBoolean(SAVE_OPEN_SESSION);
        currentPlan = savedInstanceState.getStringArrayList(SAVE_CURRENT_PLAN);

    }

    //region View Listeners
    private ImageButton.OnClickListener onClick_btnCreatePlans = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // this send the app over to the plan management tool activity
            Intent buildIntent = new Intent(context, TrackItEqMainActivity.class);
            startActivity(buildIntent);
        }
    };

    private ImageButton.OnClickListener onClick_btnStartPlan = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // this will begin the actual running of the plan.  It kicks off the timer
            // which also kicks off the gps location listener

            if (currentPlan.size() != 0) {
                setActionButtons(getString(R.string.startButtonPushed));  // reset back to just the play if we stopped the plan
                startLocationUpdates();
                timerHandler.postDelayed(timerRunnable, 0);
                timerRunning = true;
            }

        }
    };
    private ImageButton.OnClickListener onClick_btnStopPlan = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // TODO: add a (fragment) or something to handle a Are you sure moment
            // if the user wants to end the plan before it is over.  This resets everything
            onStopPlan();
            timerRunning = false;
        }
    };
    private ImageButton.OnClickListener onClick_btnPausePlan = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // this simply stops the timer and gps listener till the start button is pressed again
            // or the stop button
            timerHandler.removeCallbacks(timerRunnable);
            timerRunning = false;
            stopLocationUpdates();
            setActionButtons(getString(R.string.pauseButtonPushed));  // reset back to just the play if we stopped the plan
        }
    };

    private ImageButton.OnClickListener onClick_btnOpenPlan = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // This will allow the user to select a plan from a list of pre-made plans
            onOpenPlan();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();

        // when the app is successfully started we need to create a googleapiclient
        // that interfaces with teh google location services and maps api

        client.connect();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
            Log.i(eTAG, "Connected");
        }

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TrackItEqDisplay Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.newproject.jhull3341.trackiteq/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction2 = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TrackItEqDisplay Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.newproject.jhull3341.trackiteq/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction2);
    }

    @Override
    public void onStop() {
        super.onStop();

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TrackItEqDisplay Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.newproject.jhull3341.trackiteq/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    //endregion
    //region Private Functions
    private void onOpenPlan() {

        // set and open the dialog view to allow user to select a plan to run

        openSession = true;

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_track_it_eq_open_plan);
        dialog.setTitle("Open Exercise Plan...");

        Button diaCancelButton = (Button) dialog.findViewById(R.id.btnCancelOpen);
        diaCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // get a list of files from the local app plans
        ListView lvPlan;
        ArrayList<String> FilesInFolder = GetFiles(getString(R.string.local_data_path));
        lvPlan = (ListView) dialog.findViewById(R.id.lvPlans);
        lvPlan.setAdapter(new customArrayAdapter<>(context, android.R.layout.simple_list_item_1, FilesInFolder));
        lvPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                resetCurrentPlanValues();

                // get the selected plan to execute
                String data = (String) parent.getItemAtPosition(position);
                currentPlan = readFromFile(data);   // set the plan for reading/processing

                //get the total plan time for initial display and display it along with control
                // buttons
                planTime = getTotalPlanTime();

                setCurrentLeg();

                // this sets up the actual initial display
                setLegDisplay();

                String paceTitle = String.format("Leg Pace %1s (avg/curr)", PACE_SPEED_UNITS);
                TextView lblPaceTitle = (TextView) findViewById(R.id.txtPaceTitle);
                lblPaceTitle.setText(paceTitle);

                LinearLayout btnActions = (LinearLayout) findViewById((R.id.lyActionButtons));
                btnActions.setVisibility(View.VISIBLE);
                setActionButtons(getString(R.string.stopButtonPushed));
                //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest, (LocationListener) context);
                
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    private void onStopPlan() {

        // set and open the dialog view to allow user to select a plan to run

        openSession = false;

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_track_it_eq_confirm);
        dialog.setTitle("Closing Current Plan...");

        // pause the clock and gps, just in case

        timerHandler.removeCallbacks(timerRunnable);
        stopLocationUpdates();

        // set up the yes button, that if clicked will end the running of the current plan.
        Button yesButton = (Button) dialog.findViewById((R.id.btnYes));
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCurrentPlanValues();
                setActionButtons(getString(R.string.yesStopButtonPushed));
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) context);
                dialog.dismiss();
            }
        });
        // the no way button will just clear out the
        Button noButton = (Button) dialog.findViewById((R.id.btnNoWay));
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActionButtons(getString(R.string.noStopButtonPushed));  // reset back to just the play if we stopped the plan
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void LocationChanged(int currSpeed) {

        String sSpeed;

        // calculate the avg speed of the leg for display, but only when speed is > 0
        if (currSpeed > 0) {
            sSpeed = String.format("%1$04d",currSpeed);
        } else {
            sSpeed = getString(R.string.noData);
        }
        txtPace.setText(sSpeed);
    }
    private int convertSpeed(Location locCurr,String units) {

        // speed can come from either the speed set by fuse location listener or as I am
        // starting to see, when GPS is out, it comes from the LatLon calculations.
        double speed;
        Double d1;
        Long t1;

        if (locCurr.hasSpeed()) {
            speed = locCurr.getSpeed() * 1.0; // need to * 1.0 to get into a double for some reason...
        } else {
            speed = 0;
            try {
                // get the distance and time between the current position, and the previous position.
                // using (counter - 1) % data_points doesn't wrap properly
                d1 = distance(locCurr.getLatitude(), locCurr.getLongitude(), locPrev.getLatitude(), locPrev.getLongitude());
                t1 = locCurr.getTime() - locPrev.getTime();
                Log.i(eTAG,"d1:" + d1);
                Log.i(eTAG,"t1:" + t1);
                speed = d1 / t1; // m/s
                Log.i(eTAG,"speed: " + speed);
            } catch (NullPointerException e) {
                //all good, just not enough data yet.
            }

        }
        int lclSpeed;
        // convert from m/s to specified units
        switch (units) {
            case "kph":
                lclSpeed = (int)(speed * 3.6d);
                break;
            case "mph":
                lclSpeed = (int)(speed * 2.23693629d);
                break;
            case "knots":
                lclSpeed = (int)(speed * 1.94384449d);
                break;
            case "mpm":
                lclSpeed = (int)(speed * 60);  // 60 meters per minute = 1 meter per sec
                break;
            default:
                lclSpeed = (int)speed;
        }

        locPrev = locCurr;

        return lclSpeed;

    }
    private void resetCurrentPlanValues() {

        preTime = 5;            // this gives the warning bells before the start.
        legTime = 0;
        planTime = 0;
        legNumber = 0;
        TextView txtPace = (TextView) findViewById(R.id.txtAvgPace);
        txtPace.setText(" ");
        TextView txtLeg = (TextView) findViewById(R.id.txtLegTime);
        txtLeg.setText(" ");
        TextView txtTotal = (TextView) findViewById(R.id.txtTotalTime);
        txtTotal.setText(" ");
        TextView lblPaceTitle = (TextView) findViewById(R.id.txtPaceTitle);
        lblPaceTitle.setText(R.string.lclPaceTitle);


    }

    public String gaitLetter(String gait) {
        return gait.substring(0, 1).toUpperCase();
    }

    private void setLegDisplay() {

        TextView legText = (TextView) findViewById(R.id.txtLegTime);
        TextView totText = (TextView) findViewById(R.id.txtTotalTime);
        totText.setText(displayTime(planTime));
        legText.setText(String.format("%1s %2s > %3s", gaitLetter(legGait), displayTime(legTime),gaitLetter(nextGait)));

    }

    private void setLegColor() {

        // set the color of the text box based on the current gait.  It is a set once for
        // the length of the leg.

        TextView legText = (TextView) findViewById(R.id.txtLegTime);
        switch (gaitLetter(legGait)) {
            case "W":
                legText.setBackgroundResource(R.color.colorWalk);
                break;
            case "T":
                legText.setBackgroundResource(R.color.colorTrot);
                break;
            case "C":
                legText.setBackgroundResource(R.color.colorCanter);
                break;
            case "G":
                legText.setBackgroundResource(R.color.colorGallop);
                break;
        }
    }

    private void setCurrentLeg() {

        //get the total plan time for initial display and display it along with control
        // buttons

        String[] legData = currentPlan.get(legNumber).split(",");

        legTime = Integer.parseInt(legData[1]) * 60;    //convert to seconds
        legGait = legData[0];
        setLegColor();
        legNumber++;   // increase for the next change
        try {
            String[] nextLegData = currentPlan.get(legNumber).split(",");
            nextGait = nextLegData[0];
        } catch (Exception ex) {
            nextGait = "Finish";
        }



    }

    private void setActionButtons(String whatPushed) {

        ImageButton btnStartPlan = (ImageButton) findViewById(R.id.btnStartPlan);
        ImageButton btnPausePlan = (ImageButton) findViewById(R.id.btnPausePlan);
        ImageButton btnStopPlan = (ImageButton) findViewById(R.id.btnStopPlan);

        if (whatPushed.equals(getString(R.string.startButtonPushed))) {
            btnStartPlan.setVisibility((View.INVISIBLE));
            btnPausePlan.setVisibility((View.VISIBLE));
            btnStopPlan.setVisibility((View.VISIBLE));
        } else if (whatPushed.equals(getString(R.string.stopButtonPushed))) {
            btnPausePlan.setVisibility((View.INVISIBLE));
            btnStopPlan.setVisibility((View.INVISIBLE));
            btnStartPlan.setVisibility((View.VISIBLE));
        } else if (whatPushed.equals(getString(R.string.pauseButtonPushed))) {
            btnStartPlan.setVisibility((View.VISIBLE));
        } else if (whatPushed.equals(getString(R.string.noStopButtonPushed))) {
            btnPausePlan.setVisibility((View.VISIBLE));
            btnStopPlan.setVisibility((View.VISIBLE));
            btnStartPlan.setVisibility((View.VISIBLE));
        } else if (whatPushed.equals(getString(R.string.yesStopButtonPushed))) {
            btnPausePlan.setVisibility((View.INVISIBLE));
            btnStopPlan.setVisibility((View.INVISIBLE));
            btnStartPlan.setVisibility((View.INVISIBLE));
        }
    }

    public ArrayList<String> GetFiles(String DirectoryPath) {

        // this function will generate a list of files based on the passed path

        ArrayList<String> MyFiles = new ArrayList<>();
        File f = new File(DirectoryPath);

        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0)
            return null;
        else for (File file : files) MyFiles.add(file.getName());

        return MyFiles;
    }

    private ArrayList<String> readFromFile(String fName) {
        Log.i(eTAG, "readFromFile");

        File file = new File(getString(R.string.local_data_path), fName);
        ArrayList<String> rows = new ArrayList<>();

        try {
            FileReader iFile = new FileReader(file);
            BufferedReader br = new BufferedReader(iFile);

            String row;
            while ((row = br.readLine()) != null) {
                rows.add(row);
            }
            iFile.close();

        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return rows;
    }

    private long getTotalPlanTime() {
        // read through the arraylist pulled from the plan and calculate the total time of the plan

        long TotalSecs = 0;
        for (String element : currentPlan) {
            String[] elements = element.split(",");
            TotalSecs += (Integer.parseInt(elements[1])) * 60;  // convert to seconds
        }

        return TotalSecs;
    }

    private String displayTime(long inTime) {

        String rtn;

        int[] rtnVals = splitToComponentTimes(inTime);

        if (rtnVals[0] == 0) {
            rtn = String.format("%1$02d:%2$02d", rtnVals[1], rtnVals[2]);
        } else {
            rtn = String.format("%1$d:%2$02d:%3$02d", rtnVals[0], rtnVals[1], rtnVals[2]);
        }

        return rtn;
    }

    private int[] splitToComponentTimes(long longVal) {

        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        return new int[]{hours, mins, secs};
    }
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
    private void setActivityMainListeners() {
        Log.i(eTAG, "setActivityMainListeners function 1");
        txtPace = (TextView) findViewById(R.id.txtAvgPace);

        TextView txt1 = (TextView) findViewById((R.id.txtAvgPace));
        txt1.setText(" ");
        TextView txt2 = (TextView) findViewById((R.id.txtLegTime));
        txt2.setText(" ");
        TextView txt3 = (TextView) findViewById((R.id.txtTotalTime));
        txt3.setText(" ");

        ImageButton btnCreatePlan = (ImageButton) findViewById(R.id.btnManagePlans);
        btnCreatePlan.setOnClickListener(onClick_btnCreatePlans);
        ImageButton btnSelectPlan = (ImageButton) findViewById(R.id.btnOpenPlan);
        btnSelectPlan.setOnClickListener(onClick_btnOpenPlan);
        ImageButton btnStartPlan = (ImageButton) findViewById(R.id.btnStartPlan);
        btnStartPlan.setOnClickListener(onClick_btnStartPlan);
        ImageButton btnPausePlan = (ImageButton) findViewById(R.id.btnPausePlan);
        btnPausePlan.setOnClickListener(onClick_btnPausePlan);
        ImageButton btnStopPlan = (ImageButton) findViewById(R.id.btnStopPlan);
        btnStopPlan.setOnClickListener(onClick_btnStopPlan);

        createLocationRequest();

        if (!openSession) {
            LinearLayout btnActions = (LinearLayout) findViewById((R.id.lyActionButtons));
            btnActions.setVisibility(View.INVISIBLE);
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        sayTime = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    sayTime.setLanguage(Locale.UK);
                }
            }
        });
    }

    private void processTime() {

        // now set up the new leg data
        if (legTime == 0) {
            if (legNumber >= currentPlan.size()) {
                // we are done with the program, end it the timer.
                timerHandler.removeCallbacks(timerRunnable);
                timerHandler = null;
                timerRunning = false;
            } else {
                setCurrentLeg();    // set up the next leg on zero if we still have legs to complete
                setLegDisplay();    // set the total time and leg display
            }
        } else {
            if (preTime > 0) {
                // play a sound to let user know they got ten seconds to get ready before plan starts
                preTime -= 1;
                soundPool.play(soundID, volume, volume, 1, 0, 1f);
            } else {
                //process the plan
                planTime -= 1;   // remove a second
                legTime -= 1;    // remove a second from the leg as well

                // on every minute, tell the user the minute remaining
                // on every 30 seconds , play a sound
                if ((legTime % 60) == 0 && legTime > 0) {
                    //soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    int minute = (int) (legTime / 60);

                    sayTime.speak(Integer.toString(minute), TextToSpeech.QUEUE_FLUSH, null);

                } else if ((legTime % 30) == 0 && legTime > 0) {
                    soundPool.play(soundID, volume, volume, 1, 0, .5f);

                }

                // 10 secs before the leg ends tell user
                if (legTime == 10) {
                    sayTime.speak("Next up " + nextGait, TextToSpeech.QUEUE_FLUSH, null);
                }

                // at 5 seconds to end of leg, play a bell again
                if (legTime <= 5) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                }
            }
            setLegDisplay();    // set the total time and leg display
        }


    }

    private void soundStuff() {

        // AudioManager audio settings for adjusting the volume
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });
        soundID = soundPool.load(this, R.raw.bronzebell2, 1);

    }

    private void setUpGoogleApiClientIfNeeded() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();
        }
    }
    protected void createLocationRequest() {

        Log.i(eTAG, "createLocationRequest");
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        mLocationRequest.setFastestInterval(LOCATION_REQUEST_FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mRequestingLocationUpdates = true;

    }

    protected void startLocationUpdates() {
        Log.i(eTAG, "startLocationUpdates");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        Log.i(eTAG, "stopLocationUpdates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
    }

    private boolean checkPlayServices() {
        Log.i(eTAG, "in checkPlayServices");
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(eTAG, "This device is not supported.");
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    //endregion
    protected class customArrayAdapter<String> extends ArrayAdapter<String> {

        public customArrayAdapter(Context context, int resource, ArrayList<String> objects) {
            super(context, resource, objects);
        }

        public customArrayAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            if (position % 2 == 1) {
                view.setBackgroundColor(Color.LTGRAY);
            } else {
                view.setBackgroundColor(Color.WHITE);
            }

            return view;
        }
    }
}
