package org.firespeed.metronome.ui.settings

import org.firespeed.metronome.model.Bpm

sealed class BpmListItem {
    object AddItem : BpmListItem()
    class BpmItem(var bpm: Bpm, var selected:Boolean = false, var editing:Boolean = false) : BpmListItem()
    object BottomItem : BpmListItem()
}


