package org.firespeed.metronome.model

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import javax.inject.Inject

class PreferencesLocalDataSource(val context: Context) : PreferencesDataSource {

    @Inject
    lateinit var bpmDataSource: BpmDataSource
    override suspend fun setSelectedBpm(id: Long) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        pref.edit {
            this.putLong(KEY_SELECTED_BPM, id)
        }
    }

    override suspend fun getSelectedBpm(): Long =
        PreferenceManager.getDefaultSharedPreferences(context).getLong(KEY_SELECTED_BPM, -1L)

    override suspend fun setEnableBeepTaktActionSet(enabled: Boolean) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        pref.edit {
            this.putBoolean(KEY_BEEP_TAKT_ACTION, enabled)
        }
    }

    override suspend fun getEnableBeepTaktActionSet(): Boolean =
        PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(KEY_BEEP_TAKT_ACTION, true)

    override suspend fun setEnableVibratorTaktActionSet(enabled: Boolean) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        pref.edit {
            this.putBoolean(KEY_VIBRATOR_TAKT_ACTION, enabled)
        }
    }

    override suspend fun getEnableVibratorTaktActionSet(): Boolean =
        PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(KEY_VIBRATOR_TAKT_ACTION, true)

    companion object {
        private const val KEY_SELECTED_BPM = "selectedBpm"
        private const val KEY_VIBRATOR_TAKT_ACTION = "vibratorTaktAction"
        private const val KEY_BEEP_TAKT_ACTION = "beepTaktAction"
    }
}