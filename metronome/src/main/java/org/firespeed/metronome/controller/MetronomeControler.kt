package org.firespeed.metronome.controller

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.firespeed.metronome.animator.BpmCalculator
import org.firespeed.metronome.animator.reset
import org.firespeed.metronome.model.Bpm
import org.firespeed.metronome.model.BpmDataSource
import org.firespeed.metronome.model.PreferencesDataSource
import javax.inject.Inject

class MetronomeController @Inject constructor() {

    var valueUpdateListener: ((Float) -> Unit)? = null
    var taktTimeListener: (() -> Unit)? = null
    @Inject
    lateinit var bpmDataSource: BpmDataSource
    @Inject
    lateinit var preferencesDataSource: PreferencesDataSource

    val bpm: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().apply { value = DEFAULT_BPM }
    }
    val enable: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    private val animator: ValueAnimator

    fun selectedBpmFlow(): Flow<Bpm> = flow {
        val selectedId = preferencesDataSource.getSelectedBpm()
        emit(if (selectedId < 0L) {
            Bpm.getDefaultBpm()
        }else {
            val selectedBpm = bpmDataSource.loadById(selectedId)
            selectedBpm
        })
    }

    private val bpmCalculator = BpmCalculator()

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
        animator = ValueAnimator.ofFloat(0f, 360f).apply {
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            duration = bpmCalculator.toDuration(bpm.value ?: 0)
            addUpdateListener(valueUpdateListener)
            addListener(repeatListener)
            interpolator = LinearInterpolator()
        }
    }

    fun start() {
        enable.postValue(true)
        animator.start()
    }

    fun stop() {
        enable.postValue(false)
        animator.cancel()
    }


    fun setBpm(bpm: Int) {
        if (bpm != this.bpm.value) {
            this.bpm.value = bpm
            animator.duration = bpmCalculator.toDuration(bpm)
            animator.reset()
        }
    }

    fun reset() {
        animator.reset()

    }

    companion object {
        private const val DEFAULT_BPM = 60

    }


}