package org.firespeed.metronome.animator

class BpmCalculator {
    fun toDuration(bpm: Int): Long {
        if (bpm == 0) return 0
        return 1000L * 60L / bpm.toLong()
    }

    fun toBpm(duration: Long): Int {
        if (duration == 0L) return 0
        return (MINUTE_MILLISECONDS / duration).toInt()
    }

    companion object {
        const val MINUTE_MILLISECONDS = 1000L * 60L
    }

}