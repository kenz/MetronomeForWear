package org.firespeed.metronome.ui.settings

import androidx.databinding.ViewDataBinding
import org.firespeed.metronome.R
import org.firespeed.metronome.databinding.ItemAddBinding
import org.firespeed.metronome.databinding.ItemBpmBinding

class MobileBpmListLayoutResolver : BpmListAdapter.LayoutResolver {
    override fun getLayoutRes(viewType: Int): Int =
        when (viewType) {
            BpmListAdapter.VIEW_TYPE_ADD -> R.layout.item_add
            else -> R.layout.item_bpm
        }


    override fun bindBpmItem(
        binding: ViewDataBinding,
        bpmItem: BpmItem,
        itemInteractListener: BpmListAdapter.ItemInteractListener
    ) {
        (binding as ItemBpmBinding).content = bpmItem
        binding.root.setOnClickListener {
            itemInteractListener.selectBpmListener(bpmItem.bpm)
        }
        binding.root.setOnLongClickListener {
            itemInteractListener.editBpmListener(bpmItem.bpm)
            true
        }
    }

    override fun bindAddItem(
        binding: ViewDataBinding,
        addItem: AddItem,
        itemInteractListener: BpmListAdapter.ItemInteractListener
    ) {
        (binding as ItemAddBinding).content = addItem
        binding.root.setOnClickListener {
            itemInteractListener.onAddClickListener()
        }
    }
}
