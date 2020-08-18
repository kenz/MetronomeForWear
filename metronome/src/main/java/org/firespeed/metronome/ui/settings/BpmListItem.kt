package org.firespeed.metronome.ui.settings

import org.firespeed.metronome.model.Bpm

sealed class BpmListItem


object AddItem : BpmListItem()

class BpmItem(val bpm: Bpm) : BpmListItem()

