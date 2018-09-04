package ro.stefanhalus.android.blindtransport;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import ro.stefanhalus.android.blindtransport.DatabaseModel.DBHelper;
import ro.stefanhalus.android.blindtransport.DatabaseModel.LinesArrayAdapter;
import ro.stefanhalus.android.blindtransport.Models.LinesModel;
import ro.stefanhalus.android.blindtransport.Utils.MessageUtil;
import ro.stefanhalus.android.blindtransport.Utils.PermissionUtil;

public class BusesActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int PERMISSION_BLUETOOTH = 0;
    private static final int PERMISSION_BLUETOOTH_ADMIN = 1;
    private static final int PERMISSION_FINE_LOCATION = 2;
    private static final int PERMISSION_COARSE_LOCATION = 5;
    private static final int PERMISSION_INTERNET = 3;
    private static final int PERMISSION_NETWORK_STATE = 4;

    private DBHelper btDb = new DBHelper(this);
    private ListView busesStops;
    private String stationName;
    public Context context;
    public static HashMap<Integer, LinesModel> selectedCheckboxItems;

    @SuppressLint("UseSparseArrays")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buses);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        context = this;
requestFineLocationPermission();

        selectedCheckboxItems = new HashMap<>();
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

        if (id == android.R.id.home) {
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

    private void goToWaitPage() {
        Button btnWait = findViewById(R.id.btnWait);
        btnWait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(BusesActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(v.getContext(), WaitingActivity.class);
                    startActivityForResult(i, 0);
                } else {
                    requestFineLocationPermission();
                }
            }
        });
    }


    private void requestFineLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.toast_permissions_request_message_title))
                    .setMessage(getString(R.string.toast_permissions_request_message_content))
                    .setPositiveButton(getString(R.string.ubil_button_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BusesActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
                        }
                    })
                    .setNegativeButton(getString(R.string.ubil_button_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.toast_permissions_granted), Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(this, getString(R.string.toast_permissions_refused), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
