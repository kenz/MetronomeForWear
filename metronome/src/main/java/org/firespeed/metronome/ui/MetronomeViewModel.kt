package org.firespeed.metronome.ui

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MetronomeViewModel : ViewModel() {
    val fps: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val enable: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun start() {
        if (enable.value != true) {
            enable.postValue(true)
        }
    }

    fun stop() {
        enable.postValue(false)
    }

    fun startStop() =
        if (enable.value == true) stop() else start()
}