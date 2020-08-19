package org.firespeed.metronome.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.firespeed.metronome.actions.TaktAction
import org.firespeed.metronome.controller.MetronomeController

class MetronomeViewModel @ViewModelInject constructor(
    private val metronomeController: MetronomeController,
    @Assisted private val savedStateHandle: SavedStateHandle
) :
    ViewModel(), LifecycleObserver {
    private val enable: MutableLiveData<Boolean> = metronomeController.enable
    var isVibratorEnable = MutableLiveData<Boolean>()
    var isSoundEnable = MutableLiveData<Boolean>()
    var isVibratorCapability = MutableLiveData<Boolean>().also { it.value = false }
    var isSoundCapability = MutableLiveData<Boolean>().also { it.value = false }
    val bpm: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val enableActionList = ArrayList<TaktAction>()
    var actionVibrator: TaktAction? = null
        set(value) {
            if (value != null) {
                setActionEnable(value, true, isVibratorEnable)
                isVibratorCapability.postValue(true)
            }
            field = value
        }
    var actionSound: TaktAction? = null
        set(value) {
            if (value != null) {
                setActionEnable(value, true, isSoundEnable)
                isSoundCapability.postValue(true)
            }
            field = value
        }

    init {
        metronomeController.taktTimeListener = {
            enableActionList.map { it.action() }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        getSelectedBpm()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        stop()
        enableActionList.clear()
    }

    private fun getSelectedBpm() {
        viewModelScope.launch(Dispatchers.IO) {
            metronomeController.selectedBpmFlow().collect {
                bpm.postValue(it.bpm.toString())
                metronomeController.bpm = it.bpm
            }
        }
    }

    private fun start() = metronomeController.start()
    private fun stop() = metronomeController.stop()

    fun setValueUpdateListener(listener: (Float) -> Unit) {
        metronomeController.valueUpdateListener = listener
    }

    fun switchVibrator() = switchAction(actionVibrator, isVibratorEnable)

    fun switchSound() = switchAction(actionSound, isSoundEnable)

    private fun setActionEnable(
        action: TaktAction,
        enable: Boolean,
        enableLiveData: MutableLiveData<Boolean>
    ) {
        enableLiveData.postValue(enable)
        if (enable)
            enableActionList.add(action)
        else
            enableActionList.remove(action)
    }

    private fun switchAction(action: TaktAction?, enableLiveData: MutableLiveData<Boolean>) {
        if (action != null) {
            val enable = !(enableLiveData.value ?: false)
            setActionEnable(action, enable, enableLiveData)
        } else {
            enableLiveData.postValue(false)
            return
        }
    }

    fun startStop() = if (enable.value == true) stop() else start()
    fun reset() = metronomeController.reset()
}