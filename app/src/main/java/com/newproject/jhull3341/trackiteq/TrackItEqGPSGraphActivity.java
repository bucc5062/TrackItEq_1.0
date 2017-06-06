package com.newproject.jhull3341.trackiteq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

/**
 * Created by jhull3341 on 6/6/2017.
 */

public class TrackItEqGPSGraphActivity extends AppCompatActivity {

    private static final String eTAG = "Exception";
    final Context context = this;
    private List<eqGPSPositions_dt> myRun;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(eTAG, "onCreate GPS Graph");

        Intent i = getIntent();
        myRun = i.getParcelableArrayListExtra("gpsdata");

        setTitle(R.string.activityGPSGraph);
        setContentView(R.layout.activity_track_it_eq_graph);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_gps_graph_toolbar);
        setSupportActionBar(myToolbar);

        GraphView graph = (GraphView) findViewById(R.id.gphGPSData);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>();
        int c = 0;
        int size = myRun.size();

        DataPoint values[] = new DataPoint[size];
        DataPoint values1[] = new DataPoint[size];

        for (eqGPSPositions_dt dt:myRun) {
            Integer xi = c;
            Integer yi = (int)dt.getGpsSpeed();
            Integer y2 = (int)dt.getAvgSpeed();
            DataPoint v = new DataPoint(xi, yi);
            DataPoint v1 = new DataPoint(xi, y2);

            values[c] = v;
            values1[c] = v1;

            c++;
        }
        series = new LineGraphSeries<DataPoint>(values);
        series1 = new LineGraphSeries<DataPoint>(values1);
        graph.addSeries(series);
        graph.addSeries(series1);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_gps_exit:
                // this send the app over to the plan management tool activity
                Intent buildIntent = new Intent(context, TrackItEqGPSActivity.class);
                startActivity(buildIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.i("Exception", "Set up secondary gps graph menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gps_menu,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
