package org.firespeed.metronome.model


interface PreferencesDataSource {
    suspend fun setSelectedBpm(id: Long)
    suspend fun getSelectedBpm(): Long
    suspend fun getEnableBeepTaktActionSet(): Boolean
    suspend fun setEnableBeepTaktActionSet(enabled:Boolean)
    suspend fun getEnableVibratorTaktActionSet(): Boolean
    suspend fun setEnableVibratorTaktActionSet(enabled:Boolean)
}