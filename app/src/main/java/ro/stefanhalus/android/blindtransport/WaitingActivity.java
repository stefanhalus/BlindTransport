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
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Objects;

import config.Keys;
import ro.stefanhalus.android.blindtransport.Models.LinesModel;
import ro.stefanhalus.android.blindtransport.Utils.NotificationReceiver;
import ro.stefanhalus.android.blindtransport.Utils.VibrateUtil;

import static ro.stefanhalus.android.blindtransport.App.CHANNEL_1_ID;
import static ro.stefanhalus.android.blindtransport.App.CHANNEL_2_ID;

public class WaitingActivity extends AppCompatActivity {
    private Vibrator vibrator;
    private NotificationManagerCompat notificationManager;
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    private ProximityManager proximityManager;
    private TextView waitingLines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        context = this;

        notificationManager = NotificationManagerCompat.from(this);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Așteptăm...");

        waitingLines = findViewById(R.id.waitingLines);
        fillWaitingLines();

        KontaktSDK.initialize(Keys.kontaktioApiKey);
        proximityManager = ProximityManagerFactory.create(context);
        proximityManager.setIBeaconListener(createIBeaconListener());
        proximityManager.setScanStatusListener(createScanStatusListener());
        configureFilters();
        notifyBus();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startScanning();
    }

    @Override
    protected void onStop() {
        proximityManager.stopScanning();
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
//                BeaconScanResult.found.add(ibeacon);

//                sendOnChannel1(new LinesModel(1, ibeacon.getName(), 1, 2));
//                DecimalFormat df = new DecimalFormat("0.00");
//                new MessageUtil(context, "IBeacon detected", "Am gasit un iBeacon \n" +
//                        "Name: " + ibeacon.getName() +
////                        " \nAddress: " + ibeacon.getAddress() +
//                        " \nUUID: " + ibeacon.getUniqueId() +
//                        " \nMajor: " + ibeacon.getMajor() +
//                        " \nMinor: " + ibeacon.getMinor() +
//                        " \nDistance: " +
//                        df.format(ibeacon.getDistance()) +
//                        " \nProximity UID: " + ibeacon.getProximityUUID() +
////                        " m \nRSSI: " + ibeacon.getRssi() +
////                        " \nTX power: " + ibeacon.getTxPower() +
//                        " \nBattery: " + ibeacon.getBatteryPower());
            }
        };
    }

    public void notifyBus() {
        Button btnDemo = findViewById(R.id.btnNotify);
        btnDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOnChannel1(new LinesModel(1, "35",1,2));
            }
        });
    }

    public void sendOnChannel1(LinesModel line) {
        String lineOk = line.getName().toLowerCase().replace(" ", "");
        Uri notificationLine =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/l_" + lineOk);
        String title = "Linia " + line.getName() + " a sosit!";
        String message = "A sosit " + line.getName() + " în direcția ";
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
                .setVibrate(VibrateUtil.patternBusMorse)
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

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WaitingActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ScanStatusListener createScanStatusListener() {
        return new SimpleScanStatusListener() {
            @Override
            public void onScanStart() {
                showToast("Scanning started");
            }
            @Override
            public void onScanStop() {
                showToast("Scanning stopped");
            }
        };
    }

    private void configureFilters() {
        proximityManager.filters().iBeaconFilter(IBeaconFilters.newMajorFilter(Keys.iBeaconMajor));
    }

}
