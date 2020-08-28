package org.firespeed.metronome.ui.settings

import androidx.databinding.ViewDataBinding

interface LayoutResolver {
    fun getLayoutRes(viewType: Int): Int
    fun bindBpmItem(
        binding: ViewDataBinding,
        bpmItem: BpmListItem.BpmItem,
        event: (BpmListAdapter.Event)->Unit
    )
    fun bindAddItem(
        binding: ViewDataBinding,
        addItem: BpmListItem.AddItem,
        event: (BpmListAdapter.Event)->Unit
    )
    fun bindBottomItem(
        binding: ViewDataBinding,
        bottomItem: BpmListItem.BottomItem,
        event: (BpmListAdapter.Event)->Unit
    )
}

