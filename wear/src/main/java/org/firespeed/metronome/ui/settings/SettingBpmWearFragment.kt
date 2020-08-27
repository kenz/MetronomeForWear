package org.firespeed.metronome.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.wear.widget.WearableLinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.firespeed.metronome.R
import org.firespeed.metronome.databinding.SettingBpmWearFragmentBinding

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SettingBpmFragment : Fragment() {

    private val viewModel: SettingBpmViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<SettingBpmWearFragmentBinding>(
            inflater,
            R.layout.setting_bpm_wear_fragment,
            container,
            false
        ).also { binding ->
            binding.fragment = this
            binding.lifecycleOwner = this
            binding.viewModel = viewModel
            val adapter = BpmListAdapter(MobileBpmListLayoutResolver()) { event ->
                viewModel.handleListEvent(event)
            }

            val llm = LinearLayoutManager(context)
            binding.bpmList.apply{
                isEdgeItemsCenteringEnabled = true
                layoutManager = WearableLinearLayoutManager(requireContext())
            }

            binding.bpmList.layoutManager = llm
            binding.bpmList.adapter = adapter
            val helper = adapter.createItemTouchHelper()
            helper.attachToRecyclerView(binding.bpmList)
            binding.bpmList.addItemDecoration(helper)
            viewModel.eventFlow.onEach { event ->
                when (event) {
                    is SettingBpmViewModel.Event.StartCreate ->
                        findNavController().navigate(R.id.action_settingBpmFragment_to_editBpmDialogFragment)
                    is SettingBpmViewModel.Event.Selected -> adapter.selectItem(event.bpm)
                    is SettingBpmViewModel.Event.Inserted -> {
                        adapter.addItem(event.bpm)
                        llm.scrollToPosition(0)
                    }
                    is SettingBpmViewModel.Event.StartEdit -> adapter.startEditItem(event.bpm)
                    is SettingBpmViewModel.Event.Edited -> adapter.editedItem(event.bpm)
                    is SettingBpmViewModel.Event.Deleted -> adapter.deleteItem(event.bpm)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
            viewModel.bpmListFlow.onEach {
                adapter.setList(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
            viewModel.getConfig()
        }

        return binding.root
    }

}