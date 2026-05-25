package com.example.workoutapp.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    public static final String ID_CANALE = "CanaleTimer";

    // Crea il canale (necessario da Android 8 in su)
    public static void creaCanaleNotifica(Context contesto) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canale = new NotificationChannel(
                    ID_CANALE, "Timer di Recupero", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = contesto.getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(canale);
        }
    }

    // Costruisce la notifica che l'utente vedrà
    public static NotificationCompat.Builder ottieniCostruttore(Context contesto, String testo) {
        return new NotificationCompat.Builder(contesto, ID_CANALE)
                .setContentTitle("Recupero in corso")
                .setContentText(testo)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setOngoing(true); // Impedisce all'utente di cancellarla col dito
    }
}
