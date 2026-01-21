package com.example.financetrackerapplication.ui.settings.helpsupport

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.financetrackerapplication.databinding.ItemCategoryBinding
import com.example.financetrackerapplication.utils.BaseDiffCallback

class HelpSupportAdapter(
    private val onClickItem: (String) -> Unit,
) : ListAdapter<String, HelpSupportAdapter.ItemViewHolder>(BaseDiffCallback { it }) {
    inner class ItemViewHolder(
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(titleName: String) {
            binding.tvCategoryNameItem.text = titleName
            itemView.setOnClickListener {
                onClickItem(titleName)
            }
        }
    }

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
        private val TAG = HelpSupportAdapter::class.java.simpleName
    }
}