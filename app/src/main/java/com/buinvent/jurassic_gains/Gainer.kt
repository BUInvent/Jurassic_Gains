package com.buinvent.jurassic_gains

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
//
//    fun getExerciseSets(weekIndex: Int, dayIndex: Int, exerciseIndex: Int): ArrayList<HashMap<String, Int>?> {
//
//        if (weeks == null) return arrayListOf()
//
//        val week = weeks[weekIndex] as HashMap<*, *>
//        val day = (week["days"] as ArrayList<*>)[dayIndex] as HashMap<*, *>
//        val exercise = (day["exercises"] as ArrayList<*>)[exerciseIndex] as HashMap<*, *>
//
//        return exercise["sets"] as ArrayList<HashMap<String, Int>?>
//
//    }
}
