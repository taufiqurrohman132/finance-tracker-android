package com.example.financetrackerapplication.customview

import android.content.Context
import android.text.Editable
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
            override fun afterTextChanged(editable: Editable?) {
                if (isFormatting) return

                isFormatting = true
                val originalString = editable.toString()

                Log.d("TEXT", "afterTextChanged: original string = $originalString")
                if (
                    originalString == "," ||
                    originalString.contains(",") ||
                    originalString.isEmpty()
                ) {
                    // cek apa ada comma
                    val indexComma = originalString.indexOf(",")

                    Log.d("TEXT", "afterTextChanged: jalan ")

                    if (indexComma != -1){
                        val afterComma = originalString.substring(startIndex = indexComma + 1)
                        val lastInput = originalString.last() // teks terakhir

                        Log.d("TEXT", "afterTextChanged: after comma = $afterComma")
                        if (afterComma.length > 2 ) {
                            //ganti karakter terakhir
                            val newText = StringBuilder().apply {
                                append(editable?.subSequence(0, editable.length -2))
                                append(lastInput)
                            }
                            setText(newText)
                            setSelection(newText.length)
                        }
                    }
                    isFormatting = false
                    return
                }

                Log.d("TEXT", "afterTextChanged: clean string running")
                val cleanString = originalString.replace(",", "").replace(".", "")
                val parsed = cleanString.toLongOrNull() ?: 0

                val formattingString = getFormattedNumber(parsed)

                setText(formattingString)
                setSelection(formattingString.length)

                isFormatting = false
                Log.d(
                    "TEXT", "afterTextChanged: $originalString" +
                            "clean String = $cleanString" +
                            "parsed string = $parsed"
                )

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(textChange: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun getFormattedNumber(number: Long): String {
        val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
            groupingSeparator = '.'
            decimalSeparator = ','
        }
        val decimalFormat = DecimalFormat("#,###", symbols)
        return decimalFormat.format(number)
    }


}