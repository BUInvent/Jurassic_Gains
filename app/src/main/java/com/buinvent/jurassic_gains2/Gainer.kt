package com.buinvent.jurassic_gains2

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import kotlin.collections.HashMap

@Parcelize
class Gainer(val weeks: @RawValue HashMap<String, Any>?) : Parcelable {

    constructor() : this(null)

    fun getWeekChecks(): Array<Boolean?> {

        if (weeks == null) return arrayOfNulls(0)
        val weekChecks = arrayOfNulls<Boolean>(weeks.size)

        for (i in 0 until weeks.size) {
            weekChecks[i] = (weeks["WEEK " + (i + 1)] as HashMap<*, *>) ["checked"] as Boolean
        }
        return weekChecks
    }

    fun getDayChecks(weekNum: Int): Array<Boolean?> {

        if (weeks == null) return arrayOfNulls(0)

        val dayChecks = arrayOfNulls<Boolean>(7)
        val singleWeek = weeks["WEEK " + weekNum] as HashMap<*, *>
        val days = singleWeek["days"] as HashMap<*, *>

        for (i in 1..7) {
            val day = days["DAY " + i] as HashMap<*, *>
            dayChecks[i - 1] = day["checked"] as Boolean
        }
        return dayChecks
    }

    fun getExercises(weekNum: Int, dayNum: Int): ArrayList<String?> {

        if (weeks == null) return arrayListOf()

        val week = weeks["WEEK " + weekNum] as HashMap<*, *>
        val days = week["days"] as HashMap<*, *>
        val day = days["DAY " + dayNum] as HashMap<*, *>
        val exercises = day["exercises"] as HashMap<*, *>
        val exerciseNames = ArrayList<String?>()
        for (i in 1 until exercises.keys.size + 1){
            val exName = exercises["exercise " + i] as HashMap<*, *>
            exerciseNames.add(exName["name"] as String)
        }
        return exerciseNames
    }

    fun getExerciseSets(weekNum: Int, dayNum: Int, exerciseName: String): HashMap<String, HashMap<String, Int>?> {

        if (weeks == null) return HashMap<String, HashMap<String, Int>?>()

        val week = weeks["WEEK " + weekNum] as HashMap<*, *>
        val days = week["days"] as HashMap<*, *>
        val day = days["DAY " + dayNum] as HashMap<*, *>
        val exercises = day["exercises"] as HashMap<*, *>
        val exercise = exercises[exerciseName] as HashMap<*, *>
        return exercise["sets"] as HashMap<String, HashMap<String, Int>?>
    }
}
