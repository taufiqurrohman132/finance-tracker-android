package com.example.financetrackerapplication.utils

import android.content.Context
import android.graphics.Color
import android.icu.text.CompactDecimalFormat
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency
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

//    fun String.convertToDateMillis(): Long {
//        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
//        val parseDate = dateFormat.parse(this)
//        return parseDate?.time ?: 0L
//    }
//
//    fun String.convertToTimeMillis(): Long {
//        return try {
//            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
//            sdf.parse(this)?.time ?: 0L
//        } catch (e: Exception) {
//            0L
//        }
//    }


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
        } else
            newText.append(",00")

        val result = newText.toString().replace(",", "").replace(".", "")
        Log.d("EXT", "parseMoneyToLong: RESULT = ${result.toLong()}")
        return result.toLong()
    }

    //    yang baka di tampilkan ke ui
    fun Long.parseLongToMoney(): String {
        val result = this / 100.0

        return when{
            result >= 10_000_000 -> {
                val icuFormatter = CompactDecimalFormat.getInstance(
                    Locale.getDefault(),
                    CompactDecimalFormat.CompactStyle.SHORT
                )
                icuFormatter
                    .format(result)
                    .trim()
            }
            else ->{
                val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
                format.currency = Currency.getInstance("IDR")

                // hapus simbol mata uang rp
                if (format is DecimalFormat) {
                    val symbols = format.decimalFormatSymbols
                    symbols.currencySymbol = ""
                    format.decimalFormatSymbols = symbols
                }
                format.format(result).trim()
            }
        }

    }

    // NON FOCUS ALL editeks
    fun View.clearAllEditTexts() {
        if (this is EditText) {
            this.text.clear()
        } else if (this is ViewGroup) {
            for (i in 0 until this.childCount) {
                this.getChildAt(i).clearAllEditTexts()
            }
        }
    }


    // teksview
    fun TextView.setBalanceColor(typeIncome: String) {
        this.setTextColor(
            if (TransactionEntity.TYPE_EXPANSE == typeIncome) {
                Color.RED
            } else {
                Color.GREEN
            }
        )
    }


}