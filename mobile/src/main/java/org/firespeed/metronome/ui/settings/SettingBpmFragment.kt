package org.firespeed.metronome.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.firespeed.metronome.R
import org.firespeed.metronome.databinding.SettingBpmFragmentBinding
import org.firespeed.metronome.model.Bpm
import org.firespeed.metronome.ui.SettingBpmViewModel

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
            binding.selectedItem = it
        }
        binding.bpmList.layoutManager = LinearLayoutManager(context)
        binding.bpmList.adapter = adapter
        viewModel.getAll().observe(this.viewLifecycleOwner, Observer {
            adapter.setList(it)
        })

        return binding.root
    }


}