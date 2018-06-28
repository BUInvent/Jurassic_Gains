package com.buinvent.jurassic_gains;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DayActivity extends AppCompatActivity {

    public static final String EXTRA_WEEK = "com.buinvent.jurassic_gains.WEEK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        Intent weekNumIntent = getIntent();

        TextView topText = findViewById(R.id.weekText);
        topText.setText(weekNumIntent.getStringExtra(EXTRA_WEEK));

    }
}
