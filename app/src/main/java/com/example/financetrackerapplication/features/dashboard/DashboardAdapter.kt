package com.example.financetrackerapplication.features.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.financetrackerapplication.databinding.ItemTransactionBinding
import com.example.financetrackerapplication.databinding.ItemTransactionHeaderBinding
import com.example.financetrackerapplication.domain.model.ItemTransaction
import com.example.financetrackerapplication.utils.BaseDiffCallback
import com.example.financetrackerapplication.utils.Extention.parseLongToMoney
import com.example.financetrackerapplication.utils.TimeUtils

class DashboardAdapter(
    private val onClickItemHeader: (ItemTransaction) -> Unit,
    private val onClickItemBody: (ItemTransaction) -> Unit
) : ListAdapter<ItemTransaction, ViewHolder>(BaseDiffCallback { it }) {
    inner class ItemHeaderViewHolder(private val binding: ItemTransactionHeaderBinding) :
        ViewHolder(binding.root) {
        fun bind(itemTransaction: ItemTransaction) {
            binding.apply {
                itemTransaction.date?.let {
                    itemTransHeaderTvDate.text = TimeUtils.getStyleDate(it)
                }
                itemTransHeaderTvDay.text = itemTransaction.day
                itemTransHeaderTvIncome.text = itemTransaction.income.toString().toLong().parseLongToMoney()
                itemTransHeaderTvExpanse.text = itemTransaction.expense.toString().toLong().parseLongToMoney()
            }

            itemView.setOnClickListener {
                onClickItemHeader.invoke(itemTransaction)
            }
        }
    }

    inner class ItemBodyViewHolder(private val binding: ItemTransactionBinding) :
        ViewHolder(binding.root) {
        fun bind(itemTransaction: ItemTransaction) {
            binding.apply {
                tvKategoriItem.text = itemTransaction.category
                tvCatatanItem.text = itemTransaction.catatan
                tvAsetItem.text = itemTransaction.aset
                tvJumlahItem.text = itemTransaction.amount.toString().toLong().parseLongToMoney()
            }

            itemView.setOnClickListener {
                onClickItemBody.invoke(itemTransaction)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ITEM_HEADER -> {
                val view = ItemTransactionHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ItemHeaderViewHolder(view)
            }

            else -> {
                val view = ItemTransactionBinding.inflate(
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

    companion object {
        private const val ITEM_HEADER = 0
        private const val ITEM_BODY = 1
    }
}