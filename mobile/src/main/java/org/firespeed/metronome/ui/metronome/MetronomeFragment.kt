package org.firespeed.metronome.ui.metronome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import org.firespeed.metronome.R
import org.firespeed.metronome.databinding.MetronomeFragmentBinding
import org.firespeed.metronome.ui.MetronomeViewModel

class MetronomeFragment : Fragment() {

    companion object {
        fun newInstance() = MetronomeFragment()
    }

    private val viewModel: MetronomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DataBindingUtil.inflate<MetronomeFragmentBinding>(
            inflater,
            R.layout.metronome_fragment,
            container,
            false
        ).let {
            it.fragment = this
            it.lifecycleOwner = this
            it.viewModel = viewModel
            it.root
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}