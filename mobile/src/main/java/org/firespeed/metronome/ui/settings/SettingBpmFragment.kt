package org.firespeed.metronome.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
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

    companion object {
        fun newInstance() = SettingBpmFragment()
    }

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


            val adapter = BpmListAdapter(object : BpmListAdapter.ItemInteractListener {
                override fun onAddClickListener() {
                    viewModel.editingBpm = null
                    findNavController().navigate(
                        SettingBpmFragmentDirections.actionSettingBpmFragmentToEditBpmDialogFragment(
                            null
                        )
                    )
                }


                override fun editBpmListener(bpm: Bpm) {
                    viewModel.editingBpm = bpm
                    findNavController().navigate(
                        SettingBpmFragmentDirections.actionSettingBpmFragmentToEditBpmDialogFragment(
                            bpm
                        )
                    )

                }

                override fun deleteBpmListener(bpm: Bpm) {
                }

                override fun selectBpmListener(bpm: Bpm) = viewModel.selectBpm(bpm)


            }, MobileBpmListLayoutResolver())

            val llm = LinearLayoutManager(context)
            binding.bpmList.layoutManager = llm
            viewModel.insertedBpmFlow.onEach {
                adapter.addBpm(it)
                llm.scrollToPosition(0)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
            viewModel.getConfig()
            binding.bpmList.adapter = adapter
            viewModel.bpmListFlow.onEach {
                adapter.setList(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
            viewModel.selectedBpmFlow.onEach {
                adapter.selectItem(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
            viewModel.deletedBpmFlow.onEach {
                adapter.deleteItem(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }

        return binding.root
    }


}