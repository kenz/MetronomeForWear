package org.firespeed.metronome.ui

import android.view.View
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.firespeed.metronome.animator.LinearAnimator

class MetronomeViewModel : ViewModel(),LifecycleObserver {
    val fps: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val enable: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }


    private val animator= LinearAnimator()

    private fun start() {
        if (enable.value != true) {
            enable.postValue(true)
            animator.start()
        }
    }

    private fun stop() {
        enable.postValue(false)
        animator.stop()
    }

    fun startStop() =
        if (enable.value == true) stop() else start()
}