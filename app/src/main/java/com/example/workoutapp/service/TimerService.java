package com.example.workoutapp.service;
import android.app.Service;
import android.os.CountDownTimer;

public class TimerService extends Service {
    @Override
    public int onStartCommand(android.content.Intent intent, int flags, int startId) {
        int durata = intent.getIntExtra("durata", 60);
        new CountDownTimer(durata * 1000, 1000) {
            public void onTick(long millis) { /* Aggiorna notifica */ }
            public void onFinish() { /* Notifica fine pausa */ }
        }.start();
        return START_NOT_STICKY;
    }
    @Override public android.os.IBinder onBind(android.content.Intent i) { return null; }
}