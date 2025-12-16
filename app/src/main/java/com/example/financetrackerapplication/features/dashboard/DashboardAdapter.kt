package com.example.financetrackerapplication.features.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.databinding.ItemTransactionBinding
import com.example.financetrackerapplication.databinding.ItemTransactionHeaderBinding
import com.example.financetrackerapplication.domain.model.ItemTransaction
import com.example.financetrackerapplication.utils.BaseDiffCallback
import com.example.financetrackerapplication.utils.Extention.parseLongToMoney
import com.example.financetrackerapplication.utils.Extention.setBalanceColor
import com.example.financetrackerapplication.utils.TimeUtils

class DashboardAdapter(
    private val onClickItemHeader: (ItemTransaction) -> Unit,
    private val onClickItemBody: (ItemTransaction) -> Unit,
    private val onItemSelected: (ItemTransaction) -> Unit,
) : ListAdapter<ItemTransaction, ViewHolder>(BaseDiffCallback { it }) {
    var isSelectedMode = false

    inner class ItemHeaderViewHolder(private val binding: ItemTransactionHeaderBinding) :
        ViewHolder(binding.root) {
        fun bind(itemTransaction: ItemTransaction) {
            binding.apply {
                itemTransaction.dateTimeMillis?.let {
                    val dateResult = TimeUtils.getDate(it)
                    itemTransHeaderTvDate.text = TimeUtils.getStyleDate(dateResult)
                }
                itemTransHeaderTvDay.text = itemTransaction.day
                itemTransHeaderTvIncome.text =
                    itemTransaction.income.toString().toLong().parseLongToMoney()
                itemTransHeaderTvExpanse.text =
                    itemTransaction.expense.toString().toLong().parseLongToMoney()
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
                tvJumlahItem.apply {
                    text = itemTransaction.amount.toString().toLong().parseLongToMoney()
                    setBalanceColor(itemTransaction.typeBalance.toString())
                }

                bgTransactionItem.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        if (itemTransaction.isSelected)
                            R.color.color_primary_gradiant_10
                        else
                            R.color.white
                    )
                )

            }

            itemView.setOnLongClickListener {
                isSelectedMode = true
                onItemSelected(itemTransaction)
                true
            }


            itemView.setOnClickListener {
                if (isSelectedMode) {
                    onItemSelected(itemTransaction)
                } else onClickItemBody.invoke(itemTransaction)
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