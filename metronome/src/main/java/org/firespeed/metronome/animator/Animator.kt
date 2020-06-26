package org.firespeed.metronome.animator

import android.animation.ValueAnimator

fun ValueAnimator.reset(){
    cancel()
    start()
}