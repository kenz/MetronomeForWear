package org.firespeed.metronome.animator

import android.animation.Animator
import android.animation.ValueAnimator

class LinearAnimator(
    bpm: Int,
    valueUpdateListener: ValueAnimator.AnimatorUpdateListener,
    repeatListener: Animator.AnimatorListener
) {
    private val animator: ValueAnimator = ValueAnimator.ofInt(0, MAX_VALUE).apply {
        repeatMode = ValueAnimator.RESTART
        repeatCount = ValueAnimator.INFINITE
        duration = 1000L * 60L / bpm
        addUpdateListener(valueUpdateListener)
        addListener(repeatListener)
    }

    fun start() = animator.start()
    fun stop() = animator.cancel()
    var bps: Int
        get() = (animator.duration / 60L / 1000L).toInt()
        set(v) {
            animator.duration = 1000L * 60L / v
        }

    companion object {
        const val MAX_VALUE = 255
    }

}
