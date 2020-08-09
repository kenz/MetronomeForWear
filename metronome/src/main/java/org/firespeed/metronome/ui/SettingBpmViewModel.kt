package org.firespeed.metronome.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.firespeed.metronome.model.Bpm
import org.firespeed.metronome.model.BpmDataSource

class SettingBpmViewModel @ViewModelInject constructor(
    private val bpmDataSource: BpmDataSource,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val bpmList = MutableLiveData<List<Bpm>>()
    fun getAll() = bpmDataSource.getAll()

    fun add(bpm: Bpm) = viewModelScope.launch(Dispatchers.IO) {
        bpmDataSource.addBpm(bpm)
    }

}
