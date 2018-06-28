package com.buinvent.jurassic_gains;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.content.SharedPreferences;
import android.content.Context;


public class WeekActivity extends AppCompatActivity {

    LinearLayout mLinearLayout;
    LayoutParams weekLayoutParams;
    LayoutParams checkBoxLayoutParams;
    LayoutParams buttonLayoutParams;

    public static final int WEEKS_NUM = 12;
    public static final String WEEK_PREFERENCES = "WEEK_PREFERENCES";

    SharedPreferences weekPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);
        mLinearLayout = findViewById(R.id.week_layout);
        weekPreferences = getSharedPreferences(WEEK_PREFERENCES, Context.MODE_PRIVATE);

        weekLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
        checkBoxLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
        buttonLayoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

        CheckBox[] checkBoxes = new CheckBox[WEEKS_NUM];
        Button[] buttons = new Button[WEEKS_NUM];

        for (int i = 0; i < WEEKS_NUM; i++) {

            LinearLayout weekLayout = new LinearLayout(getApplicationContext());
            weekLayout.setOrientation(LinearLayout.HORIZONTAL);

            checkBoxes[i] = new CheckBox(getApplicationContext());
            checkBoxes[i].setGravity(Gravity.CENTER);
            checkBoxes[i].setScaleX((float)1.3);
            checkBoxes[i].setScaleY((float)1.3);
            weekLayout.addView(checkBoxes[i], checkBoxLayoutParams);

            if(!weekPreferences.contains("checkbox_week" + Integer.toString(i+1))){
                SharedPreferences.Editor editor = weekPreferences.edit();
                editor.putBoolean("checkbox_week" + Integer.toString(i+1), false);
                editor.commit();
            }

            checkBoxes[i].setChecked(weekPreferences.getBoolean("checkbox_week" + Integer.toString(i+1), false));

            buttons[i] = new Button(getApplicationContext());
            buttons[i].setTextSize(30);
            buttons[i].setGravity(Gravity.CENTER);
            buttons[i].setText("Week " + String.valueOf(i + 1));
            weekLayout.addView(buttons[i],buttonLayoutParams);

            if(i > 1) {
                checkBoxListener(i, checkBoxes[i-1], checkBoxes[i-2], checkBoxes[i], buttons[i]);
            }

            if (i != 0 && !checkBoxes[i-1].isChecked()) {
                checkBoxes[i].setEnabled(false);
                buttons[i].setEnabled(false);
            }

            if (i != 0 && checkBoxes[i].isChecked())
                checkBoxes[i-1].setEnabled(false);

            mLinearLayout.addView(weekLayout, weekLayoutParams);
        }

        checkBoxListener(checkBoxes[0], checkBoxes[1], buttons[1]);
        checkBoxListener(checkBoxes[WEEKS_NUM-1], checkBoxes[WEEKS_NUM-2]);

    }


//    This is for all weeks that are not first or last
    private void checkBoxListener(final int weekNum, final CheckBox currentCheckBox,
                                  final CheckBox previousCheckBox, final CheckBox nextCheckBox,
                                  final Button nextButton){

        currentCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nextCheckBox.setEnabled(currentCheckBox.isChecked());
                nextButton.setEnabled(currentCheckBox.isChecked());
                previousCheckBox.setEnabled(!currentCheckBox.isChecked());

                SharedPreferences.Editor editor = weekPreferences.edit();
                editor.putBoolean("checkbox_week" + Integer.toString(weekNum), currentCheckBox.isChecked());
                editor.apply();
            }
        });
    }

//    This is for the first week since it doesn't have a previous week
    private void checkBoxListener(final CheckBox currentCheckBox,
                                  final CheckBox nextCheckBox, final Button nextButton){

        currentCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nextCheckBox.setEnabled(currentCheckBox.isChecked());
                nextButton.setEnabled(currentCheckBox.isChecked());

                SharedPreferences.Editor editor = weekPreferences.edit();
                editor.putBoolean("checkbox_week1", currentCheckBox.isChecked());
                editor.apply();
            }
        });
    }

//    For the last week since it doesn't have a next week
    private void checkBoxListener(final CheckBox currentCheckBox, final CheckBox previousCheckBox){

        currentCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                previousCheckBox.setEnabled(!currentCheckBox.isChecked());

                SharedPreferences.Editor editor = weekPreferences.edit();
                editor.putBoolean("checkbox_week" + Integer.toString(WEEKS_NUM), currentCheckBox.isChecked());
                editor.apply();
            }
        });
    }

    private void setDay(final String week){
        Intent day = new Intent(getApplicationContext(), DayActivity.class);
        day.putExtra(DayActivity.EXTRA_WEEK, week);
        startActivity(day);
    }

}
