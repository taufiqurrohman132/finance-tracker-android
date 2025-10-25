package com.example.financetrackerapplication.features.aset.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.databinding.ItemAsetBinding
import com.example.financetrackerapplication.utils.BaseDiffCallback

class AsetAdapter(
    private val onClickItem: (AsetEntity) -> Unit
) : ListAdapter<AsetEntity, AsetAdapter.ItemViewHolder>(BaseDiffCallback { it.id }) {
    class ItemViewHolder(
        private val binding: ItemAsetBinding
    ) : ViewHolder(binding.root) {
        fun bind(aset: AsetEntity){
            binding.apply {
                tvAsetNameItem.text = aset.name
                tvJumlahAsetItem.text = aset.initialBalance.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = ItemAsetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val aset = getItem(position)
        holder.bind(aset)
    }
}