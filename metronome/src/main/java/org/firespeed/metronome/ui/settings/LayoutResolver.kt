package org.firespeed.metronome.ui.settings

import androidx.databinding.ViewDataBinding
import org.firespeed.metronome.model.Bpm

interface LayoutResolver {
    fun getLayoutRes(viewType: Int): Int
    fun bindBpmItem(
        binding: ViewDataBinding,
        bpmItem: BpmListItem.BpmItem,
        editing: Boolean,
        event: (BpmListAdapter.Event)->Unit
    )

    fun bindAddItem(
        binding: ViewDataBinding,
        addItem: BpmListItem.AddItem,
        event: (BpmListAdapter.Event)->Unit
    )
}

