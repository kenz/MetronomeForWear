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
import kotlinx.coroutines.flow.callbackFlow
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
    private val selectedBpmChannel = Channel<Bpm>(Channel.BUFFERED)
    val selectedBpmFlow = selectedBpmChannel.receiveAsFlow()
    private val insertedBpmChannel = Channel<Bpm>(Channel.BUFFERED)
    val insertedBpmFlow = insertedBpmChannel.receiveAsFlow()
    private val updatedBpmChannel = Channel<Bpm>(Channel.BUFFERED)
    val updatedBpmFlow = updatedBpmChannel.receiveAsFlow()
    private val deletedBpmChannel = Channel<Bpm>(Channel.BUFFERED)
    val deletedBpmFlow = deletedBpmChannel.receiveAsFlow()

    var editingBpm: Bpm? = null

    fun saveEditingBpm() {
        editingBpm?.let {
            if (it.uid == 0L)
                insertBpm(it)
            else
                updateBpm(it)
        }
    }
    fun deleteEditingBpm(){
        editingBpm?.let{
            deleteBpm(it)
        }
    }

    fun getConfig() {
        viewModelScope.launch(Dispatchers.IO) {
            val l = bpmDataSource.getAll()
            bpmListChannel.send(l)
            val selectedBpmUid = preferencesDataSource.getSelectedBpm()
            l.firstOrNull { it.uid == selectedBpmUid }?.let {
                selectedBpmChannel.send(it)
            }
        }
    }

    private fun insertBpm(bpm: Bpm) = viewModelScope.launch(Dispatchers.IO) {
        if (bpm.title.isEmpty()) {
            bpm.title = context.getString(R.string.untitled)
        }
        bpm.order = bpmDataSource.maxOrder() + 1
        bpm.uid = bpmDataSource.insertBpm(bpm)
        selectBpm(bpm)
        insertedBpmChannel.send(bpm)
    }

    private fun updateBpm(bpm: Bpm) = viewModelScope.launch(Dispatchers.IO) {
        if (bpm.title.isEmpty()) {
            bpm.title = context.getString(R.string.untitled)
        }
        bpmDataSource.updateBpm(bpm)
        selectBpm(bpm)
        updatedBpmChannel.send(bpm)
    }
    private fun deleteBpm(bpm:Bpm) = viewModelScope.launch (Dispatchers.IO){
        // 先に削除する通知を送る
        deletedBpmChannel.send(bpm)
        bpmDataSource.delete(bpm)
    }

    fun selectBpm(bpm: Bpm) {
        viewModelScope.launch(Dispatchers.IO) {
            selectedBpmChannel.send(bpm)
            preferencesDataSource.setSelectedBpm(bpm.uid)
        }
    }

}
