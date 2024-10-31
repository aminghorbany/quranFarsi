package com.amin.quran.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

fun Context.showToast(text:String){
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}
fun Context.showSnackBarShort(text:String , view: View , actionText : String = ""){
    Snackbar.make(this ,view , text , Snackbar.LENGTH_SHORT).apply {
        setAction(actionText) { dismiss() }
        show()
    }
}

fun Context.showSnackBarLong(text:String , view: View , actionText : String = ""){
    Snackbar.make(this ,view , text , Snackbar.LENGTH_LONG).apply {
        setAction(actionText) { dismiss() }
        show()
    }
}

fun Context.showWidget(view: View){
    view.visibility = View.VISIBLE
}

fun Context.hideWidget(view: View){
    view.visibility = View.INVISIBLE
}

fun Context.goneWidget(view: View){
    view.visibility = View.GONE
}

fun TextView.setMyBackground(context: Context , color: Int){
    this.setBackgroundColor(ContextCompat.getColor(context , color))
}

fun convertNumberToPersian(number: Int): String {
    val persianDigits = arrayOf(
        "۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"
    )
    val englishNumber = number.toString()
    val convertedNumber = StringBuilder()

    for (digit in englishNumber) {
        if (digit.isDigit()) {
            val digitValue = digit.toString().toInt()
            convertedNumber.append(persianDigits[digitValue])
        } else {
            convertedNumber.append(digit)
        }
    }

    return convertedNumber.toString()
}