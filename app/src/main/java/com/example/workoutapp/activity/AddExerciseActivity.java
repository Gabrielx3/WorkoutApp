package com.example.workoutapp.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.workoutapp.R;
import com.example.workoutapp.model.Exercise;
import com.example.workoutapp.utils.DataManager;
import java.util.List;

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

        btnSave.setOnClickListener(v -> saveExercise());
    }

    private void saveExercise() {
        String name = etName.getText().toString().trim();
        String setsStr = etSets.getText().toString().trim();
        String repsStr = etReps.getText().toString().trim();
        String recoveryStr = etRecovery.getText().toString().trim();

        if (name.isEmpty() || setsStr.isEmpty() || repsStr.isEmpty() || recoveryStr.isEmpty()) {
            Toast.makeText(this, "Per favore compila tutti i campi", Toast.LENGTH_SHORT).show();
            return;
        }

        int sets = Integer.parseInt(setsStr);
        int reps = Integer.parseInt(repsStr);
        int recovery = Integer.parseInt(recoveryStr);


        List<Exercise> list = DataManager.loadExercises(this);
        list.add(new Exercise(name, sets, reps, recovery));
        DataManager.saveExercises(this, list);

        Toast.makeText(this, "Esercizio salvato: " + name, Toast.LENGTH_SHORT).show();
        finish();
    }
}
