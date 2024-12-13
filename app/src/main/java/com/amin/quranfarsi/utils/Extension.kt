package com.amin.quranfarsi.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.snackbar.Snackbar

fun Context.showShortToast(txt : String){
    Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(txt : String){
    Toast.makeText(this, txt, Toast.LENGTH_LONG).show()
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

fun Context.showWidget(view : View){
    view.visibility = View.VISIBLE
}

fun Context.hideWidget(view : View){
    view.visibility = View.INVISIBLE
}

fun Context.goneWidget(view : View){
    view.visibility = View.GONE
}

fun Int.toCommaSeparatedString(): String {
    return "%,d".format(this)
}

fun RecyclerView.initRecyclerView(layoutManager : LayoutManager , adapter : RecyclerView.Adapter<*>){
    this.layoutManager = layoutManager
    this.adapter = adapter
//    this.setHasFixedSize(true)
    // we can set animation
}


fun String.convertRialToToman(): String {
    val rialValue = this.replace(",", "").toIntOrNull() ?: return this
    val tomanValue = rialValue / 10
    return tomanValue.toString().replace(Regex("(\\d)(?=(\\d{3})+\$)"), "\$1,")
}

fun String.convertToPersianDigits(): String {
    val englishDigits = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    val persianDigits = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")

    var result = this
    for (i in englishDigits.indices) {
        result = result.replace(englishDigits[i], persianDigits[i])
    }
    return result
}
