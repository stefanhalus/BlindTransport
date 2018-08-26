package ro.stefanhalus.android.blindtransport;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleEddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import java.util.ArrayList;
import java.util.Objects;

import ro.stefanhalus.android.blindtransport.Models.LinesModel;
import ro.stefanhalus.android.blindtransport.Utils.AppNotificationManager;
import ro.stefanhalus.android.blindtransport.Utils.MessageUtil;
import ro.stefanhalus.android.blindtransport.Utils.PermissionUtil;

public class WaitingActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PERMISSION_REQUEST_BLUETOOTH = 0;
    private static final int PERMISSION_REQUEST_BLUETOOTH_ADMIN = 1;
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 2;
    private static final int PERMISSION_REQUEST_INTERNET = 3;

    public static String LINE_ID;
    private String CHANNEL_ID = "Blind Transport";
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    private ProximityManager proximityManager;
    private TextView waitingLines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        context = this;

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        waitingLines = findViewById(R.id.waitingLines);

        StringBuilder lineWaiting = new StringBuilder();
        ArrayList<LinesModel> lines = BusesActivity.selected;
        for (LinesModel line : lines) {
            lineWaiting.append("[").append(line.getName()).append("] ");
        }

        permissionChecker();
        waitingLines.setText(getString(R.string.activity_waiting_text, lineWaiting.toString()));
        beaconStartWorking();
        notifyDemo();
    }

    private void permissionChecker() {
        final Activity thisActivity = (Activity) context;
        PermissionUtil.checkPermission(
                context,
                Manifest.permission.BLUETOOTH,
                new PermissionUtil.PermissionAskListener() {
                    @Override
                    public void onNeedPermission() {
                        ActivityCompat.requestPermissions(
                                thisActivity,
                                new String[]{Manifest.permission.BLUETOOTH},
                                PERMISSION_REQUEST_BLUETOOTH
                        );
                    }

                    @Override
                    public void onPermissionPreviouslyDenied() {
                        //show a dialog explaining permission and then request permission
                        new MessageUtil(context, "BLUETOOTH", "Necesar pentru a funcționa beaconii");
                    }

                    @Override
                    public void onPermissionDisabled() {
                        new MessageUtil(context, "BLUETOOTH", "Permisiune dezactivată");
                        Toast.makeText(context, "Permission BLUETOOTH Disabled.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionGranted() {
//                        permissionAllert("BLUETOOTH", "Permisiune activată");
                    }
                });

        PermissionUtil.checkPermission(
                context,
                Manifest.permission.BLUETOOTH_ADMIN,
                new PermissionUtil.PermissionAskListener() {
                    @Override
                    public void onNeedPermission() {
                        ActivityCompat.requestPermissions(
                                thisActivity,
                                new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                                PERMISSION_REQUEST_BLUETOOTH_ADMIN
                        );
                    }

                    @Override
                    public void onPermissionPreviouslyDenied() {
                        //show a dialog explaining permission and then request permission
                        new MessageUtil(context, "BLUETOOTH_ADMIN", "Necesar pentru a funcționa beaconii");
                    }

                    @Override
                    public void onPermissionDisabled() {
                        new MessageUtil(context, "BLUETOOTH_ADMIN", "Permisiune dezactivată");
                        Toast.makeText(context, "Permission BLUETOOTH_ADMIN Disabled.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionGranted() {
//                        permissionAllert("BLUETOOTH_ADMIN", "Permisiune activată");
                    }
                });

        PermissionUtil.checkPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                new PermissionUtil.PermissionAskListener() {
                    @Override
                    public void onNeedPermission() {
                        ActivityCompat.requestPermissions(
                                thisActivity,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSION_REQUEST_FINE_LOCATION
                        );
                    }

                    @Override
                    public void onPermissionPreviouslyDenied() {
                        //show a dialog explaining permission and then request permission
                        new MessageUtil(context, "ACCESS_FINE_LOCATION", "Necesar pentru a funcționa beaconii");

                    }

                    @Override
                    public void onPermissionDisabled() {
                        new MessageUtil(context, "BLUETOOTH_ADMIN", "Permisiune dezactivată");
                        Toast.makeText(context, "Permission ACCESS_FINE_LOCATION Disabled.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionGranted() {
//                        permissionAllert("BLUETOOTH_ADMIN", "Permisiune activată");
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_BLUETOOTH: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    waitingLines.setText("Avem BLUETOOTH");
                }
                return;
            }
            case PERMISSION_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    waitingLines.setText("Avem FINE LOCATION");
                }
                return;
            }
            case PERMISSION_REQUEST_INTERNET: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    waitingLines.setText("Avem INTERNET");
                }
            }
        }

    }

    private void beaconStartWorking() {

        KontaktSDK.initialize("OvwtQgASSMpYzLCfgQoJlPEALyIUzWHi");

        proximityManager = ProximityManagerFactory.create(this);
        proximityManager.setIBeaconListener(createIBeaconListener());
        proximityManager.setEddystoneListener(createEddystoneListener());

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
                Log.i("Sample", "IBeacon discovered: " + ibeacon.toString());
            }
        };
    }

    private EddystoneListener createEddystoneListener() {
        return new SimpleEddystoneListener() {
            @Override
            public void onEddystoneDiscovered(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                Log.i("Sample", "Eddystone discovered: " + eddystone.toString());
            }
        };
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

    public void notifyDemo() {
        Button btnNotify = findViewById(R.id.btnNotify);
        btnNotify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    AppNotificationManager notify = new AppNotificationManager(context);
                    notify.showBusArrived(new LinesModel(1, "77B", 1, 22));

                } catch (Exception e) {
                    new MessageUtil(context, "Notificare ERR", "Notificarea nu funcționează");
                    e.printStackTrace();
                }
            }
        });
    }


}
