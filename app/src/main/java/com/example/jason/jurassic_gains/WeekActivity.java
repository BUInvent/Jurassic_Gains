package com.example.jason.jurassic_gains;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import org.json.JSONArray;
import org.json.JSONObject;


public class WeekActivity extends AppCompatActivity {

    private LinearLayout mLinearLayout;
    private LayoutParams mButtonLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);
        mLinearLayout = findViewById(R.id.week_layout);
        mButtonLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);


        for (int i = 1; i < 13; i++) {

            // Create a button for each week.
            Button button = new Button(getApplicationContext());
            button.setTextSize(30);
            button.setGravity(Gravity.START);
            button.setText("Week " + String.valueOf(i));
            // button.setOnClickListener(view -> setMatchup(awayTeam, homeTeam, mLeague, teamAbbreviation));

            // Add the button to the layout.
            mLinearLayout.addView(button, mButtonLayoutParams);
        }

    }

}
