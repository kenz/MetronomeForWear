package org.firespeed.metronome.actions

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class VibratorTaktAction(vibrator: Vibrator, strong: Long) : TaktAction {

    private val action:VibratorAction
    init {
        action =
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> OreoVibrator(
                    vibrator,
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                )
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> OreoVibrator(
                    vibrator,
                    VibrationEffect.createOneShot(strong, VibrationEffect.DEFAULT_AMPLITUDE)
                )
                else -> LegacyVibrator(vibrator, strong)
            }
    }

    override fun action() {
        GlobalScope.launch(Dispatchers.Default){
            action.action()
        }
    }

    private interface VibratorAction {
        val vibrator: Vibrator
        fun action()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private class OreoVibrator(override val vibrator: Vibrator, private val vibe: VibrationEffect) :
        VibratorAction {
        override fun action() = vibrator.vibrate(vibe)
    }

    @Suppress("DEPRECATION")
    private class LegacyVibrator(override val vibrator: Vibrator, private val vibe: Long) :
        VibratorAction {
        override fun action() = vibrator.vibrate(vibe)
    }

}