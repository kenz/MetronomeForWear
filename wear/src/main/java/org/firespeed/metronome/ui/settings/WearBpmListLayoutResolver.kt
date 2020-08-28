package org.firespeed.metronome.ui.settings

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.databinding.ViewDataBinding
import org.firespeed.metronome.R
import org.firespeed.metronome.databinding.AddItemWearBinding
import org.firespeed.metronome.databinding.BottomItemWearBinding
import org.firespeed.metronome.databinding.BpmItemWearBinding
import org.firespeed.metronome.model.Bpm

class MobileBpmListLayoutResolver : LayoutResolver {
    override fun getLayoutRes(viewType: Int): Int =
        when (viewType) {
            BpmListAdapter.VIEW_TYPE_BPM -> R.layout.bpm_item_wear
            BpmListAdapter.VIEW_TYPE_ADD -> R.layout.add_item_wear
            else -> R.layout.bottom_item_wear
        }


    override fun bindBpmItem(
        binding: ViewDataBinding,
        bpmItem: BpmListItem.BpmItem,
        event: (BpmListAdapter.Event) -> Unit
    ) {
        (binding as BpmItemWearBinding).content = bpmItem
        binding.numBpm.minValue = Bpm.MIN_VALUE
        binding.numBpm.maxValue = Bpm.MAX_VALUE
        binding.numBpm.value = bpmItem.bpm.bpm

        binding.root.setOnClickListener {
            event.invoke(BpmListAdapter.Event.Select(bpmItem.bpm))
        }
        binding.title.setOnClickListener {
            if(!bpmItem.editing)
                event.invoke(BpmListAdapter.Event.StartEdit(bpmItem.bpm))
        }
        binding.editTitle.setOnFocusChangeListener { _, b ->
            if (bpmItem.editing && !b) {
                if(bpmItem.bpm.title != binding.editTitle.text.toString()) {
                    bpmItem.bpm.title = binding.editTitle.text.toString()
                    event.invoke(BpmListAdapter.Event.Edited(bpmItem.bpm))
                }
            }
        }
        binding.editTitle.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.numBpm.setOnValueChangedListener { _, _, newValue ->
            if(bpmItem.bpm.bpm != newValue) {
                bpmItem.bpm.bpm = newValue
                event.invoke(BpmListAdapter.Event.Edited(bpmItem.bpm))
            }
        }
        if (bpmItem.editing) {
            binding.lblBpm.visibility = View.GONE
            binding.numBpm.visibility = View.VISIBLE
            binding.btnDelete.visibility = View.VISIBLE
            binding.title.visibility = View.GONE
            binding.editLayoutTitle.visibility = View.VISIBLE
            binding.background.translationZ = 16f
        } else {
            binding.lblBpm.visibility = View.VISIBLE
            binding.numBpm.visibility = View.GONE
            binding.btnDelete.visibility = View.GONE
            binding.title.visibility = View.VISIBLE
            binding.editLayoutTitle.visibility = View.GONE
            if(bpmItem.selected){
                binding.background.translationZ = 8f
            }else {
                binding.background.translationZ = 0f
            }
        }
    }

    override fun bindAddItem(
        binding: ViewDataBinding,
        addItem: BpmListItem.AddItem,
        event: (BpmListAdapter.Event) -> Unit
    ) {
        (binding as AddItemWearBinding).content = addItem
        binding.root.setOnClickListener {
            event.invoke(BpmListAdapter.Event.StartCreate)
        }
    }

    override fun bindBottomItem(
        binding: ViewDataBinding,
        bottomItem: BpmListItem.BottomItem,
        event: (BpmListAdapter.Event) -> Unit
    ) {
        (binding as BottomItemWearBinding).content = bottomItem
    }
}
