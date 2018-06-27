package com.example.jason.jurassic_gains;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;


public class WeekActivity extends AppCompatActivity {

    LinearLayout mLinearLayout;
    LayoutParams weekLayoutParams;
    LayoutParams checkBoxLayoutParams;
    LayoutParams buttonLayoutParams;
    int weeksNum = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);
        mLinearLayout = findViewById(R.id.week_layout);

        weekLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
        checkBoxLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
        buttonLayoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);


        for (int i = 1; i <= weeksNum; i++) {

            LinearLayout weekLayout = new LinearLayout(getApplicationContext());
            weekLayout.setOrientation(LinearLayout.HORIZONTAL);

            CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setGravity(Gravity.CENTER);
            checkBox.setScaleX((float)1.3);
            checkBox.setScaleY((float)1.3);
            weekLayout.addView(checkBox, checkBoxLayoutParams);

            Button button = new Button(getApplicationContext());
            button.setTextSize(30);
            button.setGravity(Gravity.CENTER);
            button.setText("Week " + String.valueOf(i));
            weekLayout.addView(button, buttonLayoutParams);

            mLinearLayout.addView(weekLayout, weekLayoutParams);
        }

    }

}
