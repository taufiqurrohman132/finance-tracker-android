package com.example.financetrackerapplication.features.aset.list

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.databinding.ItemAsetBinding
import com.example.financetrackerapplication.databinding.ItemAsetHeaderBinding
import com.example.financetrackerapplication.domain.model.GroupAset
import com.example.financetrackerapplication.utils.Extention.parseLongToMoney
import com.iamkamrul.expandablerecyclerviewlist.adapter.ExpandableRecyclerAdapter
import com.iamkamrul.expandablerecyclerviewlist.model.ParentListItem
import com.iamkamrul.expandablerecyclerviewlist.viewholder.ChildViewHolder
import com.iamkamrul.expandablerecyclerviewlist.viewholder.ParentViewHolder
import kotlin.math.log

class AsetAdapter :
    ExpandableRecyclerAdapter<AsetAdapter.ItemHeaderViewHolder, AsetAdapter.ItemViewHolder>() {
    private var isSelectionMode = false

    private val groupSelected = mutableSetOf<GroupAset>()   // <— ini kunci


    inner class ItemHeaderViewHolder(val binding: ItemAsetHeaderBinding) :
        ParentViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(group: GroupAset, position: Int) {

            val totalBalance = group.asetList.filter {
                it.groupAset == group.groupName
            }.sumOf { it.initialBalance }

            binding.asetGroupName.text = group.groupName
            binding.asetTotal.text = totalBalance.parseLongToMoney()

            itemView.setOnLongClickListener {
                Log.d(TAG, "bind: gruop item selected = ${group.isSelected}")
                if (!this@AsetAdapter.isSelectionMode) {
                    onParentListItemExpanded(position)
                    this@AsetAdapter.isSelectionMode = true

                    group.isSelected = true

                    itemView.background =
                        ContextCompat.getDrawable(itemView.context, R.drawable.bg_item_selection)

                    groupSelected.add(group)
                    Log.d(TAG, "bind: lenght aset list = ${group.asetList.size}")
                    itemView.post {
                        notifyItemRangeChanged(position + 1, group.asetList.size)
                    }
                }
                Log.d(TAG, "bind: list groupaset = ${groupSelected.size}")

                true

            }

            itemView.setOnClickListener {
                Log.d(TAG, "onClick: item ivew is selected ${this@AsetAdapter.isSelectionMode}")
                if (this@AsetAdapter.isSelectionMode) {
                    onParentListItemExpanded(position)
                    group.isSelected = !group.isSelected

                    Log.d(TAG, "onClick: group is selected = ${group.isSelected}")
                    itemView.background = if (group.isSelected) {
                        groupSelected.add(group)
                        Log.d(TAG, "bind: add group = $group")
                        ContextCompat.getDrawable(itemView.context, R.drawable.bg_item_selection)
                    } else {
                        val removed = groupSelected.remove(group)
                        Log.d(TAG, "Removed? $removed")
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.cograd_stroke_blue_to_purple
                        )
                    }
                    Log.d(TAG, "bind: lenght aset list = ${group.asetList.size}")
                    itemView.post {
                        notifyItemRangeChanged(position + 1, group.asetList.size)
                    }
                } else {
                    // default expand/collapse
                    onClick(it)
                }
                Log.d(TAG, "bind: list groupaset = ${groupSelected.size}")
            }
        }



        override fun setExpanded(expanded: Boolean) {
            super.setExpanded(expanded)
            binding.asetIndicatorExpand.setBackgroundColor(
                if (expanded) itemView.context.getColor(R.color.color_primary)
                else itemView.context.getColor(R.color.grey_trans_10)
            )
        }

    }

    private fun checkSelectionMode() {
        val anySelected = getParentItemList().any { (it as GroupAset).isSelected }
        Log.d(TAG, "checkSelectionMode: grous aset $groupSelected")
        if (!anySelected) {
            clearSelection()
            isSelectionMode = false
        }
    }

    inner class ItemViewHolder(val binding: ItemAsetBinding) : ChildViewHolder(binding.root) {
        fun bind(aset: AsetEntity) {
            binding.tvAsetNameItem.text = aset.name
            binding.tvJumlahAsetItem.text = aset.initialBalance.parseLongToMoney()

            val groupNameSelected = groupSelected.filter { aset.groupAset == it.groupName }
            Log.d(TAG, "bind: group selected is a = $groupNameSelected")
            groupNameSelected.forEach { group ->
                Log.d(TAG, "bind: item child is foreach")
                if (this@AsetAdapter.isSelectionMode) {
                    Log.d(TAG, "bind: item child selected")
                    if (group.isSelected) {
                        itemView.backgroundTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.color_primary_gradiant_10
                            )
                        )
                    } else {
                        itemView.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                    }
                }
            }
            checkSelectionMode()
        }
    }


    fun clearSelection() {
        groupSelected.clear()
    }

    fun isAnythingSelected() = groupSelected.isNotEmpty()

//    fun getSelectedItems(): List<AsetEntity> =
//        childListItem.filter { childSelectedIds.contains(it.id) }

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
        parentViewHolder.bind(data, position)
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

    companion object {
        private val TAG = AsetAdapter::class.java.simpleName
    }
}