package org.firespeed.metronome.ui.settings

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.firespeed.metronome.R
import org.firespeed.metronome.databinding.EditBpmDialogWearFragmentBinding
import org.firespeed.metronome.model.Bpm


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class EditBpmDialogFragment : DialogFragment() {

    private val viewModel: SettingBpmViewModel by activityViewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {activity ->
            val builder = AlertDialog.Builder(activity)
            val binding = DataBindingUtil.inflate<EditBpmDialogWearFragmentBinding>(LayoutInflater.from(activity), R.layout.edit_bpm_dialog_wear_fragment, null, false).also{ binding ->
                binding.numBpm.maxValue = Bpm.MAX_VALUE
                binding.numBpm.minValue = Bpm.MIN_VALUE
                viewModel.editingBpm?.also{
                    binding.numBpm.value = it.bpm
                    binding.txtTitle.setText(it.title)
                }?:run{
                    binding.numBpm.value = Bpm.DEFAULT_VALUE
                    binding.txtTitle.setText("")
                }
            }
            builder.setView(binding.root)
                .setPositiveButton(R.string.submit) { _, _ ->
                    binding.txtTitle.requestFocus()
                    saveBpm(binding.txtTitle.text.toString(), binding.numBpm.value)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
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