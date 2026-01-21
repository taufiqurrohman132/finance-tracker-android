package com.example.financetrackerapplication.ui.settings.helpsupport.detailhelps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.financetrackerapplication.databinding.ItemTransactionBinding
import com.example.financetrackerapplication.databinding.ItemTransactionHeaderBinding
import com.example.financetrackerapplication.domain.model.HelpSupportItem
import com.example.financetrackerapplication.utils.BaseDiffCallback

class DetailHelpSupportAdapter: ListAdapter<HelpSupportItem, ViewHolder>(BaseDiffCallback { it }){
    inner class ItemImagesViewHolder(private val binding: ItemTransactionHeaderBinding) :
        ViewHolder(binding.root) {
        fun bind(item: HelpSupportItem.Images) {
            binding.apply {

            }


        }
    }

    inner class ItemTextViewHolder(private val binding: ItemTransactionBinding) :
        ViewHolder(binding.root) {
        fun bind(itemTransaction: HelpSupportItem.Text) {
            binding.apply {

            }






        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ITEM_TEXT -> {
                val view = ItemTransactionHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ItemImagesViewHolder(view)
            }

            else -> {
                val view = ItemTransactionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ItemTextViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is HelpSupportItem.Text -> ITEM_TEXT
            is HelpSupportItem.Images -> ITEM_IMAGES
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is HelpSupportItem.Images -> (holder as ItemImagesViewHolder).bind(item)
            is HelpSupportItem.Text -> (holder as ItemTextViewHolder).bind(item)
        }
    }

    companion object {
        private const val ITEM_TEXT = 0
        private const val ITEM_IMAGES = 1
        private val TAG = DetailHelpSupportAdapter::class.java.simpleName
    }

}