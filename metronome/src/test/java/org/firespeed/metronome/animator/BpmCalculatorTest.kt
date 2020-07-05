package org.firespeed.metronome.animator


import org.junit.Test

import org.junit.Assert.*


class BpmCalculatorTest {
    private val target = BpmCalculator()

    @Test
    fun testToBpm() {
        assertEquals(60, target.toBpm(1000L))
        assertEquals(30, target.toBpm(2000L))
        assertEquals(120, target.toBpm(500L))
    }

    @Test
    fun testToDuration() {
        assertEquals(1000L, target.toDuration(60))
        assertEquals(2000L, target.toDuration(30))
        assertEquals(500L, target.toDuration(120))

    }

}