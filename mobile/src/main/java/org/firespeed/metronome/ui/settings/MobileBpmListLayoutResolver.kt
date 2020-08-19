package org.firespeed.metronome.ui.settings

import android.view.View
import androidx.databinding.ViewDataBinding
import org.firespeed.metronome.R
import org.firespeed.metronome.databinding.ItemAddBinding
import org.firespeed.metronome.databinding.ItemBpmBinding
import org.firespeed.metronome.model.Bpm

class MobileBpmListLayoutResolver : LayoutResolver {
    override fun getLayoutRes(viewType: Int): Int =
        when (viewType) {
            BpmListAdapter.VIEW_TYPE_ADD -> R.layout.item_add
            else -> R.layout.item_bpm
        }


    override fun bindBpmItem(
        binding: ViewDataBinding,
        bpmItem: BpmListItem.BpmItem,
        event: (BpmListAdapter.Event) -> Unit
    ) {
        (binding as ItemBpmBinding).content = bpmItem
        binding.root.setOnClickListener {
            event.invoke(BpmListAdapter.Event.Select(bpmItem.bpm))
            selectedListener?.invoke(bpmItem.bpm)
        }
        binding.root.setOnLongClickListener {
            event.invoke(BpmListAdapter.Event.StartEdit(bpmItem.bpm))
            true
        }
        if (bpmItem.bpm == selectedBpm) {
            binding.lblBpm.visibility = View.GONE
            binding.numBpm.visibility = View.VISIBLE
            binding.btnDelete.visibility = View.VISIBLE
            binding.title.isEnabled = true
        } else {
            binding.lblBpm.visibility = View.VISIBLE
            binding.numBpm.visibility = View.GONE
            binding.btnDelete.visibility = View.GONE
            binding.title.isEnabled = false
        }
    }

    override fun bindAddItem(
        binding: ViewDataBinding,
        addItem: BpmListItem.AddItem,
        event: (BpmListAdapter.Event) -> Unit
    ) {
        (binding as ItemAddBinding).content = addItem
        binding.root.setOnClickListener {
            event.invoke(BpmListAdapter.Event.StartCreate)
        }
    }

    override var selectedListener: ((bpm: Bpm) -> Unit)? = null
    override var selectedBpm: Bpm? = null
}
