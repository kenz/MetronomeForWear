package org.firespeed.metronome.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.firespeed.metronome.model.Bpm
import org.firespeed.metronome.model.BpmDataSource
import org.firespeed.metronome.model.BpmDatabase
import org.firespeed.metronome.model.BpmLocalDataSource

// todo: migrate to hilt.
class SettingBpmViewModel(application: Application) : AndroidViewModel(application) {
    private val bpmList = MutableLiveData<List<Bpm>>()
    private val bpmDataSource: BpmDataSource

    init {
        val bpmDatabase =
            Room.databaseBuilder(application, BpmDatabase::class.java, "bpm.db").build()
        bpmDataSource = BpmLocalDataSource(bpmDatabase.bpmDao())
        getAll()
    }

    fun getAll()  = bpmDataSource.getAll()

    fun add(bpm:Bpm) = viewModelScope.launch(Dispatchers.IO){
        bpmDataSource.addBpm(bpm)
    }

}
