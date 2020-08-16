package org.firespeed.metronome.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "bpm_list")
data class Bpm(
    @PrimaryKey(autoGenerate = true) var uid: Long = 0,
    @ColumnInfo var title: String,
    @ColumnInfo var bpm: Int,
    @ColumnInfo var order:Long
): Parcelable {

    companion object{
        private const val DEFAULT_INT = 60
        const val MIN_INT = 20
        const val MAX_INT = 600

        fun getDefaultBpm()=Bpm(
            uid = -1L,
            bpm = DEFAULT_INT,
            title = "",
            order = -1L
        )
    }
}


