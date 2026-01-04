package com.example.financetrackerapplication.utils

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.utils.Extention.setupStyle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText

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

    fun keyboardClickListener(
        context: Context,
        nextFocus: () -> Unit,
        editText: TextInputEditText,
        vararg button: AppCompatButton
    ) {
        button.forEach { btn ->
            btn.setOnClickListener {
                when (btn.id) {
                    R.id.key_num_delete -> editText.text?.apply {
                        if (this.isNotBlank()) delete(length - 1, length) // hapus karakter terakhir
                    }

                    R.id.key_num_minus -> editText.text?.apply {
                        val minus = btn.text.toString()
                        if (!this.toString().contains(minus)) insert(0, btn.text.toString())
                        else delete(0, 1)
                    }

                    R.id.key_num_koma -> editText.text?.apply {
                        // di editteks apa ada koma
                        val comma = btn.text.toString()
                        if (!this.toString().contains(comma)) insert(this.length, comma)
                    }

                    R.id.key_num_calculator -> {
                        showToast(context, "Fitur Belum Tersedia")
                    }

                    R.id.key_num_selesai -> {
                        nextFocus.invoke()
                    }

                    else -> editText.append(btn.text.toString())
                }
            }

        }
    }
}