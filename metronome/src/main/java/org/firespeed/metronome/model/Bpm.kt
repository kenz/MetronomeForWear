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
    @ColumnInfo var order:Long = 0
): Parcelable {

    companion object{
        const val DEFAULT_VALUE = 60
        const val MIN_VALUE = 20
        const val MAX_VALUE = 600

        fun getDefaultBpm()=Bpm(
            uid = -1L,
            bpm = DEFAULT_VALUE,
            title = "",
            order = -1L
        )
    }
}


