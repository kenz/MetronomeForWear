package org.firespeed.metronome.model

import androidx.room.Dao
import androidx.room.Query

@Dao
interface BpmDao {
    @Query("SELECT * FROM bpm")
    fun getAll(): List<Bpm>

    @Query("SELECT * FROM bpm WHERE id = (:id)")
    fun loadAllByIds(id: Int): List<Bpm>
}