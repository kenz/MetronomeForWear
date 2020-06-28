package org.firespeed.metronome.animator

import android.animation.Animator
import android.animation.ValueAnimator

class LinearAnimator(
    bpm: Int,
    valueUpdateListener: ValueAnimator.AnimatorUpdateListener,
    repeatListener: Animator.AnimatorListener
) {
    private val bpmCalculator = BpmCalculator()
    private val animator: ValueAnimator = ValueAnimator.ofInt(0, MAX_VALUE).apply {
        repeatMode = ValueAnimator.RESTART
        repeatCount = ValueAnimator.INFINITE
        duration = bpmCalculator.toDuration(bpm)
        addUpdateListener(valueUpdateListener)
        addListener(repeatListener)
    }

    fun start() = animator.start()
    fun stop() = animator.end()
    var bpm: Int
        get() = bpmCalculator.toBpm(animator.duration )
        set(v) {
            animator.duration = bpmCalculator.toDuration(v)
            animator.reset()
        }

    companion object {
        const val MAX_VALUE = 255
    }

}
