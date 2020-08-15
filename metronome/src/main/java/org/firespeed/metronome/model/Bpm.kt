package org.firespeed.metronome.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bpm_list")
data class Bpm(
    @PrimaryKey(autoGenerate = true) var uid: Long = 0,
    @ColumnInfo var title: String,
    @ColumnInfo var bpm: Int,
    @ColumnInfo var order:Long
){
    companion object{
        private const val DEFAULT_INT = 60
        fun getDefaultBpm()=Bpm(
            uid = -1L,
            bpm = DEFAULT_INT,
            title = "",
            order = -1L
        )
    }
}
