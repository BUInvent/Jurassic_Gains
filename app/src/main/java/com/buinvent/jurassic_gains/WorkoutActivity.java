package com.buinvent.jurassic_gains;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class WorkoutActivity extends AppCompatActivity {

    LinearLayout mLinearLayout;
    LinearLayout workoutLayout;
    LayoutParams linearLayoutParams = new LayoutParams(-1, -2, 1);
    LayoutParams exerciseTextParams = new LayoutParams(-2, -1);
    LayoutParams allWeightTextParams = new LayoutParams(-2, -1);
    LayoutParams repNumParams = new LayoutParams(0, -1, 1);
    LayoutParams exercisePrevParams = new LayoutParams(0, -1, 4);
    LayoutParams exerciseStatParams = new LayoutParams(0, -1, 3);

    public static final String EXTRA_DAY = "com.buinvent.jurassic_gains.DAY";
    public static final String WORKOUT_PREFERENCES = "WORKOUT_PREFERENCES";
    public static final int SET_NUM = 3;

    String[] exercises;
    EditText[][] lbsInput;
    SharedPreferences workoutPreferences;
    String previousWeight;
    String previousReps;

    Intent dayNumIntent;
    String weekExtra;
    String dayExtra;
    TextView topText;
    TextView exerciseText;
    EditText allWeightText;
    TextView lbsText;
    LinearLayout statLabelLayout;
    TextView hash;
    TextView previous;
    TextView lbs;
    TextView reps;
    LinearLayout statLayout;
    TextView setNum;
    TextView previousText;
    EditText repsInput;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        mLinearLayout = findViewById(R.id.workout_layout);
        workoutPreferences = getSharedPreferences(WORKOUT_PREFERENCES, Context.MODE_PRIVATE);
        dayNumIntent = getIntent();
        weekExtra = dayNumIntent.getStringExtra(DayActivity.EXTRA_WEEK);
        dayExtra = dayNumIntent.getStringExtra(EXTRA_DAY);
        topText = findViewById(R.id.dayText);
        editor = workoutPreferences.edit();

        topText.setText(dayExtra);
        allWeightTextParams.setMargins(100, 0, 0,0);

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

        lbsInput = new EditText[exercises.length][SET_NUM];

        for(int i = 0; i < exercises.length; i++){

            final int iNum = i;
            workoutLayout = new LinearLayout(getApplicationContext());
            workoutLayout.setOrientation(LinearLayout.HORIZONTAL);

            exerciseText = new TextView(getApplicationContext());
            exerciseText.setText(exercises[i]);
            exerciseText.setTextSize(20);
            exerciseText.setTypeface(Typeface.DEFAULT_BOLD);
            exerciseText.setGravity(Gravity.CENTER);
            workoutLayout.addView(exerciseText, exerciseTextParams);

            allWeightText = new EditText(getApplicationContext());
            allWeightText.setInputType(InputType.TYPE_CLASS_NUMBER);
            allWeightText.setWidth(100);
            allWeightText.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(3)});
            allWeightText.setTextSize(14);
            allWeightText.setGravity(Gravity.CENTER);
            workoutLayout.addView(allWeightText, allWeightTextParams);

            lbsText = new TextView(getApplicationContext());
            lbsText.setText(getResources().getString(R.string.pounds));
            lbsText.setTextSize(16);
            lbsText.setGravity(Gravity.CENTER);
            workoutLayout.addView(lbsText, exerciseTextParams);

            mLinearLayout.addView(workoutLayout, linearLayoutParams);

            statLabelLayout = new LinearLayout(getApplicationContext());
            statLabelLayout.setOrientation(LinearLayout.HORIZONTAL);

            hash = new TextView(getApplicationContext());
            hash.setText(getResources().getString(R.string.hash));
            hash.setTextSize(20);
            hash.setGravity(Gravity.CENTER);
            statLabelLayout.addView(hash, repNumParams);

            previous = new TextView(getApplicationContext());
            previous.setText(getResources().getString(R.string.previous));
            previous.setGravity(Gravity.CENTER);
            previous.setTextSize(16);
            statLabelLayout.addView(previous, exercisePrevParams);

            lbs = new TextView(getApplicationContext());
            lbs.setText(getResources().getString(R.string.pounds));
            lbs.setGravity(Gravity.CENTER);
            lbs.setTextSize(16);
            statLabelLayout.addView(lbs, exerciseStatParams);

            reps = new TextView(getApplicationContext());
            reps.setText(getResources().getString(R.string.reps));
            reps.setGravity(Gravity.CENTER);
            reps.setTextSize(16);
            statLabelLayout.addView(reps, exerciseStatParams);

            mLinearLayout.addView(statLabelLayout, linearLayoutParams);


            for(int j=0; j<SET_NUM; j++){
                final int jNum = j;
                statLayout = new LinearLayout(getApplicationContext());
                statLayout.setOrientation(LinearLayout.HORIZONTAL);

                setNum = new TextView(getApplicationContext());
                setNum.setText(Integer.toString(j+1));
                setNum.setTextSize(16);
                setNum.setGravity(Gravity.CENTER);
                statLayout.addView(setNum, repNumParams);

                previousText = new TextView(getApplicationContext());
                previousText.setGravity(Gravity.CENTER);
                previousText.setTextSize(16);
                statLayout.addView(previousText, exercisePrevParams);

                previousWeight = workoutPreferences.getString((Integer.valueOf(weekExtra) - 1) + exercises[iNum] + "weight" + (jNum+1), "None");
                previousReps = workoutPreferences.getString((Integer.valueOf(weekExtra) - 1) + exercises[iNum] + "reps" + (jNum+1), "8");
                if(previousWeight.equals("None"))
                    previousText.setText("None");
                else
                    previousText.setText(previousWeight + " x " + previousReps);

                lbsInput[i][j] = new EditText(getApplicationContext());
                lbsInput[i][j].setInputType(InputType.TYPE_CLASS_NUMBER);
                lbsInput[i][j].setGravity(Gravity.CENTER);
                lbsInput[i][j].setTextSize(16);
                lbsInput[i][j].setFilters(new InputFilter[]{ new InputFilter.LengthFilter(3)});
                lbsInput[i][j].setText(workoutPreferences.getString(weekExtra + exercises[iNum] + "weight" + (jNum+1), ""));
                statLayout.addView(lbsInput[i][j], exerciseStatParams);

                repsInput = new EditText(getApplicationContext());
                repsInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                repsInput.setGravity(Gravity.CENTER);
                repsInput.setTextSize(16);
                repsInput.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(2)});
                repsInput.setText(workoutPreferences.getString(weekExtra + exercises[iNum] + "reps" + (jNum+1), "8"));
                statLayout.addView(repsInput, exerciseStatParams);

                mLinearLayout.addView(statLayout, linearLayoutParams);

                repsInput.addTextChangedListener(new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void afterTextChanged(Editable s) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        editor.putString(weekExtra + exercises[iNum] + "reps" + (jNum+1), s.toString());
                        editor.apply();
                    }
                });

                lbsInput[i][j].addTextChangedListener(new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void afterTextChanged(Editable s) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        SharedPreferences.Editor editor = workoutPreferences.edit();
                        editor.putString(weekExtra + exercises[iNum] + "weight" + (jNum+1), s.toString());
                        editor.apply();
                    }
                });

            }

            allWeightText.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void afterTextChanged(Editable s) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    for(int j=0; j<SET_NUM; j++)
                        lbsInput[iNum][j].setText(s);
                }
            });

        }

    }
}
