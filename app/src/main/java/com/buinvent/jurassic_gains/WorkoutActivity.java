package com.buinvent.jurassic_gains;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class WorkoutActivity extends AppCompatActivity {

    LinearLayout mLinearLayout;
    LayoutParams linearLayoutParams;
    LayoutParams exerciseTextParams;
    LayoutParams allWeightTextParams;
    LayoutParams repNumParams;
    LayoutParams exerciseStatParams;
    LayoutParams exercisePrevParams;

    public static final String EXTRA_DAY = "com.buinvent.jurassic_gains.DAY";
    String[] exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        linearLayoutParams = new LayoutParams(-1, -2, 1);
        exerciseTextParams = new LayoutParams(-2, -1);
        allWeightTextParams = new LayoutParams(-2, -1);
        allWeightTextParams.setMargins(100, 0, 0,0);
        repNumParams = new LayoutParams(0, -1, 1);
        exercisePrevParams = new LayoutParams(0, -1, 4);
        exerciseStatParams = new LayoutParams(0, -1, 3);


        Intent dayNumIntent = getIntent();
        String dayExtra = dayNumIntent.getStringExtra(EXTRA_DAY);
        TextView topText = findViewById(R.id.dayText);
        topText.setText(dayExtra);

        mLinearLayout = findViewById(R.id.workout_layout);

        if(dayExtra.equals(getResources().getString(R.string.day1))){
            exercises = new String[]{"Bench Press", "Skull Crushers", "Flyes", "Incline Bench Press"};
        }
        else if(dayExtra.equals(getResources().getString(R.string.day2))){
            exercises = new String[]{"Chin-Ups", "Bicep Curls", "Bent Over Rows", "Lat Pulldown"};
        }
        else if(dayExtra.equals(getResources().getString(R.string.day4))){
            exercises = new String[]{"Squats", "Leg Extensions", "Leg Curls", "Calf Raises"};
        }
        else if(dayExtra.equals(getResources().getString(R.string.day5))){
            exercises = new String[]{"Shoulder Press", "Side Lateral Raise", "upright rows", "Seated Bent Over Flys"};
        }

        for(int i = 0; i < exercises.length; i++){

            LinearLayout workoutLayout = new LinearLayout(getApplicationContext());
            workoutLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView exerciseText = new TextView(getApplicationContext());
            exerciseText.setText(exercises[i]);
            exerciseText.setTextSize(20);
            exerciseText.setTypeface(Typeface.DEFAULT_BOLD);
            exerciseText.setGravity(Gravity.CENTER);
            workoutLayout.addView(exerciseText, exerciseTextParams);

            EditText allWeightText = new EditText(getApplicationContext());
            allWeightText.setWidth(100);
            allWeightText.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(3)});
            allWeightText.setTextSize(14);
            allWeightText.setGravity(Gravity.CENTER);
            workoutLayout.addView(allWeightText, allWeightTextParams);

            TextView lbsText = new TextView(getApplicationContext());
            lbsText.setText(getResources().getString(R.string.pounds));
            lbsText.setTextSize(16);
            lbsText.setGravity(Gravity.CENTER);
            workoutLayout.addView(lbsText, exerciseTextParams);

            mLinearLayout.addView(workoutLayout, linearLayoutParams);


            LinearLayout statLabelLayout = new LinearLayout(getApplicationContext());
            statLabelLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView hash = new TextView(getApplicationContext());
            hash.setText(getResources().getString(R.string.hash));
            hash.setTextSize(20);
            hash.setGravity(Gravity.CENTER);
            statLabelLayout.addView(hash, repNumParams);

            TextView previous = new TextView(getApplicationContext());
            previous.setText(getResources().getString(R.string.previous));
            previous.setGravity(Gravity.CENTER);
            previous.setTextSize(16);
            statLabelLayout.addView(previous, exercisePrevParams);

            TextView lbs = new TextView(getApplicationContext());
            lbs.setText(getResources().getString(R.string.pounds));
            lbs.setGravity(Gravity.CENTER);
            lbs.setTextSize(16);
            statLabelLayout.addView(lbs, exerciseStatParams);

            TextView reps = new TextView(getApplicationContext());
            reps.setText(getResources().getString(R.string.reps));
            reps.setGravity(Gravity.CENTER);
            reps.setTextSize(16);
            statLabelLayout.addView(reps, exerciseStatParams);

            mLinearLayout.addView(statLabelLayout, linearLayoutParams);


            for(int j=1; j<=3; j++){
                LinearLayout statLayout = new LinearLayout(getApplicationContext());
                statLayout.setOrientation(LinearLayout.HORIZONTAL);

                TextView setNum = new TextView(getApplicationContext());
                setNum.setText(Integer.toString(j));
                setNum.setTextSize(16);
                setNum.setGravity(Gravity.CENTER);
                statLayout.addView(setNum, repNumParams);

                TextView previousInput = new TextView(getApplicationContext());
                previousInput.setGravity(Gravity.CENTER);
                previousInput.setTextSize(16);
                statLayout.addView(previousInput, exercisePrevParams);

                EditText lbsInput = new EditText(getApplicationContext());
                lbsInput.setGravity(Gravity.CENTER);
                lbsInput.setTextSize(16);
                lbsInput.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(3)});
                statLayout.addView(lbsInput, exerciseStatParams);

                EditText repsInput = new EditText(getApplicationContext());
                repsInput.setGravity(Gravity.CENTER);
                repsInput.setTextSize(16);
                repsInput.setText("8");
                repsInput.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(2)});
                statLayout.addView(repsInput, exerciseStatParams);

                mLinearLayout.addView(statLayout, linearLayoutParams);

            }

        }

    }
}
