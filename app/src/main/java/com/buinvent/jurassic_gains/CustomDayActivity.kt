package com.buinvent.jurassic_gains

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import com.travijuu.numberpicker.library.NumberPicker
import android.widget.LinearLayout.LayoutParams
import kotlinx.android.synthetic.main.activity_custom_day.*
import kotlinx.android.synthetic.main.activity_routine.*

class CustomDayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_day)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val customDayIntent = intent
        val dayNum = customDayIntent.getIntExtra("DAY_NUM", 1)
        setTitle("Customize Day " + dayNum)

        val exerciseName = findViewById<EditText>(R.id.exerciseText)
        val repsPicker = findViewById<NumberPicker>(R.id.reps_picker)
        val setsPicker = findViewById<NumberPicker>(R.id.sets_picker)
        val addExerciseButton = findViewById<Button>(R.id.addExercisebtn)


        val mLinearLayout = findViewById<LinearLayout>(R.id.custom_day_layout)
        val exerciseLayout = LinearLayout(this)

        exerciseLayout.orientation = LinearLayout.HORIZONTAL

        val xButton = Button(this)
        val exerciseNameView = TextView(this)

        exerciseNameView.gravity = Gravity.CENTER
        exerciseNameView.text = "BENCH PRESS"
        exerciseNameView.textSize = 20f
        exerciseLayout.addView(xButton)
        exerciseLayout.addView(exerciseNameView)

        mLinearLayout.addView(exerciseLayout)

    }

    // This was needed for back button
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}
