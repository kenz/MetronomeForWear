package org.firespeed.metronome.ui.settings

import android.view.View
import androidx.databinding.ViewDataBinding
import org.firespeed.metronome.R
import org.firespeed.metronome.databinding.AddItemMobileBinding
import org.firespeed.metronome.databinding.BottomItemMobileBinding
import org.firespeed.metronome.databinding.BpmItemMobileBinding
import org.firespeed.metronome.model.Bpm

class MobileBpmListLayoutResolver : LayoutResolver {
    override fun getLayoutRes(viewType: Int): Int =
        when (viewType) {
            BpmListAdapter.VIEW_TYPE_BPM -> R.layout.bpm_item_mobile
            BpmListAdapter.VIEW_TYPE_ADD -> R.layout.add_item_mobile
            else -> R.layout.bottom_item_mobile
        }


    override fun bindBpmItem(
        binding: ViewDataBinding,
        bpmItem: BpmListItem.BpmItem,
        event: (BpmListAdapter.Event) -> Unit
    ) {
        (binding as BpmItemMobileBinding).content = bpmItem
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
        } else {
            binding.lblBpm.visibility = View.VISIBLE
            binding.numBpm.visibility = View.GONE
            binding.btnDelete.visibility = View.GONE
            binding.title.visibility = View.VISIBLE
            binding.editLayoutTitle.visibility = View.GONE
        }
    }

    override fun bindAddItem(
        binding: ViewDataBinding,
        addItem: BpmListItem.AddItem,
        event: (BpmListAdapter.Event) -> Unit
    ) {
        (binding as AddItemMobileBinding).content = addItem
        binding.root.setOnClickListener {
            event.invoke(BpmListAdapter.Event.StartCreate)
        }
    }

    override fun bindBottomItem(
        binding: ViewDataBinding,
        bottomItem: BpmListItem.BottomItem,
        event: (BpmListAdapter.Event) -> Unit
    ) {
        (binding as BottomItemMobileBinding).content = bottomItem
    }
}
