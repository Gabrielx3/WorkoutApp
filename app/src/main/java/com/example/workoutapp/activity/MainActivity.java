package com.example.workoutapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.workoutapp.R;

public class MainActivity extends AppCompatActivity {

    private Button btnAddExercise, btnStartWorkout, btnShare, btnGuide;
    private ListView lvExercises;

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

        // Inizializzazione dei componenti UI
        btnAddExercise = findViewById(R.id.btnAddExercise);
        btnStartWorkout = findViewById(R.id.btnStartWorkout);
        btnShare = findViewById(R.id.btnShare);
        btnGuide = findViewById(R.id.btnGuide);
        lvExercises = findViewById(R.id.lvExercises);

        // Click per aggiungere un esercizio
        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddExerciseActivity.class);
                startActivity(intent);
            }
        });

        // Azione per iniziare l'allenamento
        btnStartWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Allenamento iniziato!", Toast.LENGTH_SHORT).show();
            }
        });

        // Azione per condividere
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Funzione di condivisione in arrivo", Toast.LENGTH_SHORT).show();
            }
        });

        // Azione per la guida
        btnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Guida agli esercizi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}