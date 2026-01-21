package com.example.financetrackerapplication.utils

import android.graphics.Rect
import android.util.Printer
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class LastItemBottomMarginDecoration(
    private val bottomMargin: Int,
    private val adapter: Adapter<*>
): ItemDecoration(){
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        val viewType = adapter.getItemViewType(position)
        val isLastBodyItem = viewType != 0
        val isLastItem = position == state.itemCount -1
        if (isLastBodyItem && isLastItem){
            outRect.bottom = bottomMargin
        }
    }
}