package org.firespeed.metronome.ui.settings

import android.os.Bundle
import android.provider.Contacts
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
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
        ).also {
            it.fragment = this
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }
        binding.add.setOnClickListener {
            val bpm = Bpm(
                title = binding.title.text.toString(),
                bpm = binding.bpm.text.toString().toInt(),
                order = 0
            )
            viewModel.add(bpm)
        }
        val adapter = BpmListAdapter{
            // onClickListener
            viewModel.selectBpm(it)
        }
        lifecycleScope.launch {
            viewModel.bpmListFlow.collect{
                adapter.setList(it)
            }
        }
        lifecycleScope.launch {
            viewModel.selectedBpmFlow.collect{
                binding.selectedItem = it
            }
        }

        viewModel.getConfig()
        binding.bpmList.layoutManager = LinearLayoutManager(context)
        binding.bpmList.adapter = adapter





        return binding.root
    }


}