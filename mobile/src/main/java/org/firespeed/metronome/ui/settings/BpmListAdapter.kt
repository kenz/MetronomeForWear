package org.firespeed.metronome.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.firespeed.metronome.R
import org.firespeed.metronome.databinding.ItemBpmBinding
import org.firespeed.metronome.model.Bpm


class BpmListAdapter(
    private val clickListener: (Bpm) -> Unit
) : RecyclerView.Adapter<BpmListAdapter.ItemViewHolder>() {
    private val list = ArrayList<Bpm>()

    class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val binding: ItemBpmBinding? = DataBindingUtil.bind(v)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bpm, parent, false))
    }

    override fun getItemCount(): Int = list.count()

    private var selectedItem = -1
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding?.apply {
            val item = list[position]
            this.item = item
            this.root.setOnClickListener{
                clickListener.invoke(item)
                notifyItemChanged(selectedItem)
                selectedItem = position
                notifyItemChanged(selectedItem)
            }
            holder.itemView.isSelected = position == selectedItem
        }

    }

    fun setList(bpmList: List<Bpm>) {
        list.clear()
        list.addAll(bpmList)
        notifyDataSetChanged()
    }

}
