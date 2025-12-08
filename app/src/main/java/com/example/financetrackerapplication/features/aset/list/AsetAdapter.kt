package com.example.financetrackerapplication.features.aset.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.databinding.ItemAsetBinding
import com.example.financetrackerapplication.databinding.ItemAsetHeaderBinding
import com.example.financetrackerapplication.databinding.ItemTransactionHeaderBinding
import com.example.financetrackerapplication.domain.model.GroupAset
import com.example.financetrackerapplication.domain.model.ItemAset2
import com.example.financetrackerapplication.utils.BaseDiffCallback
import com.example.financetrackerapplication.utils.Extention.parseLongToMoney
import com.iamkamrul.expandablerecyclerviewlist.adapter.ExpandableRecyclerAdapter
import com.iamkamrul.expandablerecyclerviewlist.model.ParentListItem
import com.iamkamrul.expandablerecyclerviewlist.viewholder.ChildViewHolder
import com.iamkamrul.expandablerecyclerviewlist.viewholder.ParentViewHolder

class AsetAdapter(
    private val onClickItemBody: (ItemAset2) -> Unit
) : ExpandableRecyclerAdapter<AsetAdapter.ItemHeaderViewHolder, AsetAdapter.ItemViewHolder>() {
    inner class ItemHeaderViewHolder(val binding: ItemAsetHeaderBinding): ParentViewHolder(binding.root){
        fun bind(group: GroupAset){
            val totalBalance = group.asetList.filter {
                it.groupAset == group.groupName
            }.sumOf { it.initialBalance }

            binding.asetGroupName.text = group.groupName
            binding.asetTotal.text = totalBalance.parseLongToMoney()
        }
    }
    inner class ItemViewHolder(val binding: ItemAsetBinding) : ChildViewHolder(binding.root){
        fun bind(aset : AsetEntity){
            binding.tvAsetNameItem.text = aset.name
            binding.tvJumlahAsetItem.text = aset.initialBalance.parseLongToMoney()
        }
    }

    override fun onBindChildViewHolder(
        childViewHolder: ItemViewHolder,
        position: Int,
        childListItem: Any
    ) {
        val data = childListItem as AsetEntity
        childViewHolder.bind(data)
    }

    override fun onBindParentViewHolder(
        parentViewHolder: ItemHeaderViewHolder,
        position: Int,
        parentListItem: ParentListItem
    ) {
        val data = parentListItem as GroupAset
        parentViewHolder.bind(data)
    }

    override fun onCreateChildViewHolder(parentViewGroup: ViewGroup): ItemViewHolder {
        val view = ItemAsetBinding.inflate(
            LayoutInflater.from(parentViewGroup.context),
            parentViewGroup,
            false
        )
        return ItemViewHolder(view)
    }

    override fun onCreateParentViewHolder(parentViewGroup: ViewGroup): ItemHeaderViewHolder {
        val view = ItemAsetHeaderBinding.inflate(
            LayoutInflater.from(parentViewGroup.context),
            parentViewGroup,
            false
        )
        return ItemHeaderViewHolder(view)
    }
}