package com.example.financetrackerapplication.ui.settings.category.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.data.datasource.local.entity.CategoryEntity
import com.example.financetrackerapplication.databinding.ItemCategoryBinding
import com.example.financetrackerapplication.domain.model.DataSelected
import com.example.financetrackerapplication.utils.BaseDiffCallback

class CategoryAdapter(
    private val onSelect: (DataSelected<CategoryEntity>) -> Unit,
    private val onClicked: (DataSelected<CategoryEntity>) -> Unit,
) : ListAdapter<DataSelected<CategoryEntity>, CategoryAdapter.ItemViewHolder>(BaseDiffCallback { it.data.id }) {

    private var isSelectionMode = false

    inner class ItemViewHolder(
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryData: DataSelected<CategoryEntity>) {
            binding.tvCategoryNameItem.text = categoryData.data.name

            binding.bgCategoryItem.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (categoryData.isSelected)
                        R.color.color_primary_gradiant_10
                    else
                        R.color.grey_light     
                )
            )

            itemView.setOnLongClickListener {
                isSelectionMode = true
                onSelect(categoryData)
                true
            }

            itemView.setOnClickListener {
                if (isSelectionMode) {
                    onSelect(categoryData)
                } else
                    onClicked(categoryData)
            }
            isSelectionMode = currentList.any { it.isSelected }
        }
    }


    // ---------------- ADAPTER ----------------
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder = ItemViewHolder(
        ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {

    }
}
