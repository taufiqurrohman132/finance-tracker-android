package com.example.financetrackerapplication.features.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.databinding.ItemAsetBinding
import com.example.financetrackerapplication.databinding.ItemListAsetOptionsBinding
import com.example.financetrackerapplication.utils.BaseDiffCallback

class AsetOptionsAdapter(
    private val onClickItem: (AsetEntity) -> Unit
) : ListAdapter<AsetEntity, AsetOptionsAdapter.ItemViewHolder>(BaseDiffCallback { it.id }) {
    class ItemViewHolder(
        private val binding: ItemListAsetOptionsBinding
    ) : ViewHolder(binding.root) {
        fun bind(aset: AsetEntity){
            binding.apply {
                tvAsetOptions.text = aset.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = ItemListAsetOptionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val aset = getItem(position)
        holder.bind(aset)
    }
}