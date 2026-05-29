package com.example.workoutapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.workoutapp.R;
import com.example.workoutapp.utils.DataManager;

public class MainActivity extends AppCompatActivity {

    private Button btnAddExercise, btnStartWorkout, btnShare, btnGuide, btnSettings;
    private ListView lvExercises;
    private TextView tvTitle;
    private TextView tvTimerCountdown;

    private ExerciseAdapter adapter;
    private java.util.List<com.example.workoutapp.model.Exercise> exerciseList;
    private int selectedPosition = -1;

    private final BroadcastReceiver timerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (com.example.workoutapp.service.TimerService.AZIONE_TIMER.equals(intent.getAction())) {
                int secondi = intent.getIntExtra("secondi", 0);
                if (tvTimerCountdown != null) {
                    tvTimerCountdown.setText("Recupero: " + secondi + "s");
                    if (secondi == 0) {
                        tvTimerCountdown.setVisibility(View.GONE);
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTitle = findViewById(R.id.tvTitle);
        btnAddExercise = findViewById(R.id.btnAddExercise);
        btnStartWorkout = findViewById(R.id.btnStartWorkout);
        btnShare = findViewById(R.id.btnShare);
        btnGuide = findViewById(R.id.btnGuide);
        btnSettings = findViewById(R.id.btnSettings);
        lvExercises = findViewById(R.id.lvExercises);
        tvTimerCountdown = findViewById(R.id.tvTimerCountdown);

        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddExerciseActivity.class);
                startActivity(intent);
            }
        });

        lvExercises.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            Toast.makeText(MainActivity.this, "Selezionato: " + exerciseList.get(position).getNome(), Toast.LENGTH_SHORT).show();
        });

        btnStartWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == -1 || exerciseList.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Seleziona un esercizio dalla lista!", Toast.LENGTH_SHORT).show();
                    return;
                }
                int recupero = exerciseList.get(selectedPosition).getSecondiRecupero();

                Intent serviceIntent = new Intent(MainActivity.this, com.example.workoutapp.service.TimerService.class);
                serviceIntent.putExtra("durata", recupero);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                } else {
                    startService(serviceIntent);
                }
                tvTimerCountdown.setVisibility(View.VISIBLE);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Mi sto allenando con la mia Workout App!");
                startActivity(Intent.createChooser(shareIntent, "Condividi il tuo allenamento"));
            }
        });

        btnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse("https://www.projectinvictus.it/esercizi-palestra/");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("attuale_nome", DataManager.loadUsername(MainActivity.this));
                startActivity(intent);
            }
        });

        com.example.workoutapp.utils.NotificationHelper.creaCanaleNotifica(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String username = DataManager.loadUsername(this);
        if (tvTitle != null) {
            tvTitle.setText("Allenamenti di " + username);
        }

        exerciseList = DataManager.loadExercises(this);
        adapter = new ExerciseAdapter(this, exerciseList);
        lvExercises.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(com.example.workoutapp.service.TimerService.AZIONE_TIMER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(timerReceiver, filter, Context.RECEIVER_EXPORTED);
        } else {
            registerReceiver(timerReceiver, filter);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(timerReceiver);
    }
}