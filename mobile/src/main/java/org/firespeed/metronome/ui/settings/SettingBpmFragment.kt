package org.firespeed.metronome.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import org.firespeed.metronome.R
import org.firespeed.metronome.databinding.SettingBpmFragmentBinding
import org.firespeed.metronome.model.Bpm
import org.firespeed.metronome.ui.SettingBpmViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SettingBpmFragment : Fragment() {

    companion object {
        fun newInstance() = SettingBpmFragment()
    }

    private val viewModel: SettingBpmViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<SettingBpmFragmentBinding>(
            inflater,
            R.layout.setting_bpm_fragment,
            container,
            false
        ).also { binding ->
            binding.fragment = this
            binding.lifecycleOwner = this
            binding.viewModel = viewModel
            binding.add.setOnClickListener {
                val bpm = Bpm(
                    title = binding.title.text.toString(),
                    bpm = binding.bpm.text.toString().toInt(),
                    order = 0
                )
                viewModel.add(bpm)
            }
            val adapter = BpmListAdapter {
                // onClickListener
                viewModel.selectBpm(it)
            }
            viewModel.getConfig()
            binding.bpmList.layoutManager = LinearLayoutManager(context)
            binding.bpmList.adapter = adapter
            viewModel.bpmListFlow.onEach {
                adapter.setList(it)
            }
                .launchIn(viewLifecycleOwner.lifecycleScope)
            viewModel.selectedBpmFlow.onEach {
                binding.selectedItem = it
            }
                .launchIn(viewLifecycleOwner.lifecycleScope)
            /*

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.bpmListFlow.collect {
                    adapter.setList(it)
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.selectedBpmFlow.collect {
                    binding.selectedItem = it
                }
            }

             */
        }

        return binding.root
    }


}