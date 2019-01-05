package com.buinvent.jurassic_gains

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.buinvent.jurassic_gains.WorkoutActivity.WORKOUT_PREFERENCES
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.travijuu.numberpicker.library.NumberPicker
import kotlinx.android.synthetic.main.activity_routine.*
import java.util.HashMap

class RoutineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

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

        val weeksPicker = findViewById<NumberPicker>(R.id.weeks_picker)

        val user = FirebaseAuth.getInstance().currentUser
        val createRoutineButton = findViewById<Button>(R.id.routinebtn)
        createRoutineButton.setOnClickListener({ _ -> createBlankDoc(user!!.uid) })


    }

    private fun createBlankDoc(userID: String) {

        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(userID)

        val weeksNum = weeks_picker.value

        val customDayPreferences = getSharedPreferences("CUSTOM_DAY_PREFERENCES", Context.MODE_PRIVATE)
        val gson = Gson()

        val daysMap = HashMap<String, HashMap<*, *>>()

        for (i in 1..7) {

            val savedExercises = customDayPreferences.getString("DAY" + i + "exercises", "")
            var savedExercisesMap = HashMap<String, Any>()
            if (savedExercises != "") savedExercisesMap = gson.fromJson(savedExercises, object : TypeToken<HashMap<String, Any>>() {}.type)
            val exercisesMap = HashMap<String, HashMap<*, *>>()

            for (exerciseKey in savedExercisesMap.keys) {
                val exerciseMap = HashMap<String, Any>()

                val setMap = HashMap<String, Int>()
                setMap["weight"] = 0
                setMap["reps"] = ((savedExercisesMap[exerciseKey] as Map<*, *>)["reps"] as Double).toInt()

                val setsMap = HashMap<String, HashMap<*, *>>()
                for (setNum in 1..((savedExercisesMap[exerciseKey] as Map<*, *>)["sets"] as Double).toInt()){
                    setsMap["set $setNum"] = setMap
                }

                exerciseMap["sets"] = setsMap
                exerciseMap["name"] = exerciseKey
                exercisesMap["exercise ${savedExercisesMap.keys.indexOf(exerciseKey) + 1}"] = exerciseMap
            }

            val dayMap = HashMap<String, Any>()
            dayMap["checked"] = false
            dayMap["exercises"] = exercisesMap

            daysMap["DAY " + i] = dayMap
        }

        val weekMap = HashMap<String, Any>()
        weekMap["checked"] = false
        weekMap["days"] = daysMap

        val weeksMap = HashMap<String, HashMap<*, *>>()
        for (i in 0 until weeksNum) {
            weeksMap["WEEK " + (i + 1)] = weekMap
        }

        val docMap = HashMap<String, Any>()
        docMap["weeks"] = weeksMap

        // Add a new document with the user's ID
        db.collection("users")
                .document(userID)
                .set(docMap)
                .addOnSuccessListener(OnSuccessListener<Void> {
                    Log.d("Success", "DocumentSnapshot successfully written!")
                    userRef.get().addOnSuccessListener(OnSuccessListener<DocumentSnapshot> { innerDocumentSnapshot ->
                        val weekActivity = Intent(applicationContext, WeekActivity::class.java)
                        getSharedPreferences(WORKOUT_PREFERENCES, Context.MODE_PRIVATE).edit().clear().apply()
                        startActivity(weekActivity)
                    })
                })
                .addOnFailureListener(OnFailureListener { e -> Log.w("Failure", "Error writing document", e) })
            }

            private fun setCustomDay(dayNum: Int) {
                val customDay = Intent(applicationContext, CustomDayActivity::class.java)
                customDay.putExtra("DAY_NUM", dayNum)
                startActivity(customDay)
            }

            // This was needed for back button
            override fun onSupportNavigateUp(): Boolean {
                finish()
                return true
            }

        }
