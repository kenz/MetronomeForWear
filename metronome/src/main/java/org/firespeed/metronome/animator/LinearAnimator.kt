package org.firespeed.metronome.animator

import android.animation.Animator
import android.animation.ValueAnimator

class LinearAnimator(fps:Long, valueUpdateListener:ValueAnimator.AnimatorUpdateListener, repeatListener: Animator.AnimatorListener) {
    private val animator:ValueAnimator = ValueAnimator.ofFloat(0f, MAX_VALUE).apply {
        repeatMode = ValueAnimator.RESTART
        repeatCount = ValueAnimator.INFINITE
        duration = 1000L / fps
        addUpdateListener ( valueUpdateListener )
        addListener (repeatListener)
    }

    fun start() = animator.start()
    fun stop() = animator.cancel()

}
const val MAX_VALUE = 255f