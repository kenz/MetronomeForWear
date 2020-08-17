package org.firespeed.metronome.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.firespeed.metronome.R
import org.firespeed.metronome.databinding.EditBpmDialogFragmentBinding
import org.firespeed.metronome.model.Bpm


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class EditBpmDialogFragment : DialogFragment() {

    companion object {
        fun newInstance() =
            EditBpmDialogFragment()
    }

    private val viewModel: SettingBpmViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<EditBpmDialogFragmentBinding>(
            inflater,
            R.layout.edit_bpm_dialog_fragment,
            container,
            false
        ).also { binding ->
            binding.numBpm.maxValue = Bpm.MAX_VALUE
            binding.numBpm.minValue = Bpm.MIN_VALUE
            viewModel.editingBpm?.also{
                binding.numBpm.value = it.bpm
                binding.txtTitle.setText(it.title)
            }?:run{
                binding.numBpm.value = Bpm.DEFAULT_VALUE

            }
            binding.submit.setOnClickListener {
                saveBpm(binding.txtTitle.text.toString(), binding.numBpm.value)
                dismissAllowingStateLoss()
            }
        }
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        dialog?.window?.let{
            val attr = it.attributes
            attr.width = ViewGroup.LayoutParams.MATCH_PARENT
            it.attributes = attr as WindowManager.LayoutParams
        }
    }

    private fun saveBpm(title:String, bpm:Int){
        viewModel.editingBpm?.also{
            it.title = title
            it.bpm = bpm
        }?:run{
            viewModel.editingBpm = Bpm(title = title, bpm = bpm)
        }
        viewModel.saveEditingBpm()

    }


}