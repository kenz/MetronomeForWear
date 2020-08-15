package org.firespeed.metronome.model

interface BpmDataSource {
    suspend fun addBpm(bpm:Bpm)
    suspend fun getAll(): List<Bpm>
    suspend fun loadById(uid: Long): Bpm
    suspend fun delete(bpm: Bpm)
    suspend fun deleteAll()
    suspend fun maxOrder():Long
}