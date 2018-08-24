package ro.stefanhalus.android.blindtransport;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ro.stefanhalus.android.blindtransport.DatabaseModel.DBHelper;
import ro.stefanhalus.android.blindtransport.DatabaseModel.StationsArrayAdapter;
import ro.stefanhalus.android.blindtransport.Models.StationsModel;

/** Main Activity loaded as LAUNCER
 * @author Ștefan Halus
 * It provides the entry point of the application.
 * Uses methods to implement database provided in assets package.
 * A method lists all available stations.
 * An edit text runs an overload of stations fill method after third character entered.
 * Filtering is made with a wildcard search %...% upon the `name` column
 * */
public class MainActivity extends AppCompatActivity {

    /** Properties definition
     * Properties used along the class ar declared here.
     * */
    private DBHelper btDb = new DBHelper(this);
    protected Context context = this;
    private ListView stationsList;
    private EditText filter_stations_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // populate data from assets package
        copyDataBase();

        // Identifying the list holder
        stationsList = findViewById(R.id.list_stations);

//        fillStationsList();
        fillStationsList();

        // Identifying the filter imput and runing events
        filter_stations_edit = findViewById(R.id.filter_station);
        filterStationsEventKeyPress();
        filterStationsEventEnterKey();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

    private void fillStationsList() {
        final ArrayList<StationsModel> items = btDb.getAllStationsAsArrays();
        StationsArrayAdapter adapter = new StationsArrayAdapter(this, items);
        stationsList.setAdapter(adapter);
        stationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StationsModel currentStation = items.get(position);
                Intent i = new Intent(view.getContext(), BusesActivity.class);
                i.putExtra("stationId", currentStation.getId());
                i.putExtra("stationName", currentStation.getName());
                startActivityForResult(i, 0);
            }
        });
    }

    private void fillStationsList(String search) {
        final ArrayList<StationsModel> items = btDb.getAllStationsAsArrays(search);
        StationsArrayAdapter adapter = new StationsArrayAdapter(this, items);
        stationsList.setAdapter(adapter);
        stationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StationsModel currentStation = items.get(position);
                Intent i = new Intent(view.getContext(), BusesActivity.class);
                i.putExtra("stationId", currentStation.getId());
                i.putExtra("stationName", currentStation.getName());
                startActivityForResult(i, 0);
            }
        });
    }

    private void filterStationsEventKeyPress() {
        // Filter stations list as you tipe string longer than 3 characters
        filter_stations_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    fillStationsList(s.toString());
                } else {
                    fillStationsList();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filterStationsEventEnterKey() {
        //        Search on ENTER KEY
        filter_stations_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String search_station = filter_stations_edit.getText().toString();
                if (search_station.length() > 2) {
                    fillStationsList(search_station);
                    return true;
                } else {
                    fillStationsList();
                    return true;
                }
            }
        });
    }

    // method executinc the database copying
    public void copyDataBase() {
        String package_name = context.getPackageName();
        String DB_PATH = "/data/data/" + package_name + "/databases/";
        String DB_NAME = "blind_transport.db";
        try {
            InputStream myInput = context.getAssets().open(DB_NAME);
            File dbFile = new File(DB_PATH);
            dbFile.mkdirs();
            String outputFileName = DB_PATH + DB_NAME;
            OutputStream myOutput = new FileOutputStream(outputFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
