package org.firespeed.metronome.ui.settings

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.firespeed.metronome.actions.R
import org.firespeed.metronome.model.Bpm
import org.firespeed.metronome.model.BpmDataSource
import org.firespeed.metronome.model.PreferencesDataSource

@ExperimentalCoroutinesApi
class SettingBpmViewModel @ViewModelInject constructor(
    @ActivityContext private val context: Context,
    private val bpmDataSource: BpmDataSource,
    private val preferencesDataSource: PreferencesDataSource,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val bpmListChannel = Channel<List<Bpm>>(Channel.BUFFERED)
    val bpmListFlow = bpmListChannel.receiveAsFlow()
    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventFlow = eventChannel.receiveAsFlow()

    var editingBpm: Bpm? = null
    fun saveEditingBpm() {
        editingBpm?.let {
            if (it.uid == 0L)
                insertBpm(it)
            else
                updateBpm(it)
        }
    }

    fun handleListEvent(event: BpmListAdapter.Event)  {
        when (event) {
            is BpmListAdapter.Event.StartCreate ->  startCreate()
            is BpmListAdapter.Event.StartEdit -> startEdit(event.bpmItem)
            is BpmListAdapter.Event.Edited -> updateBpm(event.bpmItem)
            is BpmListAdapter.Event.Select -> selectBpm(event.bpmItem)
            is BpmListAdapter.Event.Delete -> deleteBpm(event.bpmItem)
            is BpmListAdapter.Event.Switch -> switchBpm(event.bpmItem0, event.bpmItem1)
        }
    }

    fun getConfig() {
        viewModelScope.launch(Dispatchers.IO) {
            val allItem = bpmDataSource.getAll()
            bpmListChannel.send(allItem)
            val selectedBpmUid = preferencesDataSource.getSelectedBpm()
            allItem.firstOrNull { it.uid == selectedBpmUid }?.let {
                eventChannel.send(Event.Selected(it))
            }
        }
    }

    private fun startCreate() = viewModelScope.launch(Dispatchers.IO){
        editingBpm = null
        eventChannel.send(Event.StartCreate)
    }

    private fun startEdit(bpm:Bpm) = viewModelScope.launch(Dispatchers.IO){
        editingBpm = bpm
        eventChannel.send(Event.StartEdit(bpm))
    }

    private fun insertBpm(bpm: Bpm) = viewModelScope.launch(Dispatchers.IO) {
        if (bpm.title.isEmpty()) {
            bpm.title = context.getString(R.string.untitled)
        }
        bpm.order = bpmDataSource.maxOrder() + 1
        bpm.uid = bpmDataSource.insertBpm(bpm)
        selectBpm(bpm)
        eventChannel.send(Event.Inserted(bpm))
    }

    private fun updateBpm(bpm: Bpm) = viewModelScope.launch(Dispatchers.IO) {
        if (bpm.title.isEmpty()) {
            bpm.title = context.getString(R.string.untitled)
        }
        bpmDataSource.updateBpm(bpm)
        selectBpm(bpm)
    }

    private fun deleteBpm(bpm: Bpm) = viewModelScope.launch(Dispatchers.IO) {
        bpmDataSource.delete(bpm)
        eventChannel.send(Event.Deleted(bpm))
    }

    private fun selectBpm(bpm: Bpm) = viewModelScope.launch(Dispatchers.IO){
        preferencesDataSource.setSelectedBpm(bpm.uid)
        eventChannel.send(Event.Selected(bpm))
    }

    private fun switchBpm(bpm0: Bpm, bpm1: Bpm) = viewModelScope.launch(Dispatchers.IO){
        val order1 = bpm0.order
        bpm0.order = bpm1.order
        bpm1.order = order1
        bpmDataSource.updateBpm(bpm0)
        bpmDataSource.updateBpm(bpm1)
    }

    sealed class Event {
        object StartCreate : Event()
        class Selected(val bpm: Bpm) : Event()
        class Inserted(val bpm: Bpm) : Event()
        class StartEdit(val bpm: Bpm) : Event()
        class Edited(val bpm: Bpm) : Event()
        class Deleted(val bpm: Bpm) : Event()
    }


}

fun MutableList<Bpm>.switch(a: Int, b: Int) {
    val temp = get(a)
    this[a] = this[b]
    this[b] = temp
}