package org.firespeed.metronome.ui.metronome

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.firespeed.metronome.R
import org.firespeed.metronome.actions.BeepTaktAction
import org.firespeed.metronome.actions.VibratorTaktAction
import org.firespeed.metronome.databinding.MetronomeMobileFragmentBinding
import org.firespeed.metronome.ui.MetronomeViewModel

@AndroidEntryPoint
class MetronomeFragment : Fragment() {

    private val viewModel: MetronomeViewModel by viewModels()
    private var binding: MetronomeMobileFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<MetronomeMobileFragmentBinding>(
            inflater,
            R.layout.metronome_mobile_fragment,
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
        binding.background.viewTreeObserver.addOnGlobalLayoutListener {
            val parentWidth = binding.background.width
            val parentHeight = binding.background.height
            val handWidth = calcSize(parentWidth, 0.0721f)
            val handHeight = calcSize(parentHeight, 0.5673f)
            val requireRequestLayout  = handWidth != binding.hand.width
            binding.hand.setSize(handWidth, handHeight)
            binding.handShadow.setSize(handWidth, handHeight)
            val holeCapWidth = calcSize(parentWidth, 0.05769f)
            val holeCapHeight = calcSize(parentHeight, 0.0673076923f)
            binding.holeCap.setSize(holeCapWidth, holeCapHeight)
            val bpmWidth = calcSize(parentWidth, 0.125f)
            val bpmHeight = calcSize(parentHeight, 0.2365f)
            binding.txtBpm.setSize(bpmWidth, bpmHeight)
            val centerY = binding.background.height / 2f
            val handY = binding.hand.y - binding.background.y
            binding.hand.pivotY = centerY - handY
            binding.hand.pivotX = binding.hand.width / 2f
            binding.handShadow.pivotY = binding.hand.pivotY + binding.handShadow.y - binding.hand.y
            if(requireRequestLayout)binding.background.requestLayout()
        }
        binding.handShadow.viewTreeObserver.addOnGlobalLayoutListener {
            binding.handShadow.pivotX = binding.handShadow.width / 2f
        }
        viewModel.setValueUpdateListener {
            binding.hand.rotation = it
            binding.handShadow.rotation = it
        }
        binding.background.requestLayout()


        lifecycle.addObserver(viewModel)
        context?.let { context ->
            viewModel.actionVibrator =
                VibratorTaktAction(
                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator,
                    30L
                )
            viewModel.actionSound = BeepTaktAction()
        }
        viewModel.isVibratorEnable.postValue(true)
        return binding.root

    }

    private fun View.setSize(width: Int, height: Int) {
        this.layoutParams.width = width
        this.layoutParams.height = height
    }

    private fun calcSize(parent: Int, ratio: Float): Int = (parent * ratio).toInt()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}