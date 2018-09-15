package ro.stefanhalus.android.blindtransport;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.filter.ibeacon.IBeaconFilters;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.ble.manager.listeners.ScanStatusListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleScanStatusListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import config.Keys;
import ro.stefanhalus.android.blindtransport.DatabaseModel.CardsArrayAdapter;
import ro.stefanhalus.android.blindtransport.DatabaseModel.DBHelper;
import ro.stefanhalus.android.blindtransport.Models.BeaconFoundModel;
import ro.stefanhalus.android.blindtransport.Models.LinesFoundModel;
import ro.stefanhalus.android.blindtransport.Models.LinesModel;
import ro.stefanhalus.android.blindtransport.Utils.NotificationReceiver;

import static ro.stefanhalus.android.blindtransport.App.CHANNEL_1_ID;
import static ro.stefanhalus.android.blindtransport.App.CHANNEL_2_ID;

public class WaitingActivity extends AppCompatActivity {
    private DBHelper btDb = new DBHelper(this);
    private Vibrator vibrator;
    private NotificationManagerCompat notificationManager;
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    private ProximityManager proximityManager;
    private TextView waitingLines;
    private HashMap<Integer, LinesFoundModel> foundLines;

    CardsArrayAdapter adapter;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        context = this;

        notificationManager = NotificationManagerCompat.from(this);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        foundLines = new HashMap<>();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Așteptăm...");

        waitingLines = findViewById(R.id.waitingLines);
        fillWaitingLines();

