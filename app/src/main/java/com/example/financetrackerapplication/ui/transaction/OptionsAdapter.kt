package com.example.financetrackerapplication.ui.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.financetrackerapplication.databinding.ItemTransOptionsBinding
import com.example.financetrackerapplication.domain.model.TransOptions
import com.example.financetrackerapplication.utils.BaseDiffCallback

class OptionsAdapter<T: TransOptions> (
    private val onClickItem: (T) -> Unit
) : ListAdapter<T, OptionsAdapter.ItemViewHolder>(BaseDiffCallback { it.id }) {
    class ItemViewHolder(
        private val binding: ItemTransOptionsBinding
    ) : ViewHolder(binding.root) {
        fun <T: TransOptions> bind (options: T, onClickItem: (T) -> Unit){
            binding.apply {
                tvTransOptions.text = options.name
                imgTransOptions.setImageURI(options.iconName?.toUri())
            }
            itemView.setOnClickListener { onClickItem.invoke(options) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = ItemTransOptionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val aset = getItem(position)
        holder.bind(aset, onClickItem)
    }
}