package org.firespeed.metronome

fun <T>MutableList<T>.switch(fromIndex:Int, toIndex:Int){
    val from = this[fromIndex]
    this[fromIndex] = this[toIndex]
    this[toIndex] = from
}