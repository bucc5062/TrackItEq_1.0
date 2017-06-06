package com.newproject.jhull3341.trackiteq;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jhull3341 on 5/22/2017.
 */

public class TrackItEqGPSActivity  extends AppCompatActivity {

    final Context context = this;
    private static final String eTAG = "Exception";
    private String dbPath = "";
    private String appPath = "";
    private static final String DATABASE_NAME = "EqConditioning.db";
    private ListView lstGPSPoints;
    private List<eqGPSPositions_dt> myRun;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.activityGPSActions);
        setContentView(R.layout.activity_gps_actions);
        lstGPSPoints = (ListView) findViewById(R.id.lstGPSPoints);

        Toolbar mySecondaryToolbar = (Toolbar) findViewById(R.id.my_gpstoolbar);
        setSupportActionBar(mySecondaryToolbar);

        setGPSActivityListeners();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.i("Exception", "Set up secondary  menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gps_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_gps_exit:
                // this send the app over to the plan management tool activity
                Intent buildIntent = new Intent(context, TrackItEqDisplayActivity.class);
                startActivity(buildIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private final ThreadLocal<View.OnClickListener> onClick_btnLineGraph = new ThreadLocal<View.OnClickListener>() {
        @Override
        protected View.OnClickListener initialValue() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent buildIntent = new Intent(context, TrackItEqGPSGraphActivity.class);
                    buildIntent.putParcelableArrayListExtra("gpsdata",(ArrayList<? extends Parcelable>) myRun);

                    startActivity(buildIntent);

                }


            };
        }

    };

    private final ThreadLocal<View.OnClickListener> onClick_btnOpen = new ThreadLocal<View.OnClickListener>() {
        @Override
        protected View.OnClickListener initialValue() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.activity_track_it_eq_open_gpsrun);
                    dialog.setTitle("Open GPS Run...");

                    Button diaCancelButton = (Button) dialog.findViewById(R.id.btnCancelOpen);
                    diaCancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });


                    // get a list of files from the local app plans
                    final eqDatabaseService eqDB = new eqDatabaseService(context, 2);

                    ArrayList<eqGPSDataMaster> allRuns = eqDB.getGPSList();

                    if (allRuns.isEmpty()) {
                        Toast toast = new Toast(context);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        Toast.makeText(context, "No GPS Runs to open, Please create one!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    ListView lvPlan = (ListView) dialog.findViewById(R.id.lvPlans);

                    List<Map<String, Object>> showRuns = new ArrayList<Map<String, Object>>();


                    for (eqGPSDataMaster row : allRuns) {
                        Map<String, Object> dataMap = new HashMap<>();
                        dataMap.put("sName", row.get_gps_session_name());
                        dataMap.put("sDate", row.get_gps_session_date());
                        dataMap.put("sID", row.get_gps_session_id());
                        showRuns.add((dataMap));
                    }
                    try {

                        SimpleAdapter RunsAdapter = new SimpleAdapter(context, showRuns, R.layout.gps_mst_columns,
                                new String[]{"sName", "sDate", "sID"},
                                new int[]{R.id.txtSName, R.id.txtSDate, R.id.txtSid});

                        lvPlan.setAdapter(RunsAdapter);
                    } catch (Exception e) {
                        Log.i(eTAG, e.getStackTrace().toString());
                    }
                    //********************************************************


                    lvPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                            TextView txtplan = (TextView) v.findViewById(R.id.txtSName);
                            String planName = txtplan.getText().toString();
                            TextView txtDate = (TextView) v.findViewById(R.id.txtSDate);
                            String runDate = txtplan.getText().toString();

                            myRun = null;
                            myRun = eqDB.getGPSData(planName, runDate);

                            clearGrid();
                            List<Map<String, Object>> showRun = new ArrayList<Map<String, Object>>();

                            for (eqGPSPositions_dt row : myRun) {
                                Map<String, Object> dataMap = new HashMap<>();
                                dataMap.put("gpsLat", row.get_lat());
                                dataMap.put("gpsLon", row.get_lon());
                                dataMap.put("gpsAvgSpeed", row.getAvgSpeed());
                                dataMap.put("gpsCurrSpeed", row.getGpsSpeed());
                                dataMap.put("gpsBearing", row.getBearing());
                                showRun.add((dataMap));
                            }

                            //ListView lstGPSPoints = (ListView) parent.findViewById(R.id.lstGPSPoints);

                            try {
                                SimpleAdapter adapter = new SimpleAdapter(context, showRun, R.layout.gps_detail,
                                        new String[]{"gpsLat", "gpsLon", "gpsAvgSpeed", "gpsCurrSpeed", "gpsBearing"},
                                        new int[]{R.id.txtLat, R.id.txtLon, R.id.txtavgSpeed, R.id.txtGPSSpeed, R.id.txtBearing});

                                lstGPSPoints.setAdapter(adapter);
                                //((BaseAdapter)(adapter)).notifyDataSetChanged();

                            } catch (Exception e) {
                                Log.i(eTAG, e.getMessage());
                            }

                            dialog.dismiss();


                        }
                    });
                    dialog.show();
                }
            };
        }
    };

    //region "local functions"

    private void setGPSActivityListeners() {

        ImageButton btnOpen = (ImageButton) findViewById(R.id.btnOpenGPSGo);
        btnOpen.setOnClickListener(onClick_btnOpen.get());
        ImageButton btnLineGraph = (ImageButton) findViewById(R.id.btnLineGraph);
        btnLineGraph.setOnClickListener(onClick_btnLineGraph.get());
    }

    private void clearGrid() {

        // clean up the grid any time we start to work with a plan, new or opened.

        //mylist.clear();
        // ((BaseAdapter)(adapter)).notifyDataSetChanged();

    }

    //endregion
}
