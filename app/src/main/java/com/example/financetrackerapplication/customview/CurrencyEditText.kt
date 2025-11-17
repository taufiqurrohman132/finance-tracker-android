package com.example.financetrackerapplication.customview

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class CurrencyEditText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : TextInputEditText(context, attributeSet) {
    private var isFormatting = false
    init {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (isFormatting) return

                isFormatting = true
                val originalString = text.toString()

                if (originalString.isEmpty()){
                    isFormatting = false
                    return
                }

                val cleanString = originalString.replace(",", "").replace(".", "")
                val parsed = cleanString.toDoubleOrNull() ?: 0.0

                val formattingString = getFormattedNumber(parsed)

                setText(formattingString)
                setSelection(formattingString.length)

                isFormatting = false
                Log.d("TEXT", "afterTextChanged: $originalString" +
                        "clean String = $cleanString" +
                        "parsed string = $parsed")

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun getFormattedNumber(number: Double): String{
        val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
            groupingSeparator = '.'
            decimalSeparator = ','
        }
        val decimalFormat = DecimalFormat("#,###", symbols)
        return decimalFormat.format(number)
    }


}