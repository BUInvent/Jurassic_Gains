package com.example.jason.jurassic_gains;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DayActivity extends AppCompatActivity {

    public static final String EXTRA_WEEK = "com.example.jason.jurassic_gains.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
    }
}
