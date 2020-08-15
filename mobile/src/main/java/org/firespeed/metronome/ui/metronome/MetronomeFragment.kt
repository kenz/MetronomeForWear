package org.firespeed.metronome.ui.metronome

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.firespeed.metronome.R
import org.firespeed.metronome.actions.BeepTaktAction
import org.firespeed.metronome.actions.VibratorTaktAction
import org.firespeed.metronome.databinding.MetronomeFragmentBinding
import org.firespeed.metronome.ui.MetronomeViewModel

@AndroidEntryPoint
class MetronomeFragment : Fragment() {

    companion object {
        fun newInstance() = MetronomeFragment()
    }

    private val viewModel: MetronomeViewModel by viewModels()
    private var binding: MetronomeFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<MetronomeFragmentBinding>(
            inflater,
            R.layout.metronome_fragment,
            container,
            false
        ).also {
            it.fragment = this
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        binding.txtBpm.setOnClickListener {
            findNavController().navigate(R.id.action_metronomeFragment_to_settingBpmFragment)
        }
        binding.progressBar.max = 360

        lifecycle.addObserver(viewModel)
        context?.let { context ->
            viewModel.actionVibrator =
                VibratorTaktAction(
                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator,
                    30L
                )
            viewModel.actionSound = BeepTaktAction()
        }
        viewModel.setValueUpdateListener {
            binding.progressBar.progress = it.toInt()
        }
        viewModel.isVibratorEnable.postValue(true)
        return binding.root

    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}