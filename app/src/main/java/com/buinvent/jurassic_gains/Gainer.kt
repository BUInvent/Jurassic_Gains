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
}
