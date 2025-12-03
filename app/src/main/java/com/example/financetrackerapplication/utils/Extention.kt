package com.example.financetrackerapplication.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

object Extention {
    fun BottomSheetDialog.setupStyle(
        marginStart: Int = 8,
        marginEnd: Int = 8,
        marginBottom: Int = 8
    ) {
        val container = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        container?.let {
            val params = it.layoutParams as CoordinatorLayout.LayoutParams
            params.setMargins(marginStart, 0, marginEnd, marginBottom)
            it.layoutParams = params
        }
    }

    fun TextInputEditText.focusAndHideKeyboard() {
        this.post {
            this.isFocusableInTouchMode = true
            this.showSoftInputOnFocus = false
            this.requestFocus()
        }
    }

    fun TextInputEditText.hideKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }

    fun TextInputEditText.showKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    fun String.convertToDateMillis(): Long {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val parseDate = dateFormat.parse(this)
        return parseDate?.time ?: 0L
    }

    // di paki ketikaingin menyimpan ke room
    fun String.parseMoneyToLong(): Long {

        // cek apa ada comma
        val indexComma = this.indexOf(",")
        val newText = StringBuilder().append(this)

        if (indexComma != -1) {
            val afterComma = this.substring(startIndex = indexComma + 1)
            when (afterComma.length) {
                0 -> newText.append("00")
                1 -> newText.append("0")
            }
        }else
            newText.append(",00")

        val result = newText.toString().replace(",", "").replace(".", "")
        Log.d("EXT", "parseMoneyToLong: RESULT = ${result.toLong()}")
        return result.toLong()
    }

    //    yang baka di tampilkan ke ui
    fun Long.parseLongToMoney(): String {
        val result = this / 100.0
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        return format.format(result)
    }

    // NON FOCUS ALL editeks
    fun View.clearAllEditTexts(){
        if (this is EditText){
            this.text.clear()
        }else if (this is ViewGroup) {
            for (i in 0 until this.childCount) {
                this.getChildAt(i).clearAllEditTexts()
            }
        }
    }


}