package com.newproject.jhull3341.trackiteq;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Handler;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.media.SoundPool;
import android.widget.Toast;

public class TrackItEqDisplayActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks ,LocationListener {

    private GoogleApiClient client;
    GoogleApiClient mGoogleApiClient;

    private static final String eTAG = "Exception";
    private ArrayList<String> currentPlan;
    private long planTime = 0;   // stored as secs
    private long legTime = 0;    // time remaining in a leg, stored in secs
    private long preTime = 5;
    private String legGait = ""; // current gait to perform
    private int legNumber = 0;
    private int totalSpeed = 0;         // holds the total speed per leg, changes when leg changes
    private int avgSpeed = 0;           // holds the avg speed per leg, changes when leg changes
    private int stepCount = 1;          // use to calculate the avg speed
    final Context context = this;
    private SoundPool soundPool;
    private int soundID;
    float actVolume, maxVolume, volume;
    private AudioManager audioManager;
    private Boolean loaded = false;
    private Boolean mRequestingLocationUpdates = false;
    private com.google.android.gms.location.LocationRequest mLocationRequest;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private final static int LOCATION_REQUEST_INTERVAL = 5000;
    private final static int LOCATION_REQUEST_FASTEST_INTERVAL = 1000;
    private final static String PACE_SPEED_UNITS = "kph";

    private TextView txtPace;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {

        // do something here to display

            processTime();    // process what to be done on a sec by sec basis
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_it_eq_display);

        Log.i(eTAG, "onCreate Display");

        // First we need to check availability of play services
        if (checkPlayServices()) {
            Log.i(eTAG, "passed check services");
            // Building the GoogleApi client
            buildGoogleApiClient();
        }

        setTitle(R.string.activityMainTitle);
        setContentView(R.layout.activity_track_it_eq_display);

        setActivityMainListeners();
        txtPace = (TextView) findViewById(R.id.txtAvgPace);

