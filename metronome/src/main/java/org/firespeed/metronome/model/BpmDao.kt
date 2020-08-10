package org.firespeed.metronome.model

import androidx.room.*

@Dao
interface BpmDao {
    @Query("SELECT * FROM bpm_list")
    fun getAll(): List<Bpm>

    @Query("SELECT * FROM bpm_list WHERE uid = (:uid)")
    fun loadById(uid: Long): Bpm

    @Insert
    fun insertAll(vararg bpmList: Bpm)

    @Update
    fun update(vararg bpm: Bpm)

    @Delete
    fun delete(vararg bpm: Bpm)

    @Query("DELETE FROM bpm_list")
    fun deleteAll()

}