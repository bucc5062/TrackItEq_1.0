package com.newproject.jhull3341.trackiteq;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.*;

public class TrackItEqMainActivity extends AppCompatActivity {

    public class DetailLine {
        public String Gait;
        public String legTime;
        public String totTime;
    }
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public int rowCount = 1;
    public int TotalTime = 0;

    private static final String eTAG = "Exception";
    final Context context = this;

    // global variables needed for processing the add gaits display
    private ListView lstGaits;
    private ListAdapter adapter;
    private ListAdapter adapter1;

    private ArrayList<HashMap<String, String>> mylist;
    public List<Map<String, Object>> disData;
    public Map<String, Object> dataMap;

    // *****

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(eTAG, "onCreate Build");

        setTitle(R.string.activityManagePlans);
        setContentView(R.layout.activity_track_it_eq_build);

        Toolbar mySecondaryToolbar = (Toolbar) findViewById(R.id.my_secondaryToolbar);
        setSupportActionBar(mySecondaryToolbar);

        setActivityBuildListeners();

        // set the objects needed to handle the dynamic gaits list
        ListView lstGaits = (ListView) findViewById(R.id.lstMyGaits);

        mylist = new ArrayList<HashMap<String, String>>();

        disData = new ArrayList<Map<String,Object>>();
        dataMap = new HashMap<String, Object>(3);

