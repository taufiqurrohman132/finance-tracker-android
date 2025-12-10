package com.example.financetrackerapplication.utils

import android.os.Parcelable
import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView

class GenericItemKeyProvider(
    private val adapter: RecyclerView.Adapter<*>,
    private val keySelector: (Int) -> Long
) : ItemKeyProvider<Long>(SCOPE_MAPPED) {
    override fun getKey(position: Int) = keySelector(position)
    override fun getPosition(key: Long) = (0 until adapter.itemCount).firstOrNull { keySelector(it) == key } ?: RecyclerView.NO_POSITION
}

class GenericItemDetailsLookup(
    private val recyclerView: RecyclerView,
    private val keySelector: (Int) -> Long
) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y) ?: return null
        val holder = recyclerView.getChildViewHolder(view)
        return object : ItemDetails<Long>() {
            override fun getPosition() = holder.adapterPosition
            override fun getSelectionKey() = keySelector(holder.adapterPosition)
            override fun inSelectionHotspot(e: MotionEvent) = true
        }
    }
}

fun createLongTracker(
    recyclerView: RecyclerView,
    adapter: RecyclerView.Adapter<*>,
    keySelector: (Int) -> Long,
    selectionId: String
): SelectionTracker<Long> {
    return SelectionTracker.Builder(
        selectionId,
        recyclerView,
        GenericItemKeyProvider(adapter, keySelector),
        GenericItemDetailsLookup(recyclerView, keySelector),
        StorageStrategy.createLongStorage()  // key memang Long
    ).withSelectionPredicate(SelectionPredicates.createSelectAnything())
        .build()
}


