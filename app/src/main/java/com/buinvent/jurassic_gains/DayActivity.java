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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent weekNumIntent = getIntent();
        String weekExtra = weekNumIntent.getStringExtra(EXTRA_WEEK); // grab the week that was selected from the week activity

        TextView topText = findViewById(R.id.weekText);
        topText.setText(weekExtra); // set the title of the screen to be the week that was selected

        dayPreferences = getSharedPreferences(DAY_PREFERENCES, Context.MODE_PRIVATE);

        // Declare and initialize a checkbox for the workout days (1, 2, 4, 5)
        CheckBox[] workoutDayBox = {findViewById(R.id.checkbox_day1),
                                    findViewById(R.id.checkbox_day2),
                                    findViewById(R.id.checkbox_day4),
                                    findViewById(R.id.checkbox_day5)};

        // Declare and initialize a button for the workout days
        Button[] workoutDayButton = {findViewById(R.id.button_day1),
                                     findViewById(R.id.button_day2),
                                     findViewById(R.id.button_day4),
                                     findViewById(R.id.button_day5)};

        for(int i = 0; i < workoutDayBox.length; i++){

            final String workoutDay = "checkbox_workout_day" + Integer.toString(i+1);
            final int iNum = i;

            // Set the checkbox checked based on what was saved
            workoutDayBox[i].setChecked(dayPreferences.getBoolean(weekExtra + workoutDay, false));

            // Set a clicklistener for the check box that saves the state of the check box
            workoutDayBox[i].setOnClickListener((View v) -> {
                SharedPreferences.Editor editor = dayPreferences.edit();
                editor.putBoolean(weekExtra + workoutDay, workoutDayBox[iNum].isChecked());
                editor.apply();
            });

            // Set a click listener for the workout day button
            workoutDayButton[i].setOnClickListener(view -> setWorkout(workoutDayButton[iNum].getText().toString(), weekExtra));
        }
    }

    // Function that starts the workout activity and sends the day and week to it
    private void setWorkout(final String day, final String week){
        Intent workout = new Intent(getApplicationContext(), WorkoutActivity.class);
        workout.putExtra(WorkoutActivity.EXTRA_DAY, day);
        workout.putExtra(EXTRA_WEEK, week.substring(week.lastIndexOf(' ') + 1));
        startActivity(workout);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
