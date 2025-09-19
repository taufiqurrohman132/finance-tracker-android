package com.example.financetrackerapplication.utils

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialog

object Extention {
    fun BottomSheetDialog.setupStyle(
        marginStart: Int = 8,
        marginEnd: Int = 8,
        marginBottom: Int = 8
    ){
        val container = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        container?.let {
            val params = it.layoutParams as CoordinatorLayout.LayoutParams
            params.setMargins(marginStart, 0, marginEnd, marginBottom)
            it.layoutParams = params
        }
    }
}