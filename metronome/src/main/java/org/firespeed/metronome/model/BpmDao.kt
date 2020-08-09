package org.firespeed.metronome.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BpmDao {
    @Query("SELECT * FROM bpm_list")
    fun getAll(): LiveData<List<Bpm>>

    @Query("SELECT * FROM bpm_list WHERE uid = (:uid)")
    fun loadById(uid: Long): LiveData<Bpm>

    @Insert
    fun insertAll(vararg bpmList: Bpm)

    @Update
    fun update(vararg bpm: Bpm)

    @Delete
    fun delete(vararg bpm: Bpm)

    @Query("DELETE FROM bpm_list")
    fun deleteAll()

}