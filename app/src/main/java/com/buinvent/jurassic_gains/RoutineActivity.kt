package com.buinvent.jurassic_gains

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RoutineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine)

        val customDayButtons = arrayOf(
                findViewById<Button>(R.id.day1btn),
                findViewById(R.id.day2btn),
                findViewById(R.id.day3btn),
                findViewById(R.id.day4btn),
                findViewById(R.id.day5btn),
                findViewById(R.id.day6btn),
                findViewById(R.id.day7btn))

        for (i in customDayButtons.indices)
            customDayButtons[i].setOnClickListener({ _ -> setCustomDay(i + 1) })
    }

    private fun setCustomDay(dayNum: Int) {
        val customDay = Intent(applicationContext, CustomDayActivity::class.java)
        customDay.putExtra("DAY_NUM", dayNum)
        startActivity(customDay)
    }

}
