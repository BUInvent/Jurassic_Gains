package com.buinvent.jurassic_gains

import com.google.common.collect.ArrayListMultimap
import java.util.*
import kotlin.collections.HashMap

//data class Gainer(val weeks: ArrayList<HashMap<String, ArrayList<HashMap<String, ArrayList<HashMap<String, ArrayList<HashMap<String, Long>>>>>>>>?, val name: String?) {
data class Gainer(val weeks: ArrayList<HashMap<String, Any>>?, val name: String?) {

    constructor() : this(null, null)

//    val week1: HashMap? = weeks[0]
//    val week1 = if (weeks != null) weeks[0] else -1

    fun printsomething() {
//        println("week1 = " + week1)
//        println("weeks are = " + weeks[0])
        if (weeks != null) println("weeks are = " + weeks[0])
    }
}