package com.newproject.jhull3341.trackiteq;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jhull3341 on 5/3/2017.
 */

public class TrackItEqManageGaits extends AppCompatActivity
        implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    final Context context = this;
    private static final String eTAG = "Exception";
    private String dbPath = "";
    private String appPath = "";
    private static final String DATABASE_NAME = "EqConditioning.db";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setTitle(R.string.activityManageGaits);
        setContentView(R.layout.activity_manage_gaits);

        Toolbar mySecondaryToolbar = (Toolbar) findViewById(R.id.my_gaits_toolbar);
        setSupportActionBar(mySecondaryToolbar);

        RelativeLayout rtlAddGait = (RelativeLayout) findViewById(R.id.rltAddGait);
       // rtlAddGait.setVisibility(View.INVISIBLE);

        dbPath = this.getDatabasePath(DATABASE_NAME).toString();
        appPath = this.getApplicationInfo().dataDir;
        Log.i(eTAG, "MainActivity dbPath: " + dbPath);
        Log.i(eTAG, "MainActivity appPath: " + appPath);
        eqDatabaseService eqDB = new eqDatabaseService(this,2);

        listAllGaits(eqDB);

        eqDB.close();

        setActivityMainListeners();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.i("Exception", "Set up secondary  menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gaits_toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_back2:
                // this send the app over to the plan management tool activity
                Intent buildIntent = new Intent(context, TrackItEqMainActivity.class);
                startActivity(buildIntent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    private ImageButton.OnClickListener onClick_btnDeleteIt = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            eqDatabaseService eqDB = new eqDatabaseService(context,2);
            EditText txtGait = (EditText) findViewById(R.id.txtGait);
            EditText txtPace = (EditText) findViewById(R.id.txtPace);

            eqDB.deleteCustomGait(txtGait.getText().toString());

            txtGait.setText("");
            txtPace.setText("");

            listAllGaits(eqDB);

        }
    };
    private ImageButton.OnClickListener onClick_btnAddIt = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            eqDatabaseService eqDB = new eqDatabaseService(context,2);

            EditText txtGait = (EditText) findViewById(R.id.txtGait);
            EditText txtPace = (EditText) findViewById(R.id.txtPace);

            eqCustomGaits_dt gait = new eqCustomGaits_dt();

            gait.setName(eqCustomGaits_dt.DEFAULT_NAME);
            gait.setCategory(eqCustomGaits_dt.DEFAULT_CATEGORY);
            gait.setUom(eqCustomGaits_dt.DEFAULT_UOM);
            gait.setGait(txtGait.getText().toString());
            gait.setPace(Integer.parseInt(txtPace.getText().toString()));

            eqDB.insertCustomGait(gait);
            eqDB.close();

            txtGait.setText("");
            txtPace.setText("");
            txtGait.findFocus();

            listAllGaits(eqDB);

        }
    };

    private ListView.OnItemClickListener onClick_lstGaits = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView txtgait = (TextView) view.findViewById(R.id.txtgName);
            TextView txtpace = (TextView) view.findViewById(R.id.txtgPace);

            TextView pGait = (TextView) findViewById(R.id.txtGait);
            TextView ppace = (TextView) findViewById(R.id.txtPace);
            pGait.setText(txtgait.getText());
            ppace.setText(txtpace.getText());

        }
    };
    //region local functions
    private void listAllGaits(eqDatabaseService eqDB) {

        List<eqCustomGaits_dt> allGaits = eqDB.getAllGaits();    //get the data from the database

        ListView lstGaits = (ListView) findViewById(R.id.lstGaits);
        ListView lstHeader = (ListView) findViewById(R.id.lstHeader);

        ArrayList<HashMap<String, String>> mylist, mylist_title;   //set up arrys to hold data for columned listview
        ListAdapter adapter_title, adapter;     // adapters that are send to the liseview adapters, this is how the data is added
        HashMap<String, String> map1, map2;     // not certain but I think it holds the structrue for the arrays

        // create holding arrays

        mylist = new ArrayList<HashMap<String, String>>();
        mylist_title = new ArrayList<HashMap<String, String>>();

        // set up the headings

        map1 = new HashMap<String, String>();

        map1.put("keyGait","Gait");  // ????
        map1.put("keyPace","Pace");  // ????

        mylist_title.add(map1);    // I'm figuring this creates the
        try {
            adapter_title = new SimpleAdapter(this, mylist_title, R.layout.gaits_columns,
                    new String[] { "keyGait", "keyPace" }, new int[] {
                    R.id.txtgName, R.id.txtgPace });
            lstHeader.setAdapter(adapter_title);
        } catch (Exception e) {

        }

        // process the gaits from the table into an adapter to show data
        if (!allGaits.isEmpty()) {

            for (eqCustomGaits_dt row : allGaits) {
                map2 = new HashMap<String, String>();               // create a holder for a row

                map2.put("keyGait",row.getGait());                  // put the col data in
                map2.put("keyPace", row.getPace().toString());      // put the col data in
                mylist.add(map2);                                   // add the row to the list

                Log.d(eTAG, row.getName() + "," + row.getCategory() + "," + row.getGait() + "," + row.getUom() + "," + row.getPace().toString());
            }

            try {
                adapter = new SimpleAdapter(this, mylist, R.layout.gaits_columns,
                        new String[] { "keyGait", "keyPace" }, new int[] {
                        R.id.txtgName, R.id.txtgPace  });
                lstGaits.setAdapter(adapter);
            } catch (Exception e) {

            }
            //lstGaits.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));

        } else {
            lstGaits.setAdapter(null);
        }
    }

    private void setActivityMainListeners() {

        ImageButton btnDeleteIt = (ImageButton) findViewById(R.id.btnDeleteIt);
        btnDeleteIt.setOnClickListener(onClick_btnDeleteIt);
        ImageButton btnAddIt = (ImageButton) findViewById(R.id.btnAddIt);
        btnAddIt.setOnClickListener(onClick_btnAddIt);
        ListView lstGaits = (ListView) findViewById(R.id.lstGaits);
        lstGaits.setOnItemClickListener(onClick_lstGaits);

    }

    //endregion

}
