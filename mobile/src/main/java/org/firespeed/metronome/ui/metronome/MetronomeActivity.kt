package org.firespeed.metronome.ui.metronome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.firespeed.metronome.R

@AndroidEntryPoint
class MetronomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.metronome_activity)

    }

}