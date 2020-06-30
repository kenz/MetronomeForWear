package org.firespeed.metronome.animator

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator

class LinearAnimator(
    bpm: Int,
    valueUpdateListener: ValueAnimator.AnimatorUpdateListener,
    repeatListener: Animator.AnimatorListener
) {
    private val bpmCalculator = BpmCalculator()
    private val animator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        repeatMode = ValueAnimator.RESTART
        repeatCount = ValueAnimator.INFINITE
        duration = bpmCalculator.toDuration(bpm)
        addUpdateListener(valueUpdateListener)
        addListener(repeatListener)
        interpolator = LinearInterpolator()
    }

    fun start() = animator.start()
    fun stop() = animator.end()
    var bpm: Int
        get() = bpmCalculator.toBpm(animator.duration )
        set(v) {
            animator.duration = bpmCalculator.toDuration(v)
            animator.reset()
        }


}
