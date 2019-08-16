package com.saver.instasaver.downloadphoto.helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.saver.instasaver.downloadphoto.R;
import com.saver.instasaver.downloadphoto.ui.activities.MainActivity;

import com.saver.instasaver.downloadphoto.utils.Constants;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationHelper {

    /**
     * creating persistence notification for foreground service
     *
     * @param context service's context
     * @return returns notification builder
     * @! it used by executors
     */
    public static NotificationCompat.Builder getPersistenceNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Constants.EXTRA_SWITCH_STATE, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, Constants.PENDING_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(context, Constants.CHANNEL_PERSISTENCE_ID)
                .setSmallIcon(R.drawable.ic_content_copy_black_24dp)
                .setContentTitle(context.getResources().getString(R.string.persistence_notification_title))
                .setContentText(context.getResources().getString(R.string.persistence_notification_text))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);
    }

    /**
     * creting notification channels for downloading and foreground service
     *
     * @param context services's context
     * @! it's for all application
     */

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    Constants.CHANNEL_PERSISTENCE_ID,
                    "Foreground channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationChannel channel2 = new NotificationChannel(
                    Constants.CHANNEL_DOWNLOADING_ID,
                    "Downloading progress channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.enableLights(true);
            channel2.enableLights(true);
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
                manager.createNotificationChannel(channel2);
            }
        }
    }
}
