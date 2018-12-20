package com.buinvent.jurassic_gains

import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class Gainer(val weeks: ArrayList<HashMap<String, Any>>?, val name: String?) {

    constructor() : this(null, null)

    fun getWeekChecks(): Array<Boolean?> {

        if (weeks == null) return arrayOfNulls(0)
        val weekChecks = arrayOfNulls<Boolean>(weeks.size)

        for (i in 0 until weeks.size) {
            weekChecks[i] = weeks[i]["checked"] as Boolean
        }
        return weekChecks
    }

    fun getDayChecks(weekIndex: Int): Array<Boolean?> {

        if (weeks == null) return arrayOfNulls(0)
        val days = weeks[weekIndex]["days"] as ArrayList<*>
        val dayChecks = arrayOfNulls<Boolean>(days.size)

        for (i in 0 until days.size) {
            dayChecks[i] = (days[i] as HashMap<*, *>)["checked"] as Boolean
        }
        return dayChecks
    }

    fun getExerciseSets(weekIndex: Int, dayIndex: Int, exerciseIndex: Int): ArrayList<HashMap<String, Int>?> {

        if (weeks == null) return arrayListOf()

        val week = weeks[weekIndex] as HashMap<*, *>
        val day = (week["days"] as ArrayList<*>)[dayIndex] as HashMap<*, *>
        val exercise = (day["exercises"] as ArrayList<*>)[exerciseIndex] as HashMap<*, *>

        return exercise["sets"] as ArrayList<HashMap<String, Int>?>

    }
}
