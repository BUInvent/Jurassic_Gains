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


// Activity for the screen that gives users the ability to select the week they're training
public class WeekActivity extends AppCompatActivity {

    public static final int WEEKS_NUM = 12;  // Number of weeks for training
    public static final String WEEK_PREFERENCES = "WEEK_PREFERENCES";
    SharedPreferences weekPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        LinearLayout mLinearLayout = findViewById(R.id.week_layout);
        weekPreferences = getSharedPreferences(WEEK_PREFERENCES, Context.MODE_PRIVATE);

        LayoutParams weekLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
        LayoutParams checkBoxLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
        LayoutParams buttonLayoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

        // Declare and Initialize checkbox and button variables to be size of the number of weeks
        CheckBox[] checkBoxes = new CheckBox[WEEKS_NUM];
        Button[] buttons = new Button[WEEKS_NUM];

        for (int i = 0; i < WEEKS_NUM; i++) {

            LinearLayout weekLayout = new LinearLayout(this);
            weekLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Add checkbox for each week
            checkBoxes[i] = new CheckBox(this);
            checkBoxes[i].setGravity(Gravity.CENTER);
            checkBoxes[i].setScaleX((float)1.3);
            checkBoxes[i].setScaleY((float)1.3);
            weekLayout.addView(checkBoxes[i], checkBoxLayoutParams);

            // Set the checkbox to be checked or unchecked based on what was previously saved
            checkBoxes[i].setChecked(weekPreferences.getBoolean("checkbox_week" + Integer.toString(i+1), false));

            // Add a button and set the text for each week
            buttons[i] = new Button(this);
            buttons[i].setTextSize(30);
            buttons[i].setGravity(Gravity.CENTER);
            String weekText = "Week " + String.valueOf(i + 1);
            buttons[i].setText(weekText);
            // Set a click listener that will start the Day activity and send the week's text
            buttons[i].setOnClickListener(view -> setDay(weekText));
            weekLayout.addView(buttons[i],buttonLayoutParams);

            // Do a checkbox listener if i > 1. This is because you need three checkboxes to run
            // this function (one before, one middle, and one after)
            if(i > 1) {
                checkBoxListener(i, checkBoxes[i-1], checkBoxes[i-2], checkBoxes[i], buttons[i]);
            }

            // If the previous checkbox wasn't checked, dis the checkbox and buttons
            if (i != 0 && !checkBoxes[i-1].isChecked()) {
                checkBoxes[i].setEnabled(false);
                buttons[i].setEnabled(false);
            }

            // Disable the previous checkbox if the checkbox is checked
            if (i != 0 && checkBoxes[i].isChecked())
                checkBoxes[i-1].setEnabled(false);

            mLinearLayout.addView(weekLayout, weekLayoutParams);
        }

        // Add checkbox listener for the first week's checkbox
        checkBoxListener(checkBoxes[0], checkBoxes[1], buttons[1]);
        // Add a checkbox listener for the last week's checkbox
        checkBoxListener(checkBoxes[WEEKS_NUM-1], checkBoxes[WEEKS_NUM-2]);

    }


//    This is for all weeks that are not first or last
    private void checkBoxListener(final int weekNum, final CheckBox currentCheckBox,
                                  final CheckBox previousCheckBox, final CheckBox nextCheckBox,
                                  final Button nextButton){

        currentCheckBox.setOnClickListener((View v) -> {
            // Enable the next checkbox and button if the current checkbox gets checked, disable if vice versa
            // Disable the last checkbox if current checkbox gets checked, disable if vice versa
            nextCheckBox.setEnabled(currentCheckBox.isChecked());
            nextButton.setEnabled(currentCheckBox.isChecked());
            previousCheckBox.setEnabled(!currentCheckBox.isChecked());

            // Save the state of the current checkbox
            SharedPreferences.Editor editor = weekPreferences.edit();
            editor.putBoolean("checkbox_week" + Integer.toString(weekNum), currentCheckBox.isChecked());
            editor.apply();
        });
    }

//    This is for the first week since it doesn't have a previous week
    private void checkBoxListener(final CheckBox currentCheckBox,
                                  final CheckBox nextCheckBox, final Button nextButton){

        currentCheckBox.setOnClickListener((View v) -> {
            nextCheckBox.setEnabled(currentCheckBox.isChecked());
            nextButton.setEnabled(currentCheckBox.isChecked());

            SharedPreferences.Editor editor = weekPreferences.edit();
            editor.putBoolean("checkbox_week1", currentCheckBox.isChecked());
            editor.apply();
        });
    }

//    For the last week since it doesn't have a next week
    private void checkBoxListener(final CheckBox currentCheckBox, final CheckBox previousCheckBox){

        currentCheckBox.setOnClickListener((View v) -> {
            previousCheckBox.setEnabled(!currentCheckBox.isChecked());

            SharedPreferences.Editor editor = weekPreferences.edit();
            editor.putBoolean("checkbox_week" + Integer.toString(WEEKS_NUM), currentCheckBox.isChecked());
            editor.apply();
        });
    }

    // Function to start the Day Activity
    private void setDay(final String week){
        Intent day = new Intent(getApplicationContext(), DayActivity.class);
        day.putExtra(DayActivity.EXTRA_WEEK, week); // Send the week so the day activity knows what week was selected
        startActivity(day);
    }

}
