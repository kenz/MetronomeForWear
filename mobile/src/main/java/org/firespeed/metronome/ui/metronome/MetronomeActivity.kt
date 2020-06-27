package org.firespeed.metronome.ui.metronome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.firespeed.metronome.R
import org.firespeed.metronome.ui.metronome.MetronomeFragment

class MetronomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.metronome_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MetronomeFragment.newInstance())
                .commitNow()
        }
    }

}