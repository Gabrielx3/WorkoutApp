package com.example.workoutapp.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.workoutapp.R;
import com.example.workoutapp.model.Exercise;
import java.util.List;

public class ExerciseAdapter extends ArrayAdapter<Exercise> {
    public ExerciseAdapter(Context context, List<Exercise> exercises) {
        super(context, 0, exercises);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_exercise, parent, false);
        }
        Exercise exercise = getItem(position);

        TextView tvName = convertView.findViewById(R.id.tvItemName);
        TextView tvDetails = convertView.findViewById(R.id.tvItemDetails);

        if (exercise != null) {
            tvName.setText(exercise.getNome());
            tvDetails.setText(exercise.getSerie() + "x" + exercise.getRipetizioni() + " - Recupero: " + exercise.getSecondiRecupero() + "s");
        }
        return convertView;
    }
}
