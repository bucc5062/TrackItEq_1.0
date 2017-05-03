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
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

/**
 * Created by jhull3341 on 5/3/2017.
 */

public class TrackItEqManageGaits extends AppCompatActivity
        implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String eTAG = "Exception";
    final Context context = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setTitle(R.string.activityManageGaits);
        setContentView(R.layout.activity_manage_gaits);

        Toolbar mySecondaryToolbar = (Toolbar) findViewById(R.id.my_gaits_toolbar);
        setSupportActionBar(mySecondaryToolbar);

        RelativeLayout rtlAddGait = (RelativeLayout) findViewById(R.id.rltAddGait);
        rtlAddGait.setVisibility(View.INVISIBLE);

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
}
