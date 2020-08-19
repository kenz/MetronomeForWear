package org.firespeed.metronome.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import org.firespeed.metronome.model.Bpm

class BpmListAdapter(
    private val layoutResolver: LayoutResolver,
    private val event: (Event) -> Unit
) : RecyclerView.Adapter<BpmListAdapter.BindingViewHolder>() {
    private val list = ArrayList<BpmListItem>()


    class BindingViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val binding: ViewDataBinding = DataBindingUtil.bind(v)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder =
        BindingViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(layoutResolver.getLayoutRes(viewType), parent, false)
        )

    override fun getItemViewType(position: Int): Int = list[position].toViewType()
    private fun BpmListItem.toViewType() = when (this) {
        is BpmListItem.AddItem -> VIEW_TYPE_ADD
        else -> VIEW_TYPE_BPM
    }

    override fun getItemCount(): Int = list.count()

    private var selectedItem = -1
    private var editItem: Bpm? = null
    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        when (val item = list[position]) {
            is BpmListItem.BpmItem -> {
                layoutResolver.bindBpmItem(holder.binding, item, event)
                holder.itemView.isSelected = position == selectedItem
                layoutResolver.selectedListener = { bpm ->
                    findByIndex(bpm)?.let {
                        layoutResolver.selectedBpm = item.bpm
                        notifyItemChanged(it)
                    }
                }
            }
            is BpmListItem.AddItem -> {
                layoutResolver.bindAddItem(holder.binding, item, event)
            }
        }
    }

    fun editBpm(bpm: Bpm) {
        editItem = bpm
    }

    fun selectItem(bpm: Bpm) {
        val position = findByIndex(bpm) ?: return
        notifyItemChanged(selectedItem)
        selectedItem = position
        notifyItemChanged(selectedItem)
    }


    fun updateItem(bpm: Bpm) {
        val position = findByIndex(bpm) ?: return
        notifyItemChanged(selectedItem)
        selectedItem = position
        notifyItemChanged(selectedItem)
    }

    fun deleteItem(bpm: Bpm) {
        val position = findByIndex(bpm) ?: return
        notifyItemRemoved(position)
    }

    private fun findByIndex(bpm: Bpm): Int? {
        val selectedBpmItem =
            list.filterIsInstance<BpmListItem.BpmItem>().firstOrNull { it.bpm == bpm } ?: return null
        return list.indexOf(selectedBpmItem)

    }

    fun setList(bpmList: List<Bpm>) {
        list.clear()
        list.add(BpmListItem.AddItem)
        list.addAll(bpmList.map { BpmListItem.BpmItem(it) })
        notifyDataSetChanged()
    }

    fun addBpm(bpm: Bpm) {
        list.add(1, BpmListItem.BpmItem(bpm))
        notifyItemInserted(1)
        selectedItem++
    }

    private fun getBpm(position: Int): Bpm? {
        val item = list[position]
        return if (item is BpmListItem.BpmItem) item.bpm else null
    }

    fun createItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(object : Callback() {
            override fun getMovementFlags(rv: RecyclerView, vh: RecyclerView.ViewHolder): Int =
                makeFlag(ACTION_STATE_IDLE, RIGHT) or
                        makeFlag(ACTION_STATE_SWIPE, LEFT or RIGHT) or
                        makeFlag(ACTION_STATE_DRAG, DOWN or UP)

            //ドラッグで場所を移動
            override fun onMove(
                rv: RecyclerView,
                fromVh: RecyclerView.ViewHolder,
                toVh: RecyclerView.ViewHolder
            ): Boolean {
                val from = fromVh.adapterPosition
                val to = toVh.adapterPosition
                val fromBpm = getBpm(from)
                val toBpm = getBpm(to)
                if (fromBpm != null && toBpm != null) {
                    event.invoke(Event.Switch(fromBpm, toBpm))
                }
                return true
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState == ACTION_STATE_DRAG)
                    viewHolder?.itemView?.animate()?.translationZ(8f)
                else if (actionState == ACTION_STATE_SWIPE)
                    viewHolder?.itemView?.animate()?.alpha(0.5f)
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.animate().translationZ(0f)
                viewHolder.itemView.animate().alpha(1f)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                val from = viewHolder.adapterPosition
                val item = list[from]
                if (item is BpmListItem.BpmItem) {
                    event.invoke(Event.Delete(item.bpm))
                }
            }
        })
    }

    sealed class Event {
        object StartCreate : Event()
        class StartEdit(val bpmItem: Bpm) : Event()
        class Edited(val bpmItem: Bpm) : Event()
        class Select(val bpmItem: Bpm) : Event()
        class Delete(val bpmItem: Bpm) : Event()
        class Switch(val bpmItem0: Bpm, val bpmItem1: Bpm) : Event()
    }

    companion object {
        const val VIEW_TYPE_ADD = 1
        const val VIEW_TYPE_BPM = 2
    }
}

