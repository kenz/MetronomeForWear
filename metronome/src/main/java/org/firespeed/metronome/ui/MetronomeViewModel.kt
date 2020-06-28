package org.firespeed.metronome.ui

import androidx.lifecycle.*
import org.firespeed.metronome.controller.MetronomeController

class MetronomeViewModel : ViewModel(), LifecycleObserver {
    private val metronomeController: MetronomeController = MetronomeController()
    val bpm: MutableLiveData<Int>
    val enable: MutableLiveData<Boolean>

    init {
        bpm = metronomeController.bpm
        enable = metronomeController.enable
    }

    fun setBpm(bpm: Int) = metronomeController.setBpm(bpm)

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() = stop()

    private fun start() = metronomeController.start()
    private fun stop() = metronomeController.stop()

    fun setTaktTimeListener(listener: () -> Unit) {
        metronomeController.taktTimeListener = listener
    }

    fun setValueUpdateListener(listener: (Int) -> Unit) {
        metronomeController.valueUpdateListener = listener
    }


    fun startStop() = if (enable.value == true) stop() else start()
}