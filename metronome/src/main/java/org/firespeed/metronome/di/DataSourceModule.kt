package org.firespeed.metronome.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewComponent
import dagger.hilt.android.qualifiers.ActivityContext
import org.firespeed.metronome.model.BpmDataSource
import org.firespeed.metronome.model.BpmDatabase
import org.firespeed.metronome.model.BpmLocalDataSource

@Module
@InstallIn(ActivityComponent::class)
object DataSourceModule {
    @Provides
    fun bindBpmDataSource(@ActivityContext context:Context): BpmDataSource {
        val bpmDatabase = Room.databaseBuilder(context, BpmDatabase::class.java, "bpm.db").build()
        return BpmLocalDataSource(bpmDatabase.bpmDao())
    }


}
