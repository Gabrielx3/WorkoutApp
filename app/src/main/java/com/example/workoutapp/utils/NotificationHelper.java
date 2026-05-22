package com.example.workoutapp.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.example.workoutapp.R;

public class NotificationHelper {
    public static final String CHANNEL_ID = "workout_channel";

    public static void creaCanale(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Workout Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = c.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static Notification.Builder getNotifica(Context c, String testo) {
        return new Notification.Builder(c, CHANNEL_ID)
                .setContentTitle("Workout")
                .setContentText(testo)
                .setSmallIcon(R.mipmap.ic_launcher);
    }
}
