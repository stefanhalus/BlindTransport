package ro.stefanhalus.android.blindtransport.Utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;


import java.util.ArrayList;
import java.util.List;

import ro.stefanhalus.android.blindtransport.BusesActivity;
import ro.stefanhalus.android.blindtransport.Models.LinesModel;
import ro.stefanhalus.android.blindtransport.R;
import ro.stefanhalus.android.blindtransport.WaitingActivity;

public class AppNotificationManager {

    private final static String EXTRA_NOTIFICATION_ID = "notification_id";
    private final static String APP_PACKAGE = "ro.stefanhalus.android.blind_transport";
    private final static String LINES_CHANEL_ID = APP_PACKAGE + ".LINES_CHANNEL";
    private final static String APP_CHANEL_ID = APP_PACKAGE + ".APP_CHANNEL";
    private final static String GROUP_KEY_LINES = APP_CHANEL_ID + ".LINES_GROUP";
    private final static long BASE_NOTIFICATION_ID = 100L;
    private final static int INVALID_NOTIFICATION_ID = -1;

    @NonNull
    private Context mContext;

    public AppNotificationManager(final @NonNull Context context) {
        mContext = context;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            final List<NotificationChannel> channels = new ArrayList<>();
            channels.add(createAppNotificationChanel(
                    LINES_CHANEL_ID,
                    mContext.getString(R.string.notification_channel_name),
                    mContext.getString(R.string.notification_channel_description),
                    NotificationManagerCompat.IMPORTANCE_HIGH));

            channels.add(createAppNotificationChanel(
                    APP_CHANEL_ID,
                    mContext.getString(R.string.notification_channel_app_name),
                    mContext.getString(R.string.notification_channel_app_description),
                    NotificationManagerCompat.IMPORTANCE_DEFAULT));

            final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannels(channels);
            }
        }
    }

    private void showNotification(final @NonNull Notification notification, final int notificationId) {
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(notificationId, notification);
    }

    @SuppressLint("ResourceAsColor")
    private Notification createCustomNotification(final NotificationCompat.Action action, final String message, final PendingIntent contentIntent) {
        return new NotificationCompat.Builder(mContext, LINES_CHANEL_ID)
                .setSmallIcon(R.drawable.icon_blind_transport_2)
                .setContentTitle(mContext.getString(R.string.notification_title, message))
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setColor(R.color.colorPrimary)
                .setColorized(true)
                .setPriority(7)
                .setUsesChronometer(true)
                .setTimeoutAfter(7777)
                .setSound(Uri.parse("android.resource://" + mContext.getPackageName() + "l35"))
//                .addAction(action)
                .setGroup(GROUP_KEY_LINES)
                .build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationChannel createAppNotificationChanel(final String chanelId, final String chanelName, final String chanelDescription, final int chanelImportance) {
        NotificationChannel channel = new NotificationChannel(chanelId, chanelName, chanelImportance);
        channel.setDescription(chanelDescription);
        return channel;
    }

    public void showBusArrived(final @NonNull LinesModel line) {
        final Intent busArrivedIntent = new Intent(mContext, WaitingActivity.class);
        final int notificationId = (int) (BASE_NOTIFICATION_ID + line.getId());
        busArrivedIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
        final PendingIntent busArrivedPendingIntent = PendingIntent.getActivity(
                mContext,
                notificationId,
                busArrivedIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        final Intent detailLineIntent = new Intent(mContext, WaitingActivity.class);
        detailLineIntent.putExtra(WaitingActivity.LINE_ID, line.getId());
        PendingIntent detailPendingIntent = PendingIntent.getActivity(
                mContext,
                notificationId,
                detailLineIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        final NotificationCompat.Action busArrivedAction = new NotificationCompat.Action(
                R.drawable.icon_blind_transport_2,
                mContext.getString(R.string.notification_action_waiting),
                busArrivedPendingIntent);
        final Notification notification = createCustomNotification(
                busArrivedAction,
                line.getName(),
                detailPendingIntent);
        showNotification(notification, notificationId);
        VibrateUtil.busNotification(mContext);
//        PlayBackUtil.play();
    }


    public void showDetailsNotificationWithAllCitiesAction(final @NonNull LinesModel line) {
        final Intent allCitiesIntent = new Intent(mContext, WaitingActivity.class);
        final int notificationId = (int) (BASE_NOTIFICATION_ID + line.getId());
        allCitiesIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
        final PendingIntent allCitiesPendingIntent = PendingIntent.getActivity(
                mContext,
                notificationId,
                allCitiesIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        final Intent detailLineIntent = new Intent(mContext, BusesActivity.class);
        detailLineIntent.putExtra(WaitingActivity.LINE_ID, line.getId());
        PendingIntent detailPendingIntent = PendingIntent.getActivity(
                mContext,
                notificationId,
                detailLineIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        final NotificationCompat.Action allCitiesAction = new NotificationCompat.Action(
                R.drawable.icon_blind_transport,
                mContext.getString(R.string.notification_action_waiting),
                allCitiesPendingIntent);
        final Notification notification = createCustomNotification(
                allCitiesAction,
                line.getName(),
                detailPendingIntent);
        showNotification(notification, notificationId);
    }

    public void showBundleNotification(final int notificationCount) {
        final Notification summaryNotification = new NotificationCompat.Builder(mContext, LINES_CHANEL_ID)
                .setContentText(notificationCount + " cities")
                .setSmallIcon(R.drawable.icon_blind_transport)
                .setStyle(new NotificationCompat.InboxStyle())
                .setGroup(GROUP_KEY_LINES)
                .setGroupSummary(true)
                .build();
        showNotification(summaryNotification, (int) BASE_NOTIFICATION_ID);
    }

    public void hideNotification(final @Nullable Intent intent) {
        final NotificationManager notificationManager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null && intent != null) {
            final int notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, INVALID_NOTIFICATION_ID);
            notificationManager.cancel(notificationId);
        }
    }
}