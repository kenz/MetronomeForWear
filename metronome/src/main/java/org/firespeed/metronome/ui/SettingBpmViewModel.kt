package org.firespeed.metronome.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.firespeed.metronome.model.Bpm
import org.firespeed.metronome.model.BpmDataSource
import org.firespeed.metronome.model.PreferencesDataSource

class SettingBpmViewModel @ViewModelInject constructor(
    private val bpmDataSource: BpmDataSource,
    private val preferencesDataSource : PreferencesDataSource,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val bpmListChannel = Channel<List<Bpm>>(Channel.BUFFERED)
    @ExperimentalCoroutinesApi
    val bpmListFlow = bpmListChannel.consumeAsFlow()
    val selectedBpmChannel = Channel<Bpm>(Channel.BUFFERED)

    fun getConfig(){
        viewModelScope.launch(Dispatchers.IO) {
            val l = bpmDataSource.getAll()
            bpmListChannel.send(l)
            val selectedBpmUid = preferencesDataSource.getSelectedBpm()
            l.firstOrNull{ it.uid == selectedBpmUid}?.let{
                selectedBpmChannel.send(it)
            }
        }
    }
    fun add(bpm: Bpm) = viewModelScope.launch(Dispatchers.IO) {
        bpmDataSource.addBpm(bpm)
    }
}
