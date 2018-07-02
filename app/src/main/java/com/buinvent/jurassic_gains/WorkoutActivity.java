package com.buinvent.jurassic_gains;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

public class WorkoutActivity extends AppCompatActivity {

    public static final String EXTRA_DAY = "com.buinvent.jurassic_gains.DAY";
    public static final String WORKOUT_PREFERENCES = "WORKOUT_PREFERENCES";
    public static final int SET_NUM = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        Intent dayNumIntent = getIntent();
        String weekExtra = dayNumIntent.getStringExtra(DayActivity.EXTRA_WEEK);
        String dayExtra = dayNumIntent.getStringExtra(EXTRA_DAY);

        LayoutParams linearLayoutParams = new LayoutParams(-1, -2, 1);
        LayoutParams exerciseTextParams = new LayoutParams(-2, -1);
        LayoutParams allWeightTextParams = new LayoutParams(-2, -1);
        LayoutParams repNumParams = new LayoutParams(0, -1, 1);
        LayoutParams exercisePrevParams = new LayoutParams(0, -1, 4);
        LayoutParams exerciseStatParams = new LayoutParams(0, -1, 3);
//        LayoutParams spaceLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);

        TextView topText = findViewById(R.id.dayText);
        topText.setText(dayExtra);
        allWeightTextParams.setMargins(100, 0, 0,0);

        String[] exercises;
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
        else{ exercises = new String[0]; }

        EditText[][] lbsInput = new EditText[exercises.length][SET_NUM];
        LinearLayout mLinearLayout = findViewById(R.id.workout_layout);

        for(int i = 0; i < exercises.length; i++){

            final int iNum = i;
            LinearLayout subLinearLayout = new LinearLayout(getApplicationContext());
            subLinearLayout.setGravity(Gravity.CENTER);
            subLinearLayout.setOrientation(LinearLayout.VERTICAL);
            subLinearLayout.setBackgroundColor(Color.DKGRAY);

            LinearLayout workoutLayout = new LinearLayout(getApplicationContext());
            workoutLayout.setOrientation(LinearLayout.HORIZONTAL);
            workoutLayout.addView(new Space(getApplicationContext()), 50, LayoutParams.MATCH_PARENT);

            TextView exerciseText = defaultTextView(exercises[i], 20);
            exerciseText.setTypeface(Typeface.DEFAULT_BOLD);
            workoutLayout.addView(exerciseText, exerciseTextParams);

            EditText allWeightText = defaultEditText("", 14, 3);
            allWeightText.setWidth(100);
            workoutLayout.addView(allWeightText, allWeightTextParams);

            workoutLayout.addView(defaultTextView(getResources().getString(R.string.pounds), 16), exerciseTextParams);

            subLinearLayout.addView(workoutLayout, linearLayoutParams);

            LinearLayout statLabelLayout = new LinearLayout(getApplicationContext());
            statLabelLayout.setOrientation(LinearLayout.HORIZONTAL);
            statLabelLayout.addView(new Space(getApplicationContext()), 30, LayoutParams.MATCH_PARENT);

            statLabelLayout.addView(defaultTextView(getResources().getString(R.string.hash), 16), repNumParams);
            statLabelLayout.addView(defaultTextView(getResources().getString(R.string.previous), 16), exercisePrevParams);
            statLabelLayout.addView(defaultTextView(getResources().getString(R.string.pounds), 16), exerciseStatParams);
            statLabelLayout.addView(defaultTextView(getResources().getString(R.string.reps), 16), exerciseStatParams);
            statLabelLayout.addView(new Space(getApplicationContext()), 30, LayoutParams.MATCH_PARENT);

            subLinearLayout.addView(statLabelLayout, linearLayoutParams);


            for(int j=0; j<SET_NUM; j++){

                final int jNum = j;
                mLinearLayout.addView(new Space(getApplicationContext()), LayoutParams.MATCH_PARENT, 5);
                LinearLayout statLayout = new LinearLayout(getApplicationContext());
                statLayout.setOrientation(LinearLayout.HORIZONTAL);
                statLayout.addView(new Space(getApplicationContext()), 30, LayoutParams.MATCH_PARENT);

                statLayout.addView(defaultTextView(Integer.toString(j+1), 16), repNumParams);

                TextView previousText = defaultTextView("", 16);
                statLayout.addView(previousText, exercisePrevParams);

                SharedPreferences workoutPreferences = getSharedPreferences(WORKOUT_PREFERENCES, Context.MODE_PRIVATE);
                String previousWeight = workoutPreferences.getString((Integer.valueOf(weekExtra) - 1) + exercises[iNum] + "weight" + (jNum+1), "None");
                String previousReps = workoutPreferences.getString((Integer.valueOf(weekExtra) - 1) + exercises[iNum] + "reps" + (jNum+1), "8");
                if(previousWeight.equals("None"))
                    previousText.setText("None");
                else
                    previousText.setText(previousWeight + " x " + previousReps);

                String temp = workoutPreferences.getString(weekExtra + exercises[iNum] + "weight" + (jNum+1), "");
                lbsInput[i][j] = defaultEditText(temp, 16, 3);
                statLayout.addView(lbsInput[i][j], exerciseStatParams);

                temp = workoutPreferences.getString(weekExtra + exercises[iNum] + "reps" + (jNum+1), "8");
                EditText repsInput = defaultEditText(temp, 16, 2);
                statLayout.addView(repsInput, exerciseStatParams);
                statLayout.addView(new Space(getApplicationContext()), 30, LayoutParams.MATCH_PARENT);

                subLinearLayout.addView(statLayout, linearLayoutParams);

                SharedPreferences.Editor editor = workoutPreferences.edit();
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
                        editor.putString(weekExtra + exercises[iNum] + "weight" + (jNum+1), s.toString());
                        editor.apply();
                    }
                });

            }

            mLinearLayout.addView(subLinearLayout, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

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

    private TextView defaultTextView (String text, int textSize){
        TextView textView = new TextView(getApplicationContext());
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(textSize);
        return textView;
    }

    private EditText defaultEditText (String text, int textSize, int maxLength){
        EditText editText = new EditText(getApplicationContext());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setGravity(Gravity.CENTER);
        editText.setTextSize(textSize);
        editText.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(maxLength)});
        editText.setText(text);
        return editText;
    }

}
