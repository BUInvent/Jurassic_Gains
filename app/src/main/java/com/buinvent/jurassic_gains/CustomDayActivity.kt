package com.buinvent.jurassic_gains

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.*
import com.travijuu.numberpicker.library.NumberPicker
import android.widget.LinearLayout.LayoutParams
import android.text.Editable
import android.text.TextWatcher
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CustomDayActivity : AppCompatActivity() {

    lateinit var exerciseName: EditText
    lateinit var addExerciseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_day)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val customDayIntent = intent
        val dayNum = customDayIntent.getIntExtra("DAY_NUM", 1)
        setTitle("Customize Day " + dayNum)

        val mLinearLayout = findViewById<LinearLayout>(R.id.custom_day_layout)

        exerciseName = findViewById<EditText>(R.id.exerciseText)
        val repsPicker = findViewById<NumberPicker>(R.id.reps_picker)
        val setsPicker = findViewById<NumberPicker>(R.id.sets_picker)
        addExerciseButton = findViewById<Button>(R.id.addExercisebtn)

        exerciseName.addTextChangedListener(exerciseWatcher)
        addExerciseButton.setEnabled(false)

        val customDayPreferences = getSharedPreferences("CUSTOM_DAY_PREFERENCES", Context.MODE_PRIVATE)
        val editor = customDayPreferences.edit()
        val gson = Gson()

        addExerciseButton.setOnClickListener {
            val exerciseLayout = LinearLayout(this)
            val xButton = ImageButton(this)
            xButton.setImageResource(R.drawable.delete)
            xButton.setLayoutParams(LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
            xButton.setScaleType(ImageView.ScaleType.FIT_CENTER)
            xButton.setOnClickListener { mLinearLayout.removeView(exerciseLayout) }

            val exerciseNameView = TextView(this)
            exerciseNameView.gravity = Gravity.CENTER
            exerciseNameView.text = exerciseName.text
            exerciseNameView.textSize = 28F
            exerciseLayout.addView(xButton)
            exerciseLayout.addView(exerciseNameView)

            mLinearLayout.addView(exerciseLayout)

            val exerciseMap = mapOf("exercise" to exerciseNameView.text.toString(), "sets" to setsPicker.value, "reps" to repsPicker.value)
            val dayPrefName = "DAY" + dayNum + "exercises"

            if(!customDayPreferences.contains(dayPrefName))
                editor.putString(dayPrefName, gson.toJson(listOf(exerciseMap))).apply()

            else {
                val savedExercises = customDayPreferences.getString(dayPrefName, "")
                val exerciseList: ArrayList<Map<String, Any>> = gson.fromJson(savedExercises, object : TypeToken<List<Map<String, Any>>>() {}.type)
                exerciseList.add(exerciseMap)
                editor.putString(dayPrefName, gson.toJson(exerciseList)).apply()
            }
        }
    }

    val exerciseWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val exerciseInput = exerciseName.getText().toString().trim()
            addExerciseButton.setEnabled(!exerciseInput.isEmpty())
        }

        override fun afterTextChanged(s: Editable) {}
    }

    // This was needed for back button
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}
