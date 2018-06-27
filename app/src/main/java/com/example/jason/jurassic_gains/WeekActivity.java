package com.example.jason.jurassic_gains;

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

        for (int i = 1; i <= WEEKS_NUM; i++) {

            LinearLayout weekLayout = new LinearLayout(getApplicationContext());
            weekLayout.setOrientation(LinearLayout.HORIZONTAL);

            final CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setGravity(Gravity.CENTER);
            checkBox.setScaleX((float)1.3);
            checkBox.setScaleY((float)1.3);
            weekLayout.addView(checkBox, checkBoxLayoutParams);

            if(!weekPreferences.contains("checkbox_week" + Integer.toString(i))){
                SharedPreferences.Editor editor = weekPreferences.edit();
                editor.putBoolean("checkbox_week" + Integer.toString(i), false);
                editor.commit();
            }

            checkBox.setChecked(weekPreferences.getBoolean("checkbox_week" + Integer.toString(i), false));

            final int weekNum = i;
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = weekPreferences.edit();
                    editor.putBoolean("checkbox_week" + Integer.toString(weekNum), checkBox.isChecked());
                    editor.apply();
                }
            });

            Button button = new Button(getApplicationContext());
            button.setTextSize(30);
            button.setGravity(Gravity.CENTER);
            button.setText("Week " + String.valueOf(i));
            weekLayout.addView(button, buttonLayoutParams);

            mLinearLayout.addView(weekLayout, weekLayoutParams);
        }

    }

    private void checkBoxListener(String week){

    }

}
