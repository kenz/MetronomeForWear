package org.firespeed.metronome.ui.settings

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import org.firespeed.metronome.R
import org.firespeed.metronome.ui.MetronomeViewModel

class EditBpmDialogFragment : Fragment() {

    companion object {
        fun newInstance() =
            EditBpmDialogFragment()
    }

    private val viewModel: EditBpmDialogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bpm_dialog_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}