package org.firespeed.metronome.model

import androidx.lifecycle.LiveData

class BpmLocalDataSource(private val bpmDao:BpmDao):BpmDataSource{

    override suspend fun addBpm(bpm: Bpm) = bpmDao.insertAll(bpm)
    override suspend fun getAll(): LiveData<List<Bpm>> = bpmDao.getAll()
    override suspend fun loadById(uid: Long): LiveData<Bpm> = bpmDao.loadById(uid)
    override suspend fun delete(uid: Long) = bpmDao.delete(uid)
    override suspend fun deleteAll() = bpmDao.deleteAll()

}