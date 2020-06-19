package org.firespeed.metronome.metronome

class Metronome {
    fun start(fps: Int, callback: (Int)->Unit) {
        callback.invoke(10)

    }

    fun stop() {

    }

    fun reset() {

    }

}

