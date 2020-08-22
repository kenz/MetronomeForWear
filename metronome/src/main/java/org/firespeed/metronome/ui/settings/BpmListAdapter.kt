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
        is BpmListItem.AddItem -> VIEW_TYPE_ADD
        else -> VIEW_TYPE_BPM
    }

    override fun getItemCount(): Int = list.count()

    private var selectedItemIndex: Int? = null
    private var editingItemIndex: Int? = null
    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        when (val item = list[position]) {
            is BpmListItem.BpmItem -> {
                layoutResolver.bindBpmItem(
                    holder.binding,
                    item,
                    editingItemIndex == position,
                    event
                )
                holder.itemView.isSelected = position == selectedItemIndex
            }
            is BpmListItem.AddItem -> {
                layoutResolver.bindAddItem(holder.binding, item, event)
            }
        }
    }

    fun editItem(bpm: Bpm) {
        editingItemIndex = findByIndex(bpm)?.also {
            (list[it] as BpmListItem.BpmItem).bpm = bpm
            notifyItemChanged(it)
        }
    }


    fun deleteItem(bpm: Bpm) {
        val position = findByIndex(bpm) ?: return

        selectedItemIndex?.let {
            if (position < it) {
                this.selectedItemIndex = (selectedItemIndex ?: 0) - 1
            }
        }
        editingItemIndex?.let {
            if (position < it) {
                this.editingItemIndex = (editingItemIndex ?: 0) - 1
            }
        }
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun findByIndex(bpm: Bpm?): Int? {
        if (bpm == null) return null
        val selectedBpmItem =
            list.filterIsInstance<BpmListItem.BpmItem>().firstOrNull { it.bpm == bpm }
                ?: return null
        return list.indexOf(selectedBpmItem)

    }

    fun setList(bpmList: List<Bpm>) {
        list.clear()
        list.add(BpmListItem.AddItem)
        list.addAll(bpmList.map { BpmListItem.BpmItem(it) })
        notifyDataSetChanged()
    }

    fun addItem(bpm: Bpm) {
        list.add(1, BpmListItem.BpmItem(bpm))
        notifyItemInserted(1)
        selectedItemIndex?.let {
            this.selectedItemIndex = selectedItemIndex ?: 0 + 1
        }
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
                val fromBpm = getBpm(fromPos)
                val toBpm = getBpm(toPos)
                if (editingItemIndex == fromPos) {
                    editingItemIndex = toPos
                } else if (editingItemIndex == toPos) {
                    editingItemIndex = fromPos
                }
                if (selectedItemIndex == fromPos) {
                    selectedItemIndex = toPos
                } else if (selectedItemIndex == toPos) {
                    selectedItemIndex = toPos
                }
                list.switch(fromPos, toPos)
                if (fromBpm != null && toBpm != null) {
                    event.invoke(Event.Switch(fromBpm, toBpm))
                }
            }

            //ドラッグで場所を移動
            override fun onMove(
                rv: RecyclerView,
                fromVh: RecyclerView.ViewHolder,
                toVh: RecyclerView.ViewHolder
            ): Boolean {
                notifyItemMoved(fromVh.adapterPosition, toVh.adapterPosition)
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

    fun startEditItem(bpm: Bpm) {
        // 以前に編集中のアイテムがあれば場所を記録
        val preEditItemIndex = editingItemIndex
        val newEditItemIndex = findByIndex(bpm) ?: return
        // 編集中のアイテムを更新
        editingItemIndex = newEditItemIndex
        // 以前に編集中のアイテムがあれば編集状態じゃなくする
        preEditItemIndex?.let { notifyItemChanged(it) }
        // 新たに編集対象とされたアイテムを編集状態とする
        notifyItemChanged(newEditItemIndex)
    }

    fun selectItem(bpm: Bpm) {
        val preSelectedItemIndex = selectedItemIndex
        val newSelectedItemIndex = findByIndex(bpm) ?: return
        selectedItemIndex = newSelectedItemIndex
        preSelectedItemIndex?.let { notifyItemChanged(it) }
        notifyItemChanged(newSelectedItemIndex)
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

