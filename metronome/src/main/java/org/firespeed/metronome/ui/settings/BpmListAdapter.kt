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
import org.firespeed.metronome.switch

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
        is BpmListItem.BpmItem -> VIEW_TYPE_BPM
        is BpmListItem.AddItem -> VIEW_TYPE_ADD
        else -> VIEW_TYPE_BOTTOM
    }

    override fun getItemCount(): Int = list.count()

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        when (val item = list[position]) {
            is BpmListItem.BpmItem -> {
                layoutResolver.bindBpmItem(
                    holder.binding,
                    item,
                    event
                )
                holder.itemView.isSelected = item.selected
            }
            is BpmListItem.AddItem ->
                layoutResolver.bindAddItem(holder.binding, item, event)
            is BpmListItem.BottomItem ->
                layoutResolver.bindBottomItem(holder.binding, item, event)
        }
    }


    fun editedItem(bpm: Bpm) {
        findByItem(bpm)?.let {
            it.editing = false
            notifyItemChanged(list.indexOf(it))
        }
    }


    fun deleteItem(bpm: Bpm) {
        val deleteItem = findByItem(bpm) ?: return
        val position = list.indexOf(deleteItem)
        list.removeAt(position)
        notifyItemRemoved(position)
    }


    private fun findByItem(bpm: Bpm): BpmListItem.BpmItem? =
        list.filterIsInstance<BpmListItem.BpmItem>().firstOrNull { it.bpm == bpm }

    private fun findBySelectedItem(): BpmListItem.BpmItem? =
        list.filterIsInstance<BpmListItem.BpmItem>().firstOrNull { it.selected }

    private fun findByEditedItem(): BpmListItem.BpmItem? =
        list.filterIsInstance<BpmListItem.BpmItem>().firstOrNull { it.editing }

    fun setList(bpmList: List<Bpm>) {
        list.clear()
        list.add(BpmListItem.AddItem)
        list.addAll(bpmList.map { BpmListItem.BpmItem(it) })
        list.add(BpmListItem.BottomItem)
        notifyDataSetChanged()
    }

    fun addItem(bpm: Bpm) {
        list.add(1, BpmListItem.BpmItem(bpm))
        notifyItemInserted(1)
    }

    private fun getBpm(position: Int): Bpm? {
        val item = list[position]
        return if (item is BpmListItem.BpmItem) item.bpm else null
    }

    fun createItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(object : Callback() {
            override fun getMovementFlags(rv: RecyclerView, vh: RecyclerView.ViewHolder): Int =
                if (vh.itemViewType == VIEW_TYPE_BPM)
                    makeFlag(ACTION_STATE_IDLE, LEFT) or
                            makeFlag(ACTION_STATE_SWIPE, LEFT) or
                            makeFlag(ACTION_STATE_DRAG, DOWN or UP)
                else
                    0

            override fun onMoved(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                fromPos: Int,
                target: RecyclerView.ViewHolder,
                toPos: Int,
                x: Int,
                y: Int
            ) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
                val fromBpm = getBpm(fromPos) ?: return
                val toBpm = getBpm(toPos) ?: return
                list.switch(fromPos, toPos)
                event.invoke(Event.Switch(fromBpm, toBpm))
            }

            //ドラッグで場所を移動
            override fun onMove(
                rv: RecyclerView,
                fromVh: RecyclerView.ViewHolder,
                toVh: RecyclerView.ViewHolder
            ): Boolean =
                if (fromVh.itemViewType == VIEW_TYPE_BPM && toVh.itemViewType == VIEW_TYPE_BPM) {
                    notifyItemMoved(fromVh.adapterPosition, toVh.adapterPosition)
                    true
                } else
                    false

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

    fun startEditItem(bpm: Bpm) {
        // 以前に編集中のアイテムがあれば場所を記録
        val preEditItem = findByEditedItem()
        if (bpm != preEditItem?.bpm) {
            preEditItem?.let {
                preEditItem.editing = false
                val preEditIndex = list.indexOf(preEditItem)
                notifyItemChanged(preEditIndex)
            }
            val editItem = findByItem(bpm) ?: return
            editItem.editing = true
            val editIndex = list.indexOf(editItem)
            notifyItemChanged(editIndex)
        }
    }

    fun selectItem(bpm: Bpm) {
        val preSelectedItem = findBySelectedItem()
        val selectedItem = findByItem(bpm)
        if (preSelectedItem == selectedItem) return

        preSelectedItem?.let {
            it.selected = false
            notifyItemChanged(list.indexOf(it))
        }
        selectedItem?.let {
            it.selected = true
            notifyItemChanged(list.indexOf(it))
        }
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
        const val VIEW_TYPE_BOTTOM = 3
    }
}