        try {
            adapter = new SimpleAdapter(this,
                    mylist,
                    R.layout.gaits_detail,
                    new String[] { "keyGait", "keyTime" },
                    new int[] {R.id.txtdisplayGait, R.id.txtdisplayTime  });

            adapter1 = new SimpleAdapter(this,
                    disData,
                    R.layout.gaits_detail1,
                    new String[] {"keyGait", "keyTime", "keyTTime"} ,
                    new int[] {R.id.txtdisplayGait, R.id.txtdisplayTime, R.id.txtdisplayTotalTime}){

                //overload the getChildView or any other Override methods
            };

            lstGaits.setAdapter(adapter);
            //lstGaits.setAdapter(adapter1);
        } catch (Exception e) {
            Log.i(eTAG, e.getMessage());
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.i("Exception", "Set up secondary  menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.secondary_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_back:
                // this send the app over to the plan management tool activity
                Intent buildIntent = new Intent(context, TrackItEqDisplayActivity.class);
                startActivity(buildIntent);
                break;
            case R.id.action_settings:
                break;
            case R.id.action_gaits:
                setGaitAction();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void setGaitAction() {

        Log.i(eTAG, "onOptionsItemSelected_gaits");
        // this send the app over to the plan management tool activity
        Intent buildIntent = new Intent(context, TrackItEqManageGaits.class);
        startActivity(buildIntent);
    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i(eTAG, "onSaveInstanceState");
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TrackItEqMain Page", // TODO: Define a title for the content shown.
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
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TrackItEqMain Page", // TODO: Define a title for the content shown.
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
    /* --------------------------------------------------------------------------------------------- */
    //region Listeners
    private OnItemSelectedListener onItemSelected = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            TextView myText = (TextView) findViewById(R.id.txtSelected);
            myText.requestFocus();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private ListView.OnItemClickListener onClick_lstGaits = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            TextView txtgait = (TextView) view.findViewById(R.id.txtgName);
//            TextView txtpace = (TextView) view.findViewById(R.id.txtgPace);
//
//            TextView pGait = (TextView) findViewById(R.id.txtGait);
//            TextView ppace = (TextView) findViewById(R.id.txtPace);
//            pGait.setText(txtgait.getText());
//            ppace.setText(txtpace.getText());

        }
    };
    private Button.OnClickListener onClick_btnNew = new OnClickListener() {
        @Override
        public void onClick(View v) {

            /*
            what happens here is a reset/set of the grid.  The row count is set back to one,
            all grids are removed and the START is set for display.
            */
            GridLayout grdEntry = (GridLayout) findViewById(R.id.grdEntry);
            grdEntry.setVisibility(VISIBLE);
            Log.i(eTAG, "onClick_btnNew");
            clearGrid();
            TextView txtTime = (TextView) findViewById(R.id.txtSelected);
            txtTime.setText("");

        }
    };
    private final ThreadLocal<OnClickListener> onClick_btnOpen = new ThreadLocal<OnClickListener>() {
        @Override
        protected OnClickListener initialValue() {
            return new OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.activity_track_it_eq_open_plan);
                    dialog.setTitle(R.string.OpenExercisePlan);

                    Button diaCancelButton = (Button) dialog.findViewById(R.id.btnCancelOpen);
                    diaCancelButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    // get a list of files from the local app plans
                    final eqDatabaseService eqDB = new eqDatabaseService(context, 2);

                    ListView lvPlan = (ListView) dialog.findViewById(R.id.lvPlans);
                    ListView lvHeader = (ListView) dialog.findViewById(R.id.lvpHeader);

                    ArrayList<HashMap<String,String>> allPlans = eqDB.getPlanList();
                    ArrayList<HashMap<String,String>> list_title = new ArrayList<>();

                    // set up the header

                    HashMap<String, String> HdrMap = new HashMap<>();
                    HdrMap.put("keyName", "Plan");
                    HdrMap.put("keyENum", "Num Legs");
                    list_title.add(HdrMap);

                    if (allPlans.isEmpty()) {
                        Toast toast = new Toast(context);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        Toast.makeText(context, "No Files to open, Please create one!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    try {
                        SimpleAdapter adapter_title1 = new SimpleAdapter(context, list_title, R.layout.plans_columns,
                                new String[] { "keyName", "keyENum" }, new int[] {
                                R.id.txtPlanName, R.id.txtNumElements  });

                        lvHeader.setAdapter(adapter_title1);
                    } catch (Exception e) {
                        Log.i(eTAG, e.getStackTrace().toString());
                    }
                    try {
                        SimpleAdapter PlansAdapter = new SimpleAdapter(context, allPlans, R.layout.plans_columns,
                                new String[] { "keyName", "keyENum" }, new int[] {
                                R.id.txtPlanName, R.id.txtNumElements  });

                        lvPlan.setAdapter(PlansAdapter);
                    } catch (Exception e) {
                        Log.i(eTAG, e.getStackTrace().toString());
                    }
                    //********************************************************



                    lvPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                            TextView txtplan = (TextView) v.findViewById(R.id.txtPlanName);
                            String planName = txtplan.getText().toString();

                            List<eqSessions_dt> myPlan = eqDB.getCurrentPlan(planName);

                            clearGrid();
                            TotalTime = 0;
                            disData = new ArrayList<Map<String,Object>>();

                            for (eqSessions_dt row : myPlan) {
                                TotalTime = TotalTime + row.get_time();
                                buildDisplayLine(false, row.get_gait(),Integer.toString(row.get_time()),toTime(TotalTime));
                            }


                            ((BaseAdapter)(adapter)).notifyDataSetChanged();
                            //((BaseAdapter)(adapter1)).notifyDataSetChanged();

                            dialog.dismiss();


                        }
                    });
                    dialog.show();
                }
            };
        }
    };
    private Button.OnClickListener onClick_btnSave = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(eTAG, "onClick_btnSave");

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.activity_track_it_eq_save_plan);
            dialog.setTitle(R.string.dlgSaveTitle);

            Button diaCancelButton = (Button) dialog.findViewById(R.id.btnCancel);
            diaCancelButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            Button diaSaveButton  = (Button) dialog.findViewById(R.id.btnSaveDia);
            diaSaveButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText txtplanName = (EditText) dialog.findViewById(R.id.txtPlanName);
                    String planName = txtplanName.getText().toString();

                    try {

                        // load the elements into the database

                        Integer elementCount = 0;
                        eqDatabaseService eqDB = new eqDatabaseService(context, 2);

                        String dbname = context.getDatabasePath("EqConditioning_db").getAbsolutePath();
                        Log.i(eTAG,"dbPath=" + dbname);

                        for (HashMap<String, String> aRow : mylist) {
                            String gait = aRow.get("keyGait");
                            String time = aRow.get("keyTime");
                            eqSessions_dt sessionRow = new eqSessions_dt();

                            sessionRow.set_planName(planName);
                            sessionRow.set_gait(gait);
                            sessionRow.set_time(Integer.parseInt(time));
                            sessionRow.set_elementNumber(elementCount);

                            eqDB.insertCurrentPlanElement(sessionRow);
                            elementCount += 1;

                        }

                        List<eqSessions_dt> justchecking = eqDB.getCurrentPlan(planName);

                        for (eqSessions_dt row : justchecking) {
                            Log.i(eTAG, row.get_planName() + "," + row.get_elementNumber() + "," + row.get_gait() + "," + row.get_time());
                        }

                        eqDB.close();

                    } catch (Exception ex) {

                    }

                    clearGrid();
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    };
    private Button.OnClickListener onClick_btnDelete = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //stuff happens here
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.activity_track_it_eq_open_plan);
            dialog.setTitle("Delete Exercise Plan...");

            Button diaCancelButton = (Button) dialog.findViewById(R.id.btnCancelOpen);
            diaCancelButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            // get a list of files from the local app plans
            final eqDatabaseService eqDB = new eqDatabaseService(context, 2);

            ArrayList<HashMap<String,String>> allPlans = eqDB.getPlanList();

            if (allPlans.isEmpty()) {
                Toast toast = new Toast(context);
                toast.setGravity(Gravity.TOP, 0, 0);
                Toast.makeText(context, "No Files to Delete, Please create one!", Toast.LENGTH_LONG).show();
                return;
            }

            ListView lvPlan = (ListView) dialog.findViewById(R.id.lvPlans);

            try {

                SimpleAdapter PlansAdapter = new SimpleAdapter(context, allPlans, R.layout.plans_columns,
                        new String[] { "keyName", "keyENum" }, new int[] {
                        R.id.txtPlanName, R.id.txtNumElements  });

                lvPlan.setAdapter(PlansAdapter);
            } catch (Exception e) {
                Log.i(eTAG, e.getStackTrace().toString());
            }
            //********************************************************

            lvPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    TextView txtplan = (TextView) v.findViewById(R.id.txtPlanName);
                    String planName = txtplan.getText().toString();

                    eqDB.deleteCurrentPlan(planName);

                    dialog.dismiss();

                }
            });
            dialog.show();

        }
    };
    private Button.OnClickListener onClick_btnSet = new OnClickListener() {
        @Override
        public void onClick(View v) {

            Spinner spinner = (Spinner) findViewById(R.id.spinner_gaits);
            TextView textGait = (TextView) spinner.getSelectedView();
            String gtResult = textGait.getText().toString();

            TextView txtTime = (TextView) findViewById(R.id.txtSelected);
            String tmeResult = txtTime.getText().toString();

            Integer.parseInt(tmeResult);
            TotalTime = TotalTime + Integer.parseInt(tmeResult);;

            buildDisplayLine(true,gtResult,tmeResult, toTime(TotalTime));

            txtTime.setText("");
            spinner.requestFocus();

        }
    };

    private Button.OnClickListener onClick_btnRemove = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //stuff happens here
            int id = v.getId();
            Log.i(eTAG,"Selected item" + id);
            // now find the grid this button exists in ahd remove it
            findViewById(id-300).setVisibility(GONE);
        }
    };
    //endregion

    /* ----------------------------------------------------------------------------- */
    //region PrivateFunctions
    @TargetApi(Build.VERSION_CODES.M)
    private void buildDisplayLine(boolean isNew, String gait, String time, String TotTime) {

        HashMap<String, String> map2 = new HashMap<String, String>();
        dataMap = new HashMap<String, Object>(3);

        dataMap.put("keyGait",gait);
        dataMap.put("keyTime",time);
        dataMap.put("keyTTime",TotTime);

        disData.add(dataMap);

        map2.put("keyGait",gait);                  // put the col data in
        map2.put("keyTime", time);      // put the col data in
        mylist.add(map2);

        if (isNew) {
            ((BaseAdapter) (adapter)).notifyDataSetChanged();
           // ((BaseAdapter) (adapter1)).notifyDataSetChanged();
        }

    }
    private String toTime(int passTime) {

        int hours = passTime / 60;  //Everything is in minutes so 60 as in 60 minutes in and hour
        int mins = passTime % 60;   //we do a modal on 60 because the result will be remaining secs

        return String.format("%02d:%02d",hours,mins);
    }
    private int toPixels(int dp) {

        final float scale = getResources().getDisplayMetrics().density;
        int pixels;
        pixels = (int) (dp * scale + 0.5f);

        return pixels;

    }
    private ArrayList<View> getViewsByTag(ViewGroup root, String tag){
        ArrayList<View> views = new ArrayList<>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            }

            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }

        }
        return views;
    }
    public void writeToFile(String fName, String allData) {

        ContextWrapper c = new ContextWrapper(this);
        Log.i(eTAG,c.getApplicationInfo().dataDir);

        try {
            boolean itsOkay = checkCreateDirectory(getString(R.string.local_data_path));
            if (itsOkay) {
                File file = new File(getString(R.string.local_data_path), fName);
                FileWriter oFile = new FileWriter(file);    // no append
                oFile.write(allData);
                oFile.flush();
                oFile.close();
            } else {
                Log.i(eTAG,"Could not make data directory");
            }
        }
        catch (IOException e) {
            Log.i(eTAG, e.getMessage());
        }
    }
    private boolean checkCreateDirectory(String dirName) {
        File folder = new File(dirName);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        return success;
    }
    public ArrayList<String> GetFiles(String DirectoryPath) {

        // this function will generate a list of files based on the passed path

        ArrayList<String> MyFiles = new ArrayList<>();
        File f = new File(DirectoryPath);

        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0)
            return null;
        else {
            for (int ic=0; ic<files.length; ic++) MyFiles.add(files[ic].getName());
        }

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
    private void clearGrid() {

        // clean up the grid any time we start to work with a plan, new or opened.

        mylist.clear();
        ((BaseAdapter)(adapter)).notifyDataSetChanged();

    }
    //endregion

    private void setActivityBuildListeners() {
        // set the object listeners for the activity related to managing plans
        Button btnNew = (Button) findViewById(R.id.btnNew);
        btnNew.setOnClickListener(onClick_btnNew);
        Button btnOpen = (Button) findViewById(R.id.btnOpen);
        btnOpen.setOnClickListener(onClick_btnOpen.get());
        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(onClick_btnSave);
        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(onClick_btnDelete);
        Button btnSet = (Button) findViewById(R.id.btnSet);
        btnSet.setOnClickListener(onClick_btnSet);
        ListView lstGaits = (ListView) findViewById(R.id.lstMyGaits);
        lstGaits.setOnItemClickListener(onClick_lstGaits);
        setSpinner();

        GridLayout grdEntry = (GridLayout) findViewById(R.id.grdEntry);
        grdEntry.setVisibility(INVISIBLE);
    }
    private void setSpinner() {

        //get and build the list of gaits in the DB

        // set the spinner (dropdown) widget
        Spinner spinner = (Spinner) findViewById(R.id.spinner_gaits);
       // ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, gaits, android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,allGaits());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(onItemSelected);
    }
    private ArrayList<String> allGaits() {
        ArrayList<String> gaits = new ArrayList<String>();

        // get the gaits
        eqDatabaseService eqDB = new eqDatabaseService(context,2);
        List<eqCustomGaits_dt> dbGaits = eqDB.getAllGaits();

        // loop through and pull out gait names and save in array
        for (eqCustomGaits_dt row : dbGaits) {
            gaits.add(row.getGait().toString());
        }

        return gaits;
    }
}
