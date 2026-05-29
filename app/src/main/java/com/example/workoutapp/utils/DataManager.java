package com.example.workoutapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.workoutapp.model.Exercise;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final String FILE_NAME = "exercises.json";
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_USERNAME = "username";

    // --- INTERNAL STORAGE (Salvataggio File JSON) ---
    public static void saveExercises(Context context, List<Exercise> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Exercise> loadExercises(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) return new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = context.openFileInput(FILE_NAME);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Exercise>>() {}.getType();
        return gson.fromJson(sb.toString(), type);
    }

    // --- SHAREDPREFERENCES (Preferenze Utente NATIVE) ---
    public static void saveUsername(Context context, String username) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_USERNAME, username).apply();
    }

    public static String loadUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USERNAME, "Atleta");
    }
}
