package org.firespeed.metronome.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BpmDao {
    @Query("SELECT * FROM bpm_list")
    fun getAll(): LiveData<List<Bpm>>

    @Query("SELECT * FROM bpm_list WHERE uid = (:uid)")
    fun loadById(uid: Long): LiveData<Bpm>

    @Insert
    fun insertAll(vararg bpmList: Bpm)

    @Query("DELETE FROM bpm_list WHERE uid = (:uid)")
    fun delete(uid: Long)

    @Query("DELETE FROM bpm_list")
    fun deleteAll()

}