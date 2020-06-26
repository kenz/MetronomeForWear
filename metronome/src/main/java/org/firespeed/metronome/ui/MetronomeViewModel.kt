package org.firespeed.metronome.ui

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import androidx.databinding.BaseObservable
import androidx.lifecycle.*
import org.firespeed.metronome.actions.TaktAction
import org.firespeed.metronome.actions.VibratorTaktAction
import org.firespeed.metronome.animator.LinearAnimator

class MetronomeViewModel : ViewModel(), LifecycleObserver{
    val bpm: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val enable: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun updateBpm(bpm:String){
        updateBpm(bpm.toInt())
    }

    fun updateBpm(bpm:Int){
        if(this.bpm.value != bpm){
            this.bpm.value = bpm
            animator?.bpm = bpm
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        bpm.postValue(60)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        stop()
    }

    private var animator: LinearAnimator? = null

    private fun start() {
        val bpm= bpm.value ?: return
        if (enable.value != true) {
            enable.postValue(true)
            val valueUpdateListener = ValueAnimator.AnimatorUpdateListener {
                onValueUpdateListener?.invoke(it.animatedValue as Int)
            }

            val repeatListener = object : AnimatorListener {
                override fun onAnimationRepeat(anim: Animator?) {
                    onTaktTimeListener?.invoke()
                }
                override fun onAnimationStart(anim: Animator?) {}
                override fun onAnimationEnd(anim: Animator?) {}
                override fun onAnimationCancel(anim: Animator?) {}
            }
            animator = LinearAnimator(bpm, valueUpdateListener, repeatListener).apply {
                this.start()
            }
        }
    }

    var onTaktTimeListener: (()->Unit)? = null
    var onValueUpdateListener: ((Int)->Unit)? = null

    private fun stop() {
        enable.postValue(false)
        animator?.stop()
    }

    fun startStop() =
        if (enable.value == true) stop() else start()
}