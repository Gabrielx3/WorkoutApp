package com.example.workoutapp.service;

import android.Manifest;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.*;
import com.example.workoutapp.utils.NotificationHelper;

public class TimerService extends Service {
    public static final String AZIONE_TIMER = "com.example.workoutapp.TIMER_TICK";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int durata = intent.getIntExtra("durata", 60);

        // Avvia il servizio in primo piano con la notifica
        startForeground(1, NotificationHelper.ottieniCostruttore(this, "Partenza...").build());

        // Fai partire il cronometro
        new CountDownTimer(durata * 1000, 1000) {
            @Override
            public void onTick(long millis) {
                int rimanenti = (int) (millis / 1000);
                // Invia i secondi rimanenti all'Activity per aggiornare lo schermo
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager != null) {
                    manager.notify(1, NotificationHelper.ottieniCostruttore(TimerService.this, "Mancano " + rimanenti + " secondi").build());
                }

                Intent intentTick = new Intent(AZIONE_TIMER);
                intentTick.putExtra("secondi", rimanenti);
                sendBroadcast(intentTick);
            }

            @Override
            public void onFinish() {
                // Vibrazione finale
                Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (v != null){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        v.vibrate(500);
                    }
                }
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager != null) manager.cancel(1);
                stopSelf(); // Ferma il servizio
            }
        }.start();

        return START_NOT_STICKY;
    }

    @Override public IBinder onBind(Intent intent) { return null; }
}