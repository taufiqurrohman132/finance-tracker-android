package com.example.financetrackerapplication.ui.dashboard

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.databinding.ItemTransactionBinding

class DashboardAdapter(

) : ListAdapter<TransactionEntity, DashboardAdapter.ItemViewHolder>(DIFF_CALLBACK) {
    inner class ItemViewHolder(private val binding: ItemTransactionBinding) : ViewHolder(binding.root) {
        fun bind(transactionEntity: TransactionEntity){
            binding.apply {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    companion object{
        private val DIFF_CALLBACK = object : ItemCallback<TransactionEntity>() {
            override fun areItemsTheSame(
                oldItem: TransactionEntity,
                newItem: TransactionEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: TransactionEntity,
                newItem: TransactionEntity
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}