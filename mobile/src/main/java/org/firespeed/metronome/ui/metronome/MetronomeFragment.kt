package org.firespeed.metronome.ui.metronome

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.firespeed.metronome.R
import org.firespeed.metronome.ui.MetronomeViewModel

class MetronomeFragment : Fragment() {

    companion object {
        fun newInstance() = MetronomeFragment()
    }

    private lateinit var viewModel: MetronomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.metronome_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(org.firespeed.metronome.ui.MetronomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}