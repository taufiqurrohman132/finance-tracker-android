package com.example.financetrackerapplication.utils

import android.content.Context
import android.view.View
import com.example.financetrackerapplication.utils.Extention.setupStyle
import com.google.android.material.bottomsheet.BottomSheetDialog

object DialogUtils {
    fun showBottomSheet(context: Context, view: View){
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.apply {
            setContentView(view)
            setupStyle(16, 16, 16)// setup radius
            show()
        }
    }
}