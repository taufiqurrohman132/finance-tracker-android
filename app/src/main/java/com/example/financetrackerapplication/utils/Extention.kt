package com.example.financetrackerapplication.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    fun TextInputEditText.focus(context: Context){
        this.post {
            this.isFocusableInTouchMode = true
            this.requestFocus()

            this.postDelayed({
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
            }, 150)
        }

//        postDelayed({
//            this.isFocusableInTouchMode = true
//            this.requestFocus()
//            val imm =
//                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
////            imm.hideSoftInputFromWindow(this.windowToken, 0)
//            imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
//        }, 300)
    }

    fun String.convertToDateMillis(): Long{
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val parseDate = dateFormat.parse(this)
        return parseDate?.time ?: 0L
    }
}