package org.firespeed.metronome.model

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query

interface BpmDataSource {
    suspend fun addBpm(bpm:Bpm)
    suspend fun getAll(): LiveData<List<Bpm>>
    suspend fun loadById(uid: Long): LiveData<Bpm>
    suspend fun delete(uid: Long)
    suspend fun deleteAll()
}