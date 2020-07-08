package org.firespeed.metronome.ui.metronome

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import org.firespeed.metronome.R
import org.firespeed.metronome.actions.BeepTaktAction
import org.firespeed.metronome.actions.VibratorTaktAction
import org.firespeed.metronome.databinding.MetronomeFragmentBinding
import org.firespeed.metronome.ui.MetronomeViewModel

class MetronomeFragment : Fragment() {

    companion object {
        fun newInstance() = MetronomeFragment()
    }

    private val viewModel: MetronomeViewModel by activityViewModels()
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
        binding.seekBmp.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.setBpm(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
        binding.editBpm.addTextChangedListener { viewModel.setBpm(it.toString().toInt()) }
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
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}