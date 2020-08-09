package org.firespeed.metronome.model

import androidx.lifecycle.LiveData

class BpmLocalDataSource(private val bpmDao:BpmDao):BpmDataSource{

    override suspend fun addBpm(bpm: Bpm) = bpmDao.insertAll(bpm)
    override fun getAll(): LiveData<List<Bpm>> = bpmDao.getAll()
    override fun loadById(uid: Long): LiveData<Bpm> = bpmDao.loadById(uid)
    override suspend fun delete(bpm: Bpm) = bpmDao.delete(bpm)
    override suspend fun deleteAll() = bpmDao.deleteAll()

}