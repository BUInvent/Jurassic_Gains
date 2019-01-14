package com.buinvent.jurassic_gains

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.*
import com.travijuu.numberpicker.library.NumberPicker
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

        exerciseName = findViewById(R.id.exerciseText)
        val repsPicker = findViewById<NumberPicker>(R.id.reps_picker)
        val setsPicker = findViewById<NumberPicker>(R.id.sets_picker)
        addExerciseButton = findViewById(R.id.addExercisebtn)

        exerciseName.addTextChangedListener(exerciseWatcher)
        addExerciseButton.setEnabled(false)

        val customDayPreferences = getSharedPreferences("CUSTOM_DAY_PREFERENCES", Context.MODE_PRIVATE)
        val editor = customDayPreferences.edit()
        val gson = Gson()

        var exercisesMap = LinkedHashMap<String, Any>()
        if (customDayPreferences.contains("DAY" + dayNum + "exercises")) {
            val savedExercises = customDayPreferences.getString("DAY" + dayNum + "exercises", "")
            exercisesMap = gson.fromJson(savedExercises, object : TypeToken<LinkedHashMap<String, Any>>() {}.type)
        }

        // Add all saved exercises to layout
        println("exercises map = " + exercisesMap)
        for (exerciseKey in exercisesMap.keys) {
            addExerciseLayout(exerciseKey, exercisesMap)
        }

        addExerciseButton.setOnClickListener {

            if (exercisesMap.containsKey(exerciseName.text.toString()))
                Toast.makeText(this@CustomDayActivity, "Exercise already made", Toast.LENGTH_SHORT).show()
            else {
                addExerciseLayout(exerciseName.text.toString(), exercisesMap)

                val exerciseMap = mapOf("sets" to setsPicker.value, "reps" to repsPicker.value)
                val dayPrefName = "DAY" + dayNum + "exercises"

                if (!customDayPreferences.contains(dayPrefName))
                    editor.putString(dayPrefName, gson.toJson(mapOf(exerciseName.text.toString() to exerciseMap))).apply()

                exercisesMap.put(exerciseName.text.toString(), exerciseMap)
                editor.putString(dayPrefName, gson.toJson(exercisesMap)).apply()
                exerciseName.text.clear()
            }
        }
    }

    fun addExerciseLayout(exerciseName: String, exercisesMap: LinkedHashMap<String, Any>) {

        val customDayPreferences = getSharedPreferences("CUSTOM_DAY_PREFERENCES", Context.MODE_PRIVATE)
        val editor = customDayPreferences.edit()
        val gson = Gson()

        val mLinearLayout = findViewById<LinearLayout>(R.id.custom_day_layout)
        val exerciseLayout = LinearLayout(this)
        val xButton = ImageButton(this)

        val customDayIntent = intent
        val dayNum = customDayIntent.getIntExtra("DAY_NUM", 1)

        xButton.setImageResource(R.drawable.delete)

        val xButtonParams = LinearLayout.LayoutParams(100, 100)
        xButtonParams.setMargins(25, 9, 12, 0)
        xButton.setLayoutParams(xButtonParams)
        xButton.setBackgroundResource(0)
        xButton.setPadding(5, 5, 5, 5)

        xButton.setScaleType(ImageView.ScaleType.FIT_CENTER)
        xButton.setOnClickListener {
            mLinearLayout.removeView(exerciseLayout) // remove exercise from screen
            // Remove the exercise from sharedpreferences
            exercisesMap.remove(exerciseName)
            editor.putString("DAY" + dayNum + "exercises", gson.toJson(exercisesMap)).apply()
        }

        val exerciseNameView = TextView(this)
        exerciseNameView.gravity = Gravity.CENTER
        exerciseNameView.text = exerciseName
        exerciseNameView.textSize = 28F
        exerciseLayout.addView(xButton)
        exerciseLayout.addView(exerciseNameView)

        mLinearLayout.addView(exerciseLayout)
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
