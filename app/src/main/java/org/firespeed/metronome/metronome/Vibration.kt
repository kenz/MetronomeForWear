package org.firespeed.metronome.metronome

import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE

class Vibration {
    init{
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val vibrationEffect = VibrationEffect.createOneShot(300, DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            vibrator.vibrate(300)
        }
    }
    fun call(){

    }
}