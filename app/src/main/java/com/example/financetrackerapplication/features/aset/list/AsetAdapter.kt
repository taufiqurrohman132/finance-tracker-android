package com.example.financetrackerapplication.features.aset.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.databinding.ItemAsetBinding
import com.example.financetrackerapplication.databinding.ItemAsetHeaderBinding
import com.example.financetrackerapplication.domain.model.GroupAset
import com.example.financetrackerapplication.utils.BalanceUtils
import com.example.financetrackerapplication.utils.Extention.parseLongToMoney

class AsetAdapter(
    private val onExpand: (GroupAset.Parent) -> Unit,
    private val onSelect: (GroupAset.Child) -> Unit,
    private val selectionCallback: SelectionCallback
) : ListAdapter<GroupAset, RecyclerView.ViewHolder>(Diff) {

    private var isSelectionMode = false

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is GroupAset.Parent -> TYPE_PARENT
            is GroupAset.Child -> TYPE_CHILD
        }

    // ---------------- PARENT ----------------
    inner class ParentVH(
        private val binding: ItemAsetHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(parent: GroupAset.Parent) {
            val total = parent.childAsetList
                .sumOf { it.aset.initialBalance }

            binding.asetGroupName.text = parent.name
            binding.asetTotal.text = total.parseLongToMoney()

            binding.asetIndicatorExpand.setBackgroundColor(
                binding.root.context.getColor(
                    if (parent.isExpanded)
                        R.color.color_primary
                    else
                        R.color.grey_trans_10
                )
            )

            itemView.setOnClickListener {
                onExpand(parent)
            }
        }
    }

    // ---------------- CHILD ----------------
    inner class ChildVH(
        private val binding: ItemAsetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(child: GroupAset.Child) {
            binding.tvAsetNameItem.text = child.aset.name
            binding.tvJumlahAsetItem.apply {
                text = child.aset.initialBalance.parseLongToMoney()
                setTextColor(BalanceUtils.getBalanceColor(child.aset.initialBalance))
            }

            binding.bgAsetItem.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (child.isSelected)
                        R.color.color_primary_gradiant_10
                    else
                        R.color.white
                )
            )

            if (!isSelectionMode)
                selectionCallback.onSelectionEnded()

            itemView.setOnLongClickListener {
                isSelectionMode = true
                selectionCallback.onSelectionStarted()
                onSelect(child)
                true
            }

            itemView.setOnClickListener {
                if (isSelectionMode) {
                    onSelect(child)
                }
            }
            isSelectionMode = currentList.any { it is GroupAset.Child && it.isSelected }
        }
    }


    // ---------------- ADAPTER ----------------
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder =
        if (viewType == TYPE_PARENT) {
            ParentVH(
                ItemAsetHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            ChildVH(
                ItemAsetBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is GroupAset.Parent ->
                (holder as ParentVH).bind(item)

            is GroupAset.Child ->
                (holder as ChildVH).bind(item)
        }
    }

    interface SelectionCallback {
        fun onSelectionStarted()
        fun onSelectionUpdated(selectedCount: Int)
        fun onSelectionEnded()
    }

    companion object{

        private const val TYPE_PARENT = 0
        private const val TYPE_CHILD = 1

        val Diff = object : DiffUtil.ItemCallback<GroupAset>() {

            override fun areItemsTheSame(old: GroupAset, new: GroupAset): Boolean =
                when {
                    old is GroupAset.Parent && new is GroupAset.Parent ->
                        old.name == new.name

                    old is GroupAset.Child && new is GroupAset.Child ->
                        old.aset.id == new.aset.id

                    else -> false
                }

            override fun areContentsTheSame(old: GroupAset, new: GroupAset): Boolean =
                old == new
        }

    }
}
