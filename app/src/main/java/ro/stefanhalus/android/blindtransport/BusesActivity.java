package ro.stefanhalus.android.blindtransport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ro.stefanhalus.android.blindtransport.DatabaseModel.DBHelper;
import ro.stefanhalus.android.blindtransport.DatabaseModel.LinesArrayAdapter;
import ro.stefanhalus.android.blindtransport.DatabaseModel.LinesModel;

public class BusesActivity extends AppCompatActivity {

    private DBHelper btDb = new DBHelper(this);
    private ListView busesStops;
    private String stationName;
    public static ArrayList<LinesModel> selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buses);
        selected = new ArrayList<>();
        busesStops = findViewById(R.id.lvBusesStops);
        stationName = getIntent().getStringExtra("stationName");
        fillBusesStopping();
        TextView tVStationid = findViewById(R.id.tvStationId);
        tVStationid.setText(getString(R.string.activity_buses_message, stationName));
        goToWaitPage();
    }

    private void fillBusesStopping() {
        int station = btDb.getStationIdFromStationName(stationName);
        ArrayList<LinesModel> lines_list = btDb.getStopsByStationId(station);
        LinesArrayAdapter linesAdapter = new LinesArrayAdapter(this, lines_list);
        busesStops.setAdapter(linesAdapter);
    }

    private void goToWaitPage(){
        Button btnWait = findViewById(R.id.btnWait);
        btnWait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), WaitingActivity.class);
                startActivityForResult(i, 0);
            }
        });
    }
}
