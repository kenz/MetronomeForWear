package org.firespeed.metronome.model

import androidx.room.*

@Dao
interface BpmDao {
    @Query("SELECT * FROM bpm_list ORDER BY `order` desc")
    fun getAll(): List<Bpm>

    @Query("SELECT * FROM bpm_list WHERE uid = (:uid)")
    fun loadById(uid: Long): Bpm?

    @Query("SELECT MAX(`order`) FROM bpm_list")
    fun maxOrder(): Long

    @Insert
    fun insert(bpmList: Bpm):Long

    @Update
    fun update(bpm: Bpm)

    @Delete
    fun delete(bpm: Bpm)

    @Query("DELETE FROM bpm_list")
    fun deleteAll()

}