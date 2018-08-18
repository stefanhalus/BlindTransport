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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ro.stefanhalus.android.blindtransport.DatabaseModel.DBHelper;
import ro.stefanhalus.android.blindtransport.DatabaseModel.LinesModel;
import ro.stefanhalus.android.blindtransport.DatabaseModel.StationsModel;
import ro.stefanhalus.android.blindtransport.DatabaseModel.StopsModel;

public class MainActivity extends AppCompatActivity {

    // TODO Auto-generated method stub
    private DBHelper btDb = new DBHelper(this);
    protected Context context = this;
    private ListView stationsList;
    private EditText filter_stations_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        insertStations();

        stationsList = findViewById(R.id.list_stations);
//        fillStationsList();
        fillStationsListObj();

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
        ArrayList<String> array_list = btDb.getAllStations();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, array_list);
        stationsList.setAdapter(arrayAdapter);
        stationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(view.getContext(), BusesActivity.class);
                i.putExtra("stationId", String.valueOf(id));
                startActivityForResult(i, 0);
            }
        });
    }

    private void fillStationsListObj() {
        ArrayList<StationsModel> items = btDb.getAllStationsAsArrays();
        ArrayAdapter<StationsModel> adapter = new ArrayAdapter<>(this, R.layout.util_list_item_simple, items);
//        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, items);


        stationsList.setAdapter(adapter);
        stationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(view.getContext(), BusesActivity.class);
                i.putExtra("stationId", String.valueOf(id));
                i.putExtra("stationName", ((TextView) view).getText().toString());
                startActivityForResult(i, 0);
            }
        });
    }

    private void fillStationsListObj(String search) {
        ArrayList<StationsModel> items = btDb.getAllStationsAsArrays(search);
        ArrayAdapter<StationsModel> adapter = new ArrayAdapter<>(this, R.layout.util_list_item_simple, items);

        stationsList.setAdapter(adapter);
        stationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(view.getContext(), BusesActivity.class);
                i.putExtra("stationId", position);
                i.putExtra("stationName", ((TextView) view).getText().toString());
                startActivityForResult(i, 0);
            }
        });
    }


    private void fillStationsList(String search) {
        ArrayList<String> array_list = btDb.getAllStationsSearch(search);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, array_list);
        stationsList.setAdapter(arrayAdapter);
        stationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(view.getContext(), BusesActivity.class);
                i.putExtra("stationId", String.valueOf(id));
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
                    fillStationsListObj(s.toString());
                } else {
                    fillStationsListObj();
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
                    fillStationsListObj(search_station);
                    return true;
                } else {
                    fillStationsListObj();
                    return true;
                }
            }
        });
    }

    public void copyDataBase() throws IOException {
        String package_name = context.getPackageName();
        String DB_PATH = "/data/data/" + package_name + "/databases/";
        String DB_NAME = "blind_transport";
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
