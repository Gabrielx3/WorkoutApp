package com.example.workoutapp.activity;

import android.content.Intent;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.workoutapp.R;
import com.example.workoutapp.utils.DataManager;

public class MainActivity extends AppCompatActivity {

    // --- COMPONENTI UI (Iniziali + Nuovi aggiunti da Persona 2) ---
    private Button btnAddExercise, btnStartWorkout, btnShare, btnGuide, btnSettings;
    private ListView lvExercises;
    private TextView tvTitle;
    private TextView tvTimerCountdown; // Gestito nella UI da Persona 2, aggiornato dal receiver di Persona 3

    // --- VARIABILI DATI (Gestite da Persona 2) ---
    private ExerciseAdapter adapter;
    private java.util.List<com.example.workoutapp.model.Exercise> exerciseList;
    private int selectedPosition = -1;

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

        // --- INIZIALIZZAZIONE COMPONENTI UI (Persona 2) ---
        tvTitle = findViewById(R.id.tvTitle);
        btnAddExercise = findViewById(R.id.btnAddExercise);
        btnStartWorkout = findViewById(R.id.btnStartWorkout);
        btnShare = findViewById(R.id.btnShare);
        btnGuide = findViewById(R.id.btnGuide);
        btnSettings = findViewById(R.id.btnSettings); // Nuovo bottone per Terza Activity
        lvExercises = findViewById(R.id.lvExercises);
        tvTimerCountdown = findViewById(R.id.tvTimerCountdown); // Nuova TextView per Service

        // Navigazione esplicita verso la schermata di inserimento (Activity 2)
        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddExerciseActivity.class);
                startActivity(intent);
            }
        });

        // Selezione dell'esercizio dalla lista (Persona 2)
        lvExercises.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            Toast.makeText(MainActivity.this, "Selezionato: " + exerciseList.get(position).getNome(), Toast.LENGTH_SHORT).show();
        });

        // REQUISITO 3.2 - AVVIO FOREGROUND SERVICE CON DATI (Persona 2)
        btnStartWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == -1 || exerciseList.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Seleziona un esercizio dalla lista!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Recupera il tempo di recupero dell'esercizio selezionato
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

        // REQUISITO 3.4 - INTENT IMPLICITO 1: Condivisione Testuale (Persona 2)
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Mi sto allenando con la mia Workout App!");
                startActivity(Intent.createChooser(shareIntent, "Condividi il tuo allenamento"));
            }
        });

        // REQUISITO 3.4 - INTENT IMPLICITO 2: Browser Web Esterno (Persona 2)
        btnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse("https://www.projectinvictus.it/esercizi-palestra/");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent);
            }
        });

        // REQUISITO 3.1 - NAVIGAZIONE VERSO TERZA ACTIVITY CON EXTRA (Persona 2)
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                // Passaggio dati nativo extra (Nome Utente caricato tramite DataManager di Persona 1)
                intent.putExtra("attuale_nome", DataManager.loadUsername(MainActivity.this));
                startActivity(intent);
            }
        });

        // [Nota per il team: qui Persona 3 inserirà la chiamata per creare il canale di notifica e i permessi runtime]
    }

    // --- AGGIORNAMENTO DINAMICO UI (Condiviso/Persona 2) ---
    @Override
    protected void onResume() {
        super.onResume();
        // Aggiorna il titolo personalizzato con il nome salvato nelle SharedPreferences (Persona 1)
        String username = DataManager.loadUsername(this);
        if (tvTitle != null) {
            tvTitle.setText("Allenamenti di " + username);
        }

        // Ricarica la lista dal file JSON dell'Internal Storage (Persona 1)
        exerciseList = DataManager.loadExercises(this);
        adapter = new ExerciseAdapter(this, exerciseList);
        lvExercises.setAdapter(adapter);
    }

    // --- LOGICA DI SISTEMA PER IL TIMER (Mantenuta dall'originale, di competenza di Persona 3) ---
    private final android.content.BroadcastReceiver timerReceiver = new android.content.BroadcastReceiver() {
        @Override
        public void onReceive(android.content.Context context, Intent intent) {
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

    // [Nota per il team: qui sotto Persona 3 implementerà onStart() e onStop() per registrare/disattivare il timerReceiver]
}