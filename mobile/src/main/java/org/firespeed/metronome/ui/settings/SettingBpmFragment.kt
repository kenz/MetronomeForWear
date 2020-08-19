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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.firespeed.metronome.R
import org.firespeed.metronome.databinding.SettingBpmFragmentBinding
import org.firespeed.metronome.model.Bpm

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SettingBpmFragment : Fragment() {

    private val viewModel: SettingBpmViewModel by activityViewModels()

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
            val adapter = BpmListAdapter(MobileBpmListLayoutResolver()){event -> viewModel.handleListEvent(event)}

            val llm = LinearLayoutManager(context)
            binding.bpmList.layoutManager = llm
            binding.bpmList.adapter = adapter
            val helper = adapter.createItemTouchHelper()
            helper.attachToRecyclerView(binding.bpmList)
            binding.bpmList.addItemDecoration(helper)
            viewModel.eventFlow.onEach { event ->
                when(event){
                    is SettingBpmViewModel.Event.StartCreate ->
                    is SettingBpmViewModel.Event.Selected -> TODO()
                    is SettingBpmViewModel.Event.Inserted -> TODO()
                    is SettingBpmViewModel.Event.StartEdit -> TODO()
                    is SettingBpmViewModel.Event.Edited -> TODO()
                    is SettingBpmViewModel.Event.Deleted -> TODO()
                    is SettingBpmViewModel.Event.Switched -> TODO()
                }

            }
            viewModel.insertedBpmFlow.onEach {
                adapter.addBpm(it)
                llm.scrollToPosition(0)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
            viewModel.bpmListFlow.onEach {
                adapter.setList(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
            viewModel.selectedBpmFlow.onEach {
                adapter.selectItem(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
            viewModel.deletedBpmFlow.onEach {
                adapter.deleteItem(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
            viewModel.getConfig()

        }

        return binding.root
    }

}