        KontaktSDK.initialize(Keys.kontaktioApiKey);
        proximityManager = ProximityManagerFactory.create(context);
        proximityManager.setIBeaconListener(createIBeaconListener());
        proximityManager.setScanStatusListener(createScanStatusListener());
        configureFilters();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startScanning();
    }

    @Override
    protected void onStop() {
//        proximityManager.stopScanning();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        proximityManager.disconnect();
        proximityManager = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    private void startScanning() {
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.startScanning();
            }
        });
    }

    private IBeaconListener createIBeaconListener() {
        return new SimpleIBeaconListener() {
            @Override
            public void onIBeaconDiscovered(IBeaconDevice ibeacon, IBeaconRegion region) {
                BeaconFoundModel beaconColectedData = beaconColectingData(ibeacon);
                beaconStore(ibeacon, beaconColectedData);
                beaconAdvertiser();
            }

            @Override
            public void onIBeaconLost(IBeaconDevice ibeacon, IBeaconRegion region) {
                beaconClean(ibeacon);
            }
        };
    }

    // Collecting beacon data
    private BeaconFoundModel beaconColectingData(IBeaconDevice ibeacon) {
        BeaconFoundModel line = btDb.getBeaconByName(ibeacon.getUniqueId());
        BeaconFoundModel beaconFound = new BeaconFoundModel();
        beaconFound.setBeaconName(ibeacon.getName());
        beaconFound.setBeaconUUID(ibeacon.getUniqueId());
        beaconFound.setBeaconProximityUid(ibeacon.getProximityUUID().toString());
        beaconFound.setBeaconMajor(ibeacon.getMajor());
        beaconFound.setBeaconMinor(ibeacon.getMinor());
        beaconFound.setBeaconDistance((Double) ibeacon.getDistance());
        beaconFound.setBeaconBattery(ibeacon.getBatteryPower());
        beaconFound.setLineId(line.getLineId());
        beaconFound.setLineName(line.getLineName());
        beaconFound.setLineStart(line.getLineStart());
        beaconFound.setLineEnd(line.getLineEnd());
        beaconFound.setLineStartName(line.getLineStartName());
        beaconFound.setLineEndName(line.getLineEndName());
        return beaconFound;
    }

    // FoundBeacons cleanup
    private void beaconClean(IBeaconDevice ibeacon) {
        if (foundLines.containsKey(ibeacon.getMinor()))
            foundLines.remove(ibeacon.getMinor());
    }

    // FoundBeacons store device
    private void beaconStore(IBeaconDevice beacon, BeaconFoundModel line) {
        Double distance = beacon.getDistance();
        if (foundLines.containsKey(beacon.getMinor())) {
            LinesFoundModel inHashMap = foundLines.get(beacon.getMinor());
            if (beaconFrontBack(beacon).equals("B")) {
                foundLines.remove(beacon.getMinor());
                inHashMap.setLineDistanceBack(distance);
                foundLines.put(beacon.getMinor(), inHashMap);
            } else if (beaconFrontBack(beacon).equals("A")) {
                foundLines.remove(beacon.getMinor());
                inHashMap.setLineDistanceFront(distance);
                foundLines.put(beacon.getMinor(), inHashMap);
            }
        } else {
            Double distFront = .0, distBack = .0;
            if (beaconFrontBack(beacon).equals("F")) {
                distFront = distance;
            } else if (beaconFrontBack(beacon).equals("B")) {
                distBack = distance;
            }
            foundLines.put(beacon.getMinor(), new LinesFoundModel(line.getLineName(), line.getLineStart(), line.getLineEnd(), distBack, distFront));
        }
    }

    private void beaconAdvertiser() {
        for (Map.Entry<Integer, LinesFoundModel> entry : foundLines.entrySet()) {
            advertiseNotificationChannel1(entry.getValue());
        }
        Collection<LinesFoundModel> values = foundLines.values();
        ArrayList<LinesFoundModel> listOfValues = new ArrayList<>(values);
        lv = (ListView) findViewById(R.id.lv);
        adapter = new CardsArrayAdapter(this, listOfValues);
        lv.setAdapter(adapter);
    }

    private String beaconFrontBack(IBeaconDevice beacon) {
        return beacon.getName().replace("CTP0" + Integer.toString(beacon.getMinor()), "");
    }

    public void advertiseNotificationChannel1(LinesFoundModel linesFound) {
        long[] busMorse = new long[]{20, 400, 50, 200, 50, 200, 50, 200, 300, 200, 50, 200, 50, 400, 300, 200, 50, 200, 50, 200, 500};
        String lineOk = linesFound.getLineName().toLowerCase().replace(" ", "");
        Uri notificationLine;
        if (screenReader()) {
            notificationLine = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/notify");
        } else {
            notificationLine = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/l_" + lineOk);
        }
        String title = "Linia " + linesFound.getLineName() + " a sosit!";
        String message = "Linia " + linesFound.getLineName() + " ";
        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setDefaults(Notification.FLAG_ONGOING_EVENT)
                .setContentTitle(title)
                .setContentText(message)
                .setOnlyAlertOnce(false)
                .setContentIntent(actionIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.icon_blind_transport_2)
                .setColorized(true)
                .setColor(Color.parseColor("#3a013f"))
                .setLights(Color.MAGENTA, 5000, 3000)
                .setVibrate(busMorse)
                .setSound(notificationLine)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notification);
    }

    public void sendOnChannel2(View v) {
        String title = "Channel 2 title";
        String message = "Channel 2 description";

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.icon_blind_transport_2)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(Color.YELLOW)
                .setOnlyAlertOnce(false)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        notificationManager.notify(2, notification);
    }

    private void fillWaitingLines() {
        StringBuilder lineWaiting = new StringBuilder();
        HashMap<Integer, LinesModel> lines = BusesActivity.selectedCheckboxItems;
        for (LinesModel line : lines.values()) {
            lineWaiting.append("[").append(line.getName()).append("] ");
        }
        waitingLines.setText(getString(R.string.activity_waiting_text, lineWaiting.toString()));
    }

    private boolean screenReader() {
        AccessibilityManager am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        boolean isAccessibilityEnabled = am.isEnabled();
        boolean isExploreByTouchEnabled = am.isTouchExplorationEnabled();
        return isExploreByTouchEnabled;
    }

    private ScanStatusListener createScanStatusListener() {
        return new SimpleScanStatusListener() {
            @Override
            public void onScanStart() {
            }

            @Override
            public void onScanStop() {
            }
        };
    }

    private void configureFilters() {
        proximityManager.filters().iBeaconFilter(IBeaconFilters.newMajorFilter(Keys.iBeaconMajor));
    }

}
