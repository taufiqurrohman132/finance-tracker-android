package com.example.financetrackerapplication.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.utils.Extention.setupStyle
import com.google.android.material.bottomsheet.BottomSheetDialog

object DialogUtils {
    fun showBottomSheet(context: Context, view: View) : BottomSheetDialog{
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.apply {
            setContentView(view)
            setupStyle(16, 16, 16)// setup radius
            show()
        }
        return bottomSheetDialog
    }
    fun optionMenu(context: Context){
        val po = LayoutInflater.from(context).inflate(R.layout.menu_cicilan_berulang, null)
        val pop = PopupWindow(
            po,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        pop.elevation = 12f
        pop.elevation = 12f

    }

    fun showToast(context: Context, msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}