        // Load the sounds
        soundStuff();
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });
        soundID = soundPool.load(this, R.raw.bronzebell2, 1);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    //region View Listeners
    private ImageButton.OnClickListener onClick_btnCreatePlans = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Log.i(eTAG,"On CLick start build");

            Intent buildIntent = new Intent(context,TrackItEqMainActivity.class);
            startActivity(buildIntent);

        }
    };

    private ImageButton.OnClickListener onClick_btnStartPlan = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Log.i(eTAG, "onClick_btnStartPlan");
            if (currentPlan.size() != 0) {
                startLocationUpdates();
                timerHandler.postDelayed(timerRunnable, 0);
            }

        }
    };
    private ImageButton.OnClickListener onClick_btnStopPlan = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(eTAG, "onClick_btnStopPlan");
            timerHandler.removeCallbacks(timerRunnable);
            stopLocationUpdates();

            legTime = 0;
            planTime = 0;
            legNumber = 0;
            TextView txtPace = (TextView) findViewById(R.id.txtAvgPace); txtPace.setText(" ");
            TextView txtLeg = (TextView) findViewById(R.id.txtLegTime); txtLeg.setText(" ");
            TextView txtTotal = (TextView) findViewById(R.id.txtTotalTime); txtTotal.setText(" ");
        }
    };
    private ImageButton.OnClickListener onClick_btnPausePlan = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(eTAG, "onClick_btnPausePlan");
            timerHandler.removeCallbacks(timerRunnable);
            stopLocationUpdates();
        }
    };

    private ImageButton.OnClickListener onClick_btnOpenPlan = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            Log.i(eTAG, "On CLick start build");

            // set and open the dialog view to allow user to select a plan to run
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
            lvPlan = (ListView)dialog.findViewById(R.id.lvPlans);
            lvPlan.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, FilesInFolder));
            lvPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    // get the selected plan to execute
                    String data = (String) parent.getItemAtPosition(position);
                    currentPlan = readFromFile(data);   // set the plan for reading/processing

                    //get the total plan time for initial display and display it along with control
                    // buttons
                    planTime = getTotalPlanTime();
                    setCurrentLeg();

                    // this sets up the actual initial display
                    setLegDisplay();

                    String paceTitle = String.format("Leg Pace %1s (avg/curr)",PACE_SPEED_UNITS);
                    TextView lblPaceTitle = (TextView) findViewById(R.id.txtPaceTitle);
                    lblPaceTitle.setText(paceTitle);

                    LinearLayout btnActions = (LinearLayout) findViewById((R.id.lyActionButtons));
                    btnActions.setVisibility(View.VISIBLE);

                    dialog.dismiss();

                }
            });
            dialog.show();

        }
    };
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Log.i(eTAG, "onStart");
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
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Log.i(eTAG, "onStop");
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
    //region GoogleAPI  Listeners
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }
    @Override
    public void onConnected(Bundle bundle) {
        if (mRequestingLocationUpdates) {
            // startLocationUpdates();
            Log.i(eTAG,"onConnected");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(eTAG, "onLocationChanged");
        int speed = gpsLocator.LocationChanged(location,PACE_SPEED_UNITS);

        // calculate the avg speed of the leg for display, but only when speed is > 0
        if (speed > 0) {
            totalSpeed += speed;    // keep dumping into the total speed bucket to calc avg
            avgSpeed = totalSpeed / stepCount;
            stepCount++;
        }

        String sSpeed = String.format("%1$03d/%2$03d",avgSpeed,speed);
        Log.i(eTAG, "rtn speed: " + sSpeed);
        txtPace.setText(sSpeed);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(eTAG, "onConnectionFailed");
    }
    //endregion
    //region Private Functions

    public String gaitLetter(String gait) {
        return gait.substring(0,1).toUpperCase();
    }
    private void setLegDisplay() {

        TextView legText = (TextView) findViewById(R.id.txtLegTime);
        TextView totText = (TextView) findViewById(R.id.txtTotalTime);
        totText.setText(displayTime(planTime));
        legText.setText(String.format("%1s %2s",gaitLetter(legGait), displayTime(legTime)));

        // reset the pace variables for the next leg to display
        totalSpeed = 0;
        avgSpeed = 0;
        stepCount = 1;

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
        legNumber ++;   // increase for the next change

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
        Log.i(eTAG,"readFromFile");
        String ret = "";

        File file = new File(getString(R.string.local_data_path),fName);
        ArrayList<String> rows = new ArrayList<>();

        try {
            FileReader iFile = new FileReader(file);
            BufferedReader br = new BufferedReader(iFile);

            String row;
            while ((row = br.readLine()) != null) {
                rows.add(row);
                Log.i(eTAG,row);
            }
            iFile.close();

        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return rows;
    }
    private long getTotalPlanTime() {
        // read through the arraylist pulled from the plan and calculate the total time of the plan

        long TotalSecs = 0;
        for (String element:currentPlan) {
            String[] elements = element.split(",");
            TotalSecs += (Integer.parseInt(elements[1])) * 60;  // convert to seconds
        }

        return TotalSecs;
    }
    private String displayTime(long inTime) {

        String rtn;

        int[] rtnVals = splitToComponentTimes(inTime);

        if (rtnVals[0] == 0) {
            rtn = String.format("%1$02d:%2$02d",rtnVals[1],rtnVals[2]);
        } else {
            rtn = String.format("%1$d:%2$02d:%3$02d",rtnVals[0],rtnVals[1],rtnVals[2]);
        }

        return rtn;
    }
    private int[] splitToComponentTimes(long longVal){

        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        int[] ints = {hours , mins , secs};
        return ints;
    }
    private void setActivityMainListeners(){

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

        LinearLayout btnActions = (LinearLayout) findViewById((R.id.lyActionButtons));
        btnActions.setVisibility(View.INVISIBLE);

    }
    private void processTime() {

        if (preTime > 0) {
            // play a sound to let user know they got ten seconds to get ready before plan starts
            preTime -= 1;
            soundPool.play(soundID, volume, volume, 1, 0, 1f);
        } else {
            //process the plan
            planTime -= 1;   // remove a second
            legTime -= 1;    // remove a second from the leg as well

            // at 5 seconds to end of leg, play a bell again
            if (legTime <= 5) {
                soundPool.play(soundID, volume, volume, 1, 0, 1f);
            }
            if (legTime == 0) {
                setCurrentLeg();    // set up the next leg on zero
            }
            // set the total time and leg display
            setLegDisplay();
        }
    }
    private void soundStuff(){

        // AudioManager audio settings for adjusting the volume
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;
    }
    protected void createLocationRequest() {
        Log.i(eTAG,"createLocationRequest");
        mLocationRequest = new com.google.android.gms.location.LocationRequest();

        mLocationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        mLocationRequest.setFastestInterval(LOCATION_REQUEST_FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mRequestingLocationUpdates = true;

    }
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }
    protected synchronized void buildGoogleApiClient() {
        Log.i(eTAG,"buildGoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
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
}
