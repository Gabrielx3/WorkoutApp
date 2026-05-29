package com.example.workoutapp.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.workoutapp.R;
import com.example.workoutapp.utils.DataManager;

public class SettingsActivity extends AppCompatActivity {
    private EditText etUsername;
    private Button btnSaveSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etUsername = findViewById(R.id.etUsername);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);


        String nomeAttuale = getIntent().getStringExtra("attuale_nome");
        if (nomeAttuale != null) {
            etUsername.setText(nomeAttuale);
        }

        btnSaveSettings.setOnClickListener(v -> {
            String nuovoNome = etUsername.getText().toString().trim();
            if (!nuovoNome.isEmpty()) {
                DataManager.saveUsername(SettingsActivity.this, nuevoNome);
                Toast.makeText(SettingsActivity.this, "Profilo aggiornato!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}

