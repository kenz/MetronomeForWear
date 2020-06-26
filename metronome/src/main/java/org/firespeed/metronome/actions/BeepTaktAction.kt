package org.firespeed.metronome.actions

import android.media.AudioManager
import android.media.ToneGenerator


class BeepTaktAction : TaktAction {

    private val tone =
        ToneGenerator(AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME)

    override fun action() {
        tone.startTone(ToneGenerator.TONE_PROP_BEEP)
    }
}