package com.buinvent.jurassic_gains

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class CustomDayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_day)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val customDayIntent = intent
        val dayNum = customDayIntent.getIntExtra("DAY_NUM", 1)
        setTitle("Customize Day " + dayNum)
    }

    // This was needed for back button
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}
