package org.firespeed.metronome.model

import androidx.lifecycle.LiveData

interface BpmDataSource {
    suspend fun addBpm(bpm:Bpm)
    fun getAll(): LiveData<List<Bpm>>
    fun loadById(uid: Long): LiveData<Bpm>
    suspend fun delete(bpm: Bpm)
    suspend fun deleteAll()
}