package ro.stefanhalus.android.blindtransport;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import ro.stefanhalus.android.blindtransport.DatabaseModel.DBHelper;
import ro.stefanhalus.android.blindtransport.DatabaseModel.LinesArrayAdapter;
import ro.stefanhalus.android.blindtransport.Models.LinesModel;

public class BusesActivity extends AppCompatActivity {

    private DBHelper btDb = new DBHelper(this);
    private ListView busesStops;
    private String stationName;
    public static ArrayList<LinesModel> selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buses);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        selected = new ArrayList<>();
        busesStops = findViewById(R.id.lvBusesStops);
        stationName = getIntent().getStringExtra("stationName");
        fillBusesStopping();
        TextView tVStationid = findViewById(R.id.tvStationId);
        tVStationid.setText(getString(R.string.activity_buses_message, stationName));
        goToWaitPage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            case R.id.action_search:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(R.layout.dialog_about)
                        .setTitle(R.string.dialog_about_title);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
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
