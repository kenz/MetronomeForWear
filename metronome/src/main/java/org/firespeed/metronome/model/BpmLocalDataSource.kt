package org.firespeed.metronome.model

class BpmLocalDataSource(private val bpmDao:BpmDao):BpmDataSource{

    override suspend fun addBpm(bpm: Bpm) = bpmDao.insertAll(bpm)
    override suspend fun getAll(): List<Bpm> = bpmDao.getAll()
    override suspend fun loadById(uid: Long): Bpm = bpmDao.loadById(uid)
    override suspend fun delete(bpm: Bpm) = bpmDao.delete(bpm)
    override suspend fun deleteAll() = bpmDao.deleteAll()
    override suspend fun maxOrder() = bpmDao.maxOrder()


}