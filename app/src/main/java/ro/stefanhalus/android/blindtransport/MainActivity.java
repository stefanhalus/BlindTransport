package ro.stefanhalus.android.blindtransport;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;

import ro.stefanhalus.android.blindtransport.DatabaseModel.DBHelper;

public class MainActivity extends AppCompatActivity {

    private DBHelper btDb = new DBHelper(this);
    private ListView stationsList;
    private EditText filter_stations_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        insertStations();

        stationsList = findViewById(R.id.list_stations);
        fillStationsList();

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

    @SuppressLint("WrongViewCast")
    private void fillStationsList() {
        ArrayList<String> array_list = btDb.getAllStations();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_list);
        stationsList.setAdapter(arrayAdapter);
    }

    private void fillStationsList(String search) {
        ArrayList<String> array_list = btDb.getAllStationsSearch(search);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_list);
        stationsList.setAdapter(arrayAdapter);
//        stationsList.setOnItemClickListener(new AdapterView.onItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
//                Intent appInfo = new Intent(YourActivity.this, ApkInfoActivity.class);
//                startActivity(appInfo);
//            }
    }

    public void insertStations() {
        ArrayList<String> l = new ArrayList();
        l.add("Agronomia");
        l.add("Andrei Muresanu");
        l.add("Anton Pann");
        l.add("Barbu Patriciu");
        l.add("Becas Est");
        l.add("Becas Vest");
        l.add("Bistritei");
        l.add("Borsec Nord");
        l.add("Borsec Sud");
        l.add("Calea Manastur");
        l.add("Centrul Medical");
        l.add("Closca");
        l.add("Compexit");
        l.add("Compl. Tempus Nord");
        l.add("Compl. Tempus Sud");
        l.add("Dacia Service");
        l.add("Dacia Service Sos");
        l.add("Disp. Clabucet");
        l.add("Disp. Unirii");
        l.add("Disp. Zorilor");
        l.add("Drapelului Est");
        l.add("Drapelului Vest");
        l.add("Drum Faget");
        l.add("Fabrica de Bere");
        l.add("Fagului");
        l.add("Garbau");
        l.add("Ghe. Dima");
        l.add("Gheorghe Doja");
        l.add("Gradini Manastur");
        l.add("Herculane");
        l.add("I.P. Voitesti Nord");
        l.add("I.P. Voitesti Sud");
        l.add("Ion Mester ");
        l.add("Izlazului");
        l.add("Memorandumului Nord");
        l.add("Memorandumului Sud");
        l.add("Minerva");
        l.add("N. Titulescu");
        l.add("Observatorului Nord");
        l.add("Observatorului Sud");
        l.add("Opera");
        l.add("Opera");
        l.add("P-ta 1 Mai");
        l.add("P-ta 1 Mai Sos");
        l.add("P-ta Avram Iancu");
        l.add("P-ta Cipariu Est");
        l.add("P-ta Cipariu Nord");
        l.add("P-ta Cipariu Sud");
        l.add("P-ta Garii Sos");
        l.add("P-ta Garii Sud");
        l.add("P-ta I Agarbiceanu Est");
        l.add("P-ta I Agarbiceanu Vest");
        l.add("P-ta M. Viteazul Vest");
        l.add("Paris");
        l.add("Peana");
        l.add("Ploiesti");
        l.add("PMV 2 Sos");
        l.add("Pod Traian");
        l.add("Primaverii");
        l.add("Regionala CFR");
        l.add("Septimiu Albini Nord");
        l.add("Septimiu Albini Sud");
        l.add("Silviu Dragomir");
        l.add("Snagov Nord");
        l.add("Snagov Sud");
        l.add("Sora");
        l.add("Spitalul de Copii");
        l.add("Spitalul Recuperare Nord");
        l.add("Spitalul Recuperare Sud");
        l.add("Teatru");
        l.add("Traian");
        l.add("Trifoiului Nord");
        l.add("Trifoiului Sud");
        l.add("Victoria");
        l.add("Vitacom");
        l.add("Vitacom Nord");
        l.add("Zorilor");
        for (String item : l) {
            btDb.insertStation(item);
        }
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

}
