package com.buinvent.jurassic_gains;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class DayActivity extends AppCompatActivity {

    public static final String EXTRA_WEEK = "com.buinvent.jurassic_gains.WEEK";
    public static final String DAY_PREFERENCES = "DAY_PREFERENCES";

    SharedPreferences dayPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        Intent weekNumIntent = getIntent();
        String weekExtra = weekNumIntent.getStringExtra(EXTRA_WEEK);
        TextView topText = findViewById(R.id.weekText);
        topText.setText(weekExtra);
        dayPreferences = getSharedPreferences(DAY_PREFERENCES, Context.MODE_PRIVATE);

        CheckBox[] workoutDayBox = {findViewById(R.id.checkbox_day1),
                                    findViewById(R.id.checkbox_day2),
                                    findViewById(R.id.checkbox_day4),
                                    findViewById(R.id.checkbox_day5)};

        Button[] workoutDayButton = {findViewById(R.id.button_day1),
                                     findViewById(R.id.button_day2),
                                     findViewById(R.id.button_day4),
                                     findViewById(R.id.button_day5)};

        for(int i = 0; i < workoutDayBox.length; i++){

            final String workoutDay = "checkbox_workout_day" + Integer.toString(i+1);
            final int iNum = i;

            if(!dayPreferences.contains(weekExtra + workoutDay)){
                SharedPreferences.Editor editor = dayPreferences.edit();
                editor.putBoolean(weekExtra + workoutDay, false);
                editor.commit();
            }
            workoutDayBox[i].setChecked(dayPreferences.getBoolean(weekExtra + workoutDay, false));

            workoutDayBox[i].setOnClickListener((View v) -> {
                SharedPreferences.Editor editor = dayPreferences.edit();
                editor.putBoolean(weekExtra + workoutDay, workoutDayBox[iNum].isChecked());
                editor.apply();
            });

            workoutDayButton[i].setOnClickListener(view -> setWorkout(workoutDayButton[iNum].getText().toString()));
        }
    }

    private void setWorkout(final String day){
        Intent workout = new Intent(getApplicationContext(), WorkoutActivity.class);
        workout.putExtra(WorkoutActivity.EXTRA_DAY, day);
        startActivity(workout);
    }
}
