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
import android.widget.Space;
import android.widget.TextView;

import com.google.common.primitives.Ints;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.toIntExact;

public class WorkoutActivity extends AppCompatActivity {

    public static final String EXTRA_DAY = "com.buinvent.jurassic_gains.DAY";
    public static final String WORKOUT_PREFERENCES = "WORKOUT_PREFERENCES";
    public static final int SET_NUM = 3;
    SharedPreferences workoutPreferences;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static FirebaseUser previousUser = null;
    private static ArrayList FIRST_TIME = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this is to enable back button

        // Get the day that was selected
        Intent dayIntent = getIntent();
        int weekNumExtra = dayIntent.getIntExtra(DayActivity.EXTRA_WEEK, 1);
        int dayNumExtra = dayIntent.getIntExtra(EXTRA_DAY, 1);
        Gainer gainer = dayIntent.getParcelableExtra(DayActivity.EXTRA_GAINER);

        if (previousUser != user) {
            previousUser = user;
            FIRST_TIME.clear();
        }

        ArrayList exercises = gainer.getExercises(weekNumExtra, dayNumExtra);
        workoutPreferences = getSharedPreferences(WORKOUT_PREFERENCES, Context.MODE_PRIVATE);

        if (!FIRST_TIME.contains("week " + weekNumExtra + "day " + dayNumExtra)) {
            FIRST_TIME.add("week " + weekNumExtra + "day " + dayNumExtra);
            SharedPreferences.Editor editor = workoutPreferences.edit();

            for (int i = 0; i < exercises.size(); i++) {
                String exercise = (String) exercises.get(i);
                HashMap<String, HashMap<String, Integer>> exerciseSets = gainer.getExerciseSets(weekNumExtra, dayNumExtra, exercise);

                for (int j = 1; j <= exerciseSets.size(); j++) {
                    editor.putString("WEEK " + weekNumExtra + "DAY " + dayNumExtra + "Exercise " + exercise + "weight" + j, String.valueOf(exerciseSets.get("set " + j).get("weight")));
                    editor.putString("WEEK " + weekNumExtra + "DAY " + dayNumExtra + "Exercise " + exercise + "reps" + j, String.valueOf(exerciseSets.get("set " + j).get("reps")));
                }
            }
            editor.apply();
        }

        // Set layout parameters
        LayoutParams linearLayoutParams = new LayoutParams(-1, -2, 1);
        LayoutParams exerciseTextParams = new LayoutParams(-2, -1);
        LayoutParams allWeightTextParams = new LayoutParams(-2, -1);
        LayoutParams repNumParams = new LayoutParams(0, -1, 1);
        LayoutParams exercisePrevParams = new LayoutParams(0, -1, 4);
        LayoutParams exerciseStatParams = new LayoutParams(0, -1, 3);

        // Set the text at the top to the day that was selected
        TextView topText = findViewById(R.id.dayText);
        topText.setText("DAY " + dayNumExtra);

        allWeightTextParams.setMargins(50, 0, 0, 0);

        // Set the exercises based on the day that the user selected
//        String[] exercises;
//        if(dayNumExtra.equals(getResources().getString(R.string.day1))){
//            exercises = new String[]{"Bench Press", "Skull Crushers", "Flyes", "Incline Bench Press"};
//        }
//        else if(dayNumExtra.equals(getResources().getString(R.string.day2))){
//            exercises = new String[]{"Chin-Ups", "Bicep Curls", "Bent Over Rows", "Lat Pulldown"};
//        }
//        else if(dayNumExtra.equals(getResources().getString(R.string.day4))){
//            exercises = new String[]{"Squats", "Leg Extensions", "Leg Curls", "Calf Raises"};
//        }
//        else if(dayNumExtra.equals(getResources().getString(R.string.day5))){
//            exercises = new String[]{"Shoulder Press", "Side Lateral Raise", "Upright Rows", "Seated Bent Over Flys"};
//        }
//        else{ exercises = new String[0]; }

//        EditText[][] lbsInput;
        HashMap<String, HashMap<String, EditText>> lbsInput = new HashMap<>();
        LinearLayout mLinearLayout = findViewById(R.id.workout_layout);

