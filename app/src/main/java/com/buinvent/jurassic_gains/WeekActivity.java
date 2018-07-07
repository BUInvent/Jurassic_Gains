package com.buinvent.jurassic_gains;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.content.SharedPreferences;
import android.content.Context;
import android.widget.ListView;


// Activity for the screen that gives users the ability to select the week they're training
public class WeekActivity extends AppCompatActivity {

    public static final int WEEKS_NUM = 12;  // Number of weeks for training
    public static final String WEEK_PREFERENCES = "WEEK_PREFERENCES";
    SharedPreferences weekPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        weekPreferences = getSharedPreferences(WEEK_PREFERENCES, Context.MODE_PRIVATE);

        String[] weekArray = {"WEEK 1", "WEEK 2", "WEEK 3", "WEEK 4", "WEEK 5"};
        ArrayAdapter weekBTNAdapter = new ArrayAdapter<>(this, R.layout.activity_week_listview, weekArray);

        ListView mListView = findViewById(R.id.week_layout);
        mListView.setAdapter(weekBTNAdapter);

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
