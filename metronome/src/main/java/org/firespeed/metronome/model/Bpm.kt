package org.firespeed.metronome.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bpm(
    @PrimaryKey val uid: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val bpm: Int
)
