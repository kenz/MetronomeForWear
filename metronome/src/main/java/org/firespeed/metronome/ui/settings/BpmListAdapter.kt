package org.firespeed.metronome.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

import org.firespeed.metronome.model.Bpm
import javax.inject.Inject


class BpmListAdapter(
    private val itemInteractListener: ItemInteractListener,
    private val layoutResolver: LayoutResolver
) : RecyclerView.Adapter<BpmListAdapter.BindingViewHolder>() {
    private val list = ArrayList<BpmListItem>()

    interface LayoutResolver {
        fun getLayoutRes(viewType: Int): Int
        fun bindBpmItem(
            binding: ViewDataBinding,
            bpmItem: BpmItem,
            itemInteractListener: ItemInteractListener
        )

        fun bindAddItem(
            binding: ViewDataBinding,
            addItem: AddItem,
            itemInteractListener: ItemInteractListener
        )
    }

    interface ItemInteractListener {
        fun onAddClickListener(): Unit
        fun editBpmListener(bpm: Bpm): Unit
        fun deleteBpmListener(bpm: Bpm): Unit
        fun selectBpmListener(bpm: Bpm): Unit
    }

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
        is AddItem -> org.firespeed.metronome.ui.settings.BpmListAdapter.VIEW_TYPE_ADD
        else -> org.firespeed.metronome.ui.settings.BpmListAdapter.VIEW_TYPE_BPM
    }

    override fun getItemCount(): Int = list.count()

    private var selectedItem = -1
    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        when (val item = list[position]) {
            is BpmItem -> {
                layoutResolver.bindBpmItem(holder.binding, item, itemInteractListener)
                holder.itemView.isSelected = position == selectedItem
            }
            is AddItem -> {
                layoutResolver.bindAddItem(holder.binding, item, itemInteractListener)
            }
        }
    }

    fun selectItem(bpm: Bpm) {
        val selectedBpmItem =
            list.filterIsInstance<BpmItem>().firstOrNull { it.bpm == bpm } ?: return
        val position = list.indexOf(selectedBpmItem)
        notifyItemChanged(selectedItem)
        selectedItem = position
        notifyItemChanged(selectedItem)
    }

    fun setList(bpmList: List<Bpm>) {
        list.clear()
        list.add(AddItem)
        list.addAll(bpmList.map { BpmItem(it) })
        notifyDataSetChanged()
    }

    fun addBpm(bpm: Bpm) {
        list.add(1, BpmItem(bpm))
        notifyItemInserted(1)
        selectedItem++
    }

    companion object {
        const val VIEW_TYPE_ADD = 1
        const val VIEW_TYPE_BPM = 2
    }
}
