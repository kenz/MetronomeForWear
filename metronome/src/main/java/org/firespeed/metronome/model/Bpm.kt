package org.firespeed.metronome.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bpm_list")
data class Bpm(
    @PrimaryKey(autoGenerate = true) var uid: Long = 0,
    @ColumnInfo val title: String,
    @ColumnInfo val bpm: Int,
    @ColumnInfo val order:Long
)