        // For every exercise
        for (int i = 0; i < exercises.size(); i++) {

            // Create a sub layout for every exercise
            LinearLayout subLinearLayout = new LinearLayout(this);
            subLinearLayout.setGravity(Gravity.CENTER);
            subLinearLayout.setOrientation(LinearLayout.VERTICAL);
            subLinearLayout.setBackgroundColor(Color.parseColor("#595959"));

            // workout layout has the name of the exercise and all weight text
            LinearLayout workoutLayout = new LinearLayout(this);
            workoutLayout.setOrientation(LinearLayout.HORIZONTAL);
            workoutLayout.addView(new Space(this), 40, LayoutParams.MATCH_PARENT);

            String exercise = (String) exercises.get(i);
            TextView exerciseText = defaultTextView(exercise, 18);
            exerciseText.setTypeface(Typeface.DEFAULT_BOLD);
            workoutLayout.addView(exerciseText, exerciseTextParams);

            // All weight text gives users the ability to put in a weight for every set
            EditText allWeightText = defaultEditText("", 14, 3);
            allWeightText.setWidth(100);
            workoutLayout.addView(allWeightText, allWeightTextParams);

            workoutLayout.addView(defaultTextView(getResources().getString(R.string.pounds), 16), exerciseTextParams);
            subLinearLayout.addView(workoutLayout, linearLayoutParams);

            // Stat label layout is: "#    PREVIOUS    LBS    REPS"
            LinearLayout statLabelLayout = new LinearLayout(this);
            statLabelLayout.setOrientation(LinearLayout.HORIZONTAL);
            statLabelLayout.addView(new Space(this), 30, LayoutParams.MATCH_PARENT);

            statLabelLayout.addView(defaultTextView(getResources().getString(R.string.hash), 16), repNumParams);
            statLabelLayout.addView(defaultTextView(getResources().getString(R.string.previous), 16), exercisePrevParams);
            statLabelLayout.addView(defaultTextView(getResources().getString(R.string.pounds), 16), exerciseStatParams);
            statLabelLayout.addView(defaultTextView(getResources().getString(R.string.reps), 16), exerciseStatParams);
            statLabelLayout.addView(new Space(this), 30, LayoutParams.MATCH_PARENT);

            subLinearLayout.addView(statLabelLayout, linearLayoutParams);

            HashMap<String, HashMap<String, Integer>> exerciseSets = gainer.getExerciseSets(weekNumExtra, dayNumExtra, (String) exercises.get(i));

            // For every set
            for (int j = 1; j <= exerciseSets.size(); j++) {

                final int jNum = j;
                // Stat layout is the actual set #, the previous, and user input for lbs and reps
                mLinearLayout.addView(new Space(this), LayoutParams.MATCH_PARENT, 5);
                LinearLayout statLayout = new LinearLayout(this);
                statLayout.setOrientation(LinearLayout.HORIZONTAL);
                statLayout.addView(new Space(this), 30, LayoutParams.MATCH_PARENT);

                // Add the set number
                statLayout.addView(defaultTextView(Integer.toString(j), 16), repNumParams);

                TextView previousText = defaultTextView("", 16);
                statLayout.addView(previousText, exercisePrevParams);

                // Grab and set the previous weight and reps
                previousText.setText("None");
//                SharedPreferences workoutPreferences = getSharedPreferences(WORKOUT_PREFERENCES, Context.MODE_PRIVATE);
//                String previousWeight = workoutPreferences.getString((Integer.valueOf(weekNumExtra) - 1) + exercises[iNum] + "weight" + (jNum+1), "None");
//                String previousReps = workoutPreferences.getString((Integer.valueOf(weekNumExtra) - 1) + exercises[iNum] + "reps" + (jNum+1), "8");
//                if(previousWeight.equals("None"))
//                    previousText.setText("None");
//                else
//                    previousText.setText(previousWeight + " x " + previousReps);

//                 Grab and set the current weeks saved weight
//                editor.putInt("WEEK " + weekNumExtra + "DAY " + dayNumExtra + "Exercise " + exercise + "weight" + j, exerciseSets.get("set " + j).get("weight"));
//                editor.putInt("WEEK " + weekNumExtra + "DAY " + dayNumExtra + "Exercise " + exercise + "reps" + j, exerciseSets.get("set " + j).get("reps"));
//                String temp = workoutPreferences.getString(weekNumExtra + exercises[iNum] + "weight" + (jNum+1), "");
                String temp = workoutPreferences.getString("WEEK " + weekNumExtra + "DAY " + dayNumExtra + "Exercise " + exercise + "weight" + j, "");
//                lbsInput[i][j] = defaultEditText(temp, 16, 3);
//                statLayout.addView(lbsInput[i][j], exerciseStatParams);

                HashMap<String, EditText> innerMap = lbsInput.get(exercise);
                if (innerMap == null) {
                    lbsInput.put(exercise, innerMap = new HashMap<>());
                }
                innerMap.put("set " + j, defaultEditText(String.valueOf(temp), 16, 3));
                statLayout.addView(lbsInput.get(exercise).get("set " + j));

                // Grab and set the current weeks saved reps
                temp = workoutPreferences.getString("WEEK " + weekNumExtra + "DAY " + dayNumExtra + "Exercise " + exercise + "reps" + j, "");
//                temp = workoutPreferences.getString(weekNumExtra + exercises[iNum] + "reps" + (jNum + 1), "8");
                EditText repsInput = defaultEditText(String.valueOf(temp), 16, 2);
                statLayout.addView(repsInput, exerciseStatParams);
                statLayout.addView(new Space(this), 30, LayoutParams.MATCH_PARENT);

                subLinearLayout.addView(statLayout, linearLayoutParams);

                // Save the reps if text changes
                SharedPreferences.Editor editor = workoutPreferences.edit();
                repsInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.toString().equals("")) s = "0";
                        db.collection("users").document(user.getUid())
                                .update("weeks.WEEK " + (weekNumExtra) + ".days.DAY " + (dayNumExtra) + ".exercises." + exercise + ".sets.set " + (jNum) + ".reps", Integer.parseInt(s.toString()));
                        editor.putString("WEEK " + weekNumExtra + "DAY " + dayNumExtra + "Exercise " + exercise + "reps" + jNum, s.toString());
                        editor.apply();
                    }
                });

                // Save the weight if text changes
                lbsInput.get(exercise).get("set " + j).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.toString().equals("")) s = "0";
                        db.collection("users").document(user.getUid())
                                .update("weeks.WEEK " + (weekNumExtra) + ".days.DAY " + (dayNumExtra) + ".exercises." + exercise + ".sets.set " + (jNum) + ".weight", Integer.parseInt(s.toString()));
                        editor.putString("WEEK " + weekNumExtra + "DAY " + dayNumExtra + "Exercise " + exercise + "weight" + jNum, s.toString());
                        editor.apply();
                    }
                });

            }

            mLinearLayout.addView(subLinearLayout, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

            // All Weight Text will set the weight for all weight boxes if user changes
            allWeightText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    for (int j = 0; j < exerciseSets.size(); j++)
//                        lbsInput[iNum][j].setText(s);
                        lbsInput.get(exercise).get("set " + j).setText(s);
                }
            });

        }

    }

    // Function that will return a TextView that has the parameters needed in many places
    private TextView defaultTextView(String text, int textSize) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(textSize);
        textView.setTextColor(Color.WHITE);
        return textView;
    }

    // Function that will return an EditText that has the parameters needed in many places
    private EditText defaultEditText(String text, int textSize, int maxLength) {
        EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setGravity(Gravity.CENTER);
        editText.setTextSize(textSize);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        editText.setText(text);
        return editText;
    }

    // This was needed for back button
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
