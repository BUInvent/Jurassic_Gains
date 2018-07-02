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

    public static final String EXTRA_DAY = "com.buinvent.jurassic_gains.DAY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        Intent dayNumIntent = getIntent();
        String dayExtra = dayNumIntent.getStringExtra(EXTRA_DAY);

        LayoutParams linearLayoutParams = new LayoutParams(-1, -2, 1);
        LayoutParams exercisePrevParams = new LayoutParams(0, -1, 4);

        TextView topText = findViewById(R.id.dayText);
        topText.setText(dayExtra);

        LinearLayout mLinearLayout = findViewById(R.id.workout_layout);

        LinearLayout statLabelLayout = new LinearLayout(getApplicationContext());
        statLabelLayout.setOrientation(LinearLayout.HORIZONTAL);

        statLabelLayout.addView(defaultTextView(getResources().getString(R.string.previous), 16), exercisePrevParams);
        mLinearLayout.addView(statLabelLayout, linearLayoutParams);

    }

    private TextView defaultTextView (String text, int textSize){
        TextView textView = new TextView(getApplicationContext());
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(textSize);
        return textView;
    }

}
