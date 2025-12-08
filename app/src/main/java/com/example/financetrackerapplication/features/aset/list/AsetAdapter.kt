package com.example.financetrackerapplication.features.aset.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.financetrackerapplication.databinding.ItemAsetBinding
import com.example.financetrackerapplication.databinding.ItemAsetHeaderBinding
import com.example.financetrackerapplication.domain.model.ItemAset2
import com.example.financetrackerapplication.utils.BaseDiffCallback
import com.example.financetrackerapplication.utils.Extention.parseLongToMoney

class AsetAdapter(
    private val onClickItemBody: (ItemAset2) -> Unit
) : ListAdapter<ItemAset2, ViewHolder>(BaseDiffCallback { it }) {
    private lateinit var onClickItemHeader: (ItemAset2, String) -> Unit

    inner class ItemHeaderViewHolder(private val binding: ItemAsetHeaderBinding) :
        ViewHolder(binding.root) {
        fun bind(itemAset: ItemAset2) {
            binding.apply {
                asetGroupName.text = itemAset.groupAset.toString()
                asetTotal.text = itemAset.total?.parseLongToMoney()
            }

            itemView.setOnClickListener {
                toggleGroup(itemAset.groupAset.toString())
            }
        }
    }

    inner class ItemBodyViewHolder(private val binding: ItemAsetBinding) :
        ViewHolder(binding.root) {
        fun bind(itemAset: ItemAset2) {
            binding.apply {
                tvAsetNameItem.text = itemAset.name.toString()
                tvJumlahAsetItem.text = itemAset.initialBalance?.parseLongToMoney()
            }

            itemView.setOnClickListener {
                onClickItemBody.invoke(itemAset)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ITEM_HEADER -> {
                val view = ItemAsetHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ItemHeaderViewHolder(view)
            }

            else -> {
                val view = ItemAsetBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ItemBodyViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is ItemHeaderViewHolder -> holder.bind(item)
            is ItemBodyViewHolder -> holder.bind(item)
        }
    }

    private fun toggleGroup(groupName: String){
        val newList = currentList.map { item ->
            if (item.groupAset == groupName){
                item.copy(isVisible = !item.isVisible)
            } else item
        }
        submitList(newList)
    }

    companion object {
        private const val ITEM_HEADER = 0
        private const val ITEM_BODY = 1
    }
}