package com.example.financetrackerapplication.features.dashboard

import android.graphics.Color
import android.util.Log
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
    private var isSelectedMode = false

    inner class ItemHeaderViewHolder(private val binding: ItemTransactionHeaderBinding) :
        ViewHolder(binding.root) {
        fun bind(itemTransaction: ItemTransaction) {
            binding.apply {
                itemTransaction.dateTimeMillis?.let {
                    val dateResult = TimeUtils.getDate(it)
                    itemTransHeaderTvDate.text = TimeUtils.getStyleDate(dateResult)
                }
                itemTransHeaderTvDay.text = itemTransaction.day
                itemTransHeaderTvIncome.apply {
                    setTextColor(Color.GREEN)
                    text = itemView.resources.getString(
                        R.string.total_balance,
                        itemTransaction.income.toString().toLong().parseLongToMoney()
                    )

                }
                itemTransHeaderTvExpanse.apply {
                    setTextColor(Color.RED)
                    text = itemView.resources.getString(
                        R.string.total_balance,
                        itemTransaction.expense.toString().toLong().parseLongToMoney()
                    )

                }
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
                tvKategoriItem.text = itemTransaction.dataItem?.category?.name
                tvCatatanItem.text = itemTransaction.dataItem?.transaction?.catatan
                tvAsetItem.text = itemTransaction.dataItem?.account?.name
                tvJumlahItem.apply {
                    text = itemView.resources.getString(
                        R.string.total_balance,
                        itemTransaction.dataItem?.transaction?.amount.toString().toLong()
                            .parseLongToMoney()
                    )
                    setBalanceColor(itemTransaction.dataItem?.transaction?.type.toString())
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

            Log.d(TAG, "bind: item is selected mode = $isSelectedMode")
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
            isSelectedMode = currentList.any { it.isSelected }

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
        private val TAG = DashboardAdapter::class.java.simpleName
    }
}