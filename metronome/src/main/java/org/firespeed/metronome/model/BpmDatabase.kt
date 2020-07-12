package org.firespeed.metronome.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Bpm::class], version = 1, exportSchema = false)
abstract class BpmDatabase : RoomDatabase(){
    abstract fun bpmDao():BpmDao
}