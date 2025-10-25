package com.example.financetrackerapplication.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil.ItemCallback

class BaseDiffCallback<T: Any> (
    private val idSelector: (T) -> Any
) : ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return idSelector(oldItem) == idSelector(newItem)
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}