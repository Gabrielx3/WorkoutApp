package com.example.workoutapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddExerciseActivity extends AppCompatActivity {

    private EditText etName, etSets, etReps, etRecovery;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        etName = findViewById(R.id.etName);
        etSets = findViewById(R.id.etSets);
        etReps = findViewById(R.id.etReps);
        etRecovery = findViewById(R.id.etRecovery);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExercise();
            }
        });
    }

    private void saveExercise() {
        String name = etName.getText().toString().trim();
        String sets = etSets.getText().toString().trim();
        String reps = etReps.getText().toString().trim();
        String recovery = etRecovery.getText().toString().trim();

        if (name.isEmpty() || sets.isEmpty() || reps.isEmpty() || recovery.isEmpty()) {
            Toast.makeText(this, "Per favore compila tutti i campi", Toast.LENGTH_SHORT).show();
            return;
        }


        Toast.makeText(this, "Esercizio salvato: " + name, Toast.LENGTH_SHORT).show();
        finish();
    }
}