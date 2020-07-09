package org.firespeed.metronome.ui.settings

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import org.firespeed.metronome.R
import org.firespeed.metronome.ui.MetronomeViewModel
import org.firespeed.metronome.ui.SettingBpmViewModel

class SettingBpmFragment : Fragment() {

    companion object {
        fun newInstance() = SettingBpmFragment()
    }
    private val viewModel: SettingBpmViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.setting_bpm_fragment, container, false)
    }


}