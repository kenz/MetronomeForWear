package org.firespeed.metronome.controller

import android.animation.Animator
import android.animation.ValueAnimator
import androidx.lifecycle.MutableLiveData
import org.firespeed.metronome.animator.LinearAnimator

class MetronomeController {

    var valueUpdateListener: ((Float) -> Unit)? = null
    var taktTimeListener: (() -> Unit)? = null
    private val animator: LinearAnimator

    val bpm: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val enable: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    init {
        val valueUpdateListener = ValueAnimator.AnimatorUpdateListener {
            valueUpdateListener?.invoke(it.animatedValue as Float)
        }

        val repeatListener = object : Animator.AnimatorListener {
            override fun onAnimationRepeat(anim: Animator?) {
                taktTimeListener?.invoke()
            }

            override fun onAnimationStart(anim: Animator?) {}
            override fun onAnimationEnd(anim: Animator?) {}
            override fun onAnimationCancel(anim: Animator?) {}
        }
        animator = LinearAnimator(bpm.value ?: 0, valueUpdateListener, repeatListener)
    }

    fun start() {
        enable.postValue(true)
        animator.start()
    }

    fun stop() {
        enable.postValue(false)
        animator.stop()
    }


    fun setBpm(bpm: Int) {
        if (bpm != this.bpm.value) {
            this.bpm.value = bpm
            animator.bpm = bpm
        }
    }

    companion object {
        private const val DEFAULT_BPM = 0
    }

}