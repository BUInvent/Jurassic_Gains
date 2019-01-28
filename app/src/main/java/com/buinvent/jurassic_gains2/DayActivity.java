package com.buinvent.jurassic_gains2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class DayActivity extends AppCompatActivity {

    public static final String EXTRA_WEEK = "com.buinvent.jurassic_gains.WEEK";
    public static final String EXTRA_GAINER = "com.buinvent.jurassic_gains.GAINER";
    public static final String DAY_PREFERENCES = "DAY_PREFERENCES";
    SharedPreferences dayPreferences;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static FirebaseUser previousUser = null;
    private static ArrayList FIRST_TIME = new ArrayList();
    Gainer gainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent weekIntent = getIntent();
        int weekNum = weekIntent.getIntExtra(EXTRA_WEEK, 1); // grab the week that was selected from the week activity
        gainer = weekIntent.getParcelableExtra(EXTRA_GAINER);
        Boolean[] dayChecks = gainer.getDayChecks(weekNum);

        TextView topText = findViewById(R.id.weekText);
        topText.setText("WEEK " + weekNum); // set the title of the screen to be the week that was selected

        dayPreferences = getSharedPreferences(DAY_PREFERENCES, Context.MODE_PRIVATE);

        if (previousUser != user) {
            previousUser = user;
            FIRST_TIME.clear();
        }

        if (!FIRST_TIME.contains(weekNum)) {
            FIRST_TIME.add(weekNum);
            SharedPreferences.Editor editor = dayPreferences.edit();
            for (int i = 0; i < 7; i++) {
                editor.putBoolean("WEEK " + weekNum + "checkbox_workout_day" + (i + 1), dayChecks[i]);
            }
            editor.apply();
        }

        // Declare and initialize a checkbox for the workout days (1, 2, 4, 5)
        CheckBox[] workoutDayBox = {findViewById(R.id.checkbox_day1),
                findViewById(R.id.checkbox_day2),
                findViewById(R.id.checkbox_day3),
                findViewById(R.id.checkbox_day4),
                findViewById(R.id.checkbox_day5),
                findViewById(R.id.checkbox_day6),
                findViewById(R.id.checkbox_day7)};

        // Declare and initialize a button for the workout days
        Button[] workoutDayButton = {findViewById(R.id.button_day1),
                findViewById(R.id.button_day2),
                findViewById(R.id.button_day3),
                findViewById(R.id.button_day4),
                findViewById(R.id.button_day5),
                findViewById(R.id.button_day6),
                findViewById(R.id.button_day7)};

        for (int i = 0; i < workoutDayBox.length; i++) {

            final String workoutDay = "checkbox_workout_day" + (i + 1);
            final int iNum = i;

            // Set the checkbox checked based on what was saved
            workoutDayBox[i].setChecked(dayPreferences.getBoolean("WEEK " + weekNum + workoutDay, false));

            // disable workout day buttons and boxes if there are no exercises on those days
            workoutDayButton[i].setEnabled( gainer.getExercises(weekNum, (i + 1)).size() > 0);
            workoutDayBox[i].setEnabled( gainer.getExercises(weekNum, (i + 1)).size() > 0);

            // Set a clicklistener for the check box that saves the state of the check box
            workoutDayBox[i].setOnClickListener((View v) -> {

                db.collection("users").document(user.getUid())
                        .update("weeks.WEEK " + (weekNum) + ".days.DAY " + (iNum + 1) + ".checked", workoutDayBox[iNum].isChecked());

                SharedPreferences.Editor editor = dayPreferences.edit();
                editor.putBoolean("WEEK " + weekNum + workoutDay, workoutDayBox[iNum].isChecked());
                editor.apply();
            });

            // Set a click listener for the workout day button
            workoutDayButton[i].setOnClickListener(view -> setWorkout(iNum + 1, weekNum));
        }
    }

    // Function that starts the workout activity and sends the day and week to it
    private void setWorkout(final int dayNum, final int weekNum) {
        Intent workout = new Intent(getApplicationContext(), WorkoutActivity.class);
        workout.putExtra(WorkoutActivity.EXTRA_DAY, dayNum);
        workout.putExtra(EXTRA_WEEK, weekNum);
        workout.putExtra(DayActivity.EXTRA_GAINER, gainer);
        startActivity(workout);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
