package com.example.financetrackerapplication.features.transaction.repeat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.financetrackerapplication.databinding.ItemAddRepeatBinding
import com.example.financetrackerapplication.databinding.ItemTransOptionsBinding
import com.example.financetrackerapplication.domain.model.TransOptions
import com.example.financetrackerapplication.utils.BaseDiffCallback

class RepeatAdapter (
    private val onClickItem: (String) -> Unit
) : ListAdapter<String, RepeatAdapter.ItemViewHolder>(BaseDiffCallback { it }) {
    class ItemViewHolder(
        private val binding: ItemAddRepeatBinding
    ) : ViewHolder(binding.root) {
        fun bind (options: String, onClickItem: (String) -> Unit){
            binding.apply {
                repeatTvName.text = options
            }
            itemView.setOnClickListener { onClickItem.invoke(options) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = ItemAddRepeatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val aset = getItem(position)
        holder.bind(aset, onClickItem)
    }
}