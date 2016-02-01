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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import static android.view.View.*;

public class TrackItEqMainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    public int rowCount = 1;
    private static final String eTAG = "Exception";
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(eTAG, "onCreate Build");

        setTitle(R.string.activityManagePlans);
        setContentView(R.layout.activity_track_it_eq_build);

        Toolbar mySecondaryToolbar = (Toolbar) findViewById(R.id.my_secondaryToolbar);
        setSupportActionBar(mySecondaryToolbar);

        setActivityBuildListeners();

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
        }

        return super.onOptionsItemSelected(item);
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
    private Button.OnClickListener onClick_btnOpen = new OnClickListener() {
        @Override
        public void onClick(View v) {

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.activity_track_it_eq_open_plan);
            dialog.setTitle("Open Exercise Plan...");

            Button diaCancelButton = (Button) dialog.findViewById(R.id.btnCancelOpen);
            diaCancelButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            // get a list of files from the local app plans
            ListView lvPlan;
            ArrayList<String> FilesInFolder = GetFiles(getString(R.string.local_data_path));
            if (FilesInFolder == null) {
                Toast toast = new Toast(context);
                toast.setGravity(Gravity.TOP,0,0);
                toast.makeText(context,"No Files to open, Please create one!",Toast.LENGTH_LONG).show();
                return;
            }
            lvPlan = (ListView)dialog.findViewById(R.id.lvPlans);

            lvPlan.setAdapter(new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,FilesInFolder));

            lvPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    String data=(String)parent.getItemAtPosition(position);
                    ArrayList<String> planData = readFromFile(data);
                    clearGrid();
                    for (String row:planData)
                    {
                        String[] rowData = row.split(",");
                        buildDisplayLine(false,rowData[0],rowData[1]);
                    }

                    GridLayout grdEntry = (GridLayout) findViewById(R.id.grdEntry);
                    grdEntry.setVisibility(VISIBLE);
                    dialog.dismiss();

                }
            });
            dialog.show();
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

                    String fileData = "";

                    try {
                        ViewGroup view = (ViewGroup) findViewById(android.R.id.content);
                        ArrayList<View> allGrids = getViewsByTag(view, "elementData");
                        for (View grd : allGrids) {
                            TextView txt = (TextView) grd;
                            String[] elementData = txt.getText().toString().split(" ");
                            String gait = elementData[1];
                            String time = elementData[3];
                            fileData += gait + "," + time + "\n";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    EditText planName = (EditText)dialog.findViewById(R.id.txtPlanName);

                    try {
                        writeToFile(planName.getText().toString() + ".csv", fileData);
                    } catch (Exception e) {
                        e.printStackTrace();
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
            ListView lvPlan;
            ArrayList<String> FilesInFolder = GetFiles(getString(R.string.local_data_path));
            if (FilesInFolder == null) {
                Toast toast = new Toast(context);
                toast.setGravity(Gravity.TOP,0,0);
                toast.makeText(context,"No Files to delete, Please create one!",Toast.LENGTH_LONG).show();
                return;
            }
            lvPlan = (ListView)dialog.findViewById(R.id.lvPlans);

            lvPlan.setAdapter(new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,FilesInFolder));

            lvPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    String data=(String)parent.getItemAtPosition(position);

                    File dFile = new File(getString(R.string.local_data_path),data);
                    Boolean deleted = dFile.delete();

                    dialog.dismiss();

                }
            });
            dialog.show();

        }
    };
    private Button.OnClickListener onClick_btnSet = new OnClickListener() {
        @Override
        public void onClick(View v) {
            buildDisplayLine(false,"","");
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
    private void buildDisplayLine(boolean isStart, String gait, String time) {

        // create a layout params to define how we add

        LinearLayout.LayoutParams imgLayout =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

        // create a grid layout to add controls
        GridLayout newGrid = new GridLayout(this);
        newGrid.setId(100 + rowCount);
        newGrid.setColumnCount(2);
        newGrid.setTag("gridRow");

        // set text for display row
        String gtResult;
        String tmeResult;
        String display;
        
        if (gait.equals("")) {
            Spinner spinner = (Spinner) findViewById(R.id.spinner_gaits);
            TextView textGait = (TextView) spinner.getSelectedView();
            gtResult = textGait.getText().toString();
            spinner.requestFocus();
        } else
        {
            gtResult = gait;
        }
        if (time.equals("")) {
            TextView txtTime = (TextView) findViewById(R.id.txtSelected);
            tmeResult = txtTime.getText().toString();
            txtTime.setText("");
        } else
        {
            tmeResult = time;
        }

        if (isStart) {
            display = rowCount + ": " + "Start" + " - " + "0" + " min";
        } else {
            display = rowCount + ": " + gtResult + " - " + tmeResult + " min";
        }

        // create a text view control
        TextView newText = new TextView(this);
        Resources r = getResources();
        int pc = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, r.getDisplayMetrics());
        newText.setWidth(pc);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(50, 0, 0, 0); // llp.setMargins(left, top, right, bottom);
        newText.setLayoutParams(llp);
        //newText.setTextAppearance(android.R.style.TextAppearance_Large);
        newText.setText(display);
        newText.setId(300 + rowCount);
        newText.setTag("elementData");
        // create a new button
        Button newButton = new Button(this);
        newButton.setLayoutParams(imgLayout);
        newButton.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
        newButton.getLayoutParams().width = toPixels(79);
        newButton.setText(R.string.btnDelTitle);
        newButton.setId(400 + rowCount);
        newButton.setOnClickListener(onClick_btnRemove);

        // now add to the original grid layout
        newGrid.addView(newText);
        newGrid.addView(newButton);

        LinearLayout my_root = (LinearLayout) findViewById(R.id.my_root);
        LinearLayout A = new LinearLayout(this);
        A.setOrientation(LinearLayout.HORIZONTAL);
        my_root.addView(newGrid);
        rowCount += 1;

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
            File file = new File(getString(R.string.local_data_path),fName);
            FileWriter oFile = new FileWriter(file);    // no append
            oFile.write(allData);
            oFile.flush();
            oFile.close();
        }
        catch (IOException e) {
            Log.i(eTAG, e.getMessage());
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
        
        rowCount = 1;
        ViewGroup view = (ViewGroup) findViewById(android.R.id.content);

        ArrayList<View> allGrids = getViewsByTag(view,"gridRow");
        for (View grd: allGrids) grd.setVisibility(GONE);
    }
    //endregion

    private void setActivityBuildListeners() {
        // set the object listeners for the activity related to managing plans
        Button btnNew = (Button) findViewById(R.id.btnNew);
        btnNew.setOnClickListener(onClick_btnNew);
        Button btnOpen = (Button) findViewById(R.id.btnOpen);
        btnOpen.setOnClickListener(onClick_btnOpen);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(onClick_btnSave);
        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(onClick_btnDelete);
        Button btnSet = (Button) findViewById(R.id.btnSet);
        btnSet.setOnClickListener(onClick_btnSet);
        // set the spinner (dropdown) widget
        Spinner spinner = (Spinner) findViewById(R.id.spinner_gaits);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gaits_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(onItemSelected);

        GridLayout grdEntry = (GridLayout) findViewById(R.id.grdEntry);
        grdEntry.setVisibility(INVISIBLE);
    }


}
