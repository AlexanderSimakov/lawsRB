package com.team.lawsrb.ui.codexPageFragments

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.widget.TextView

object Highlighter {
    private var color: Int = Color.YELLOW

    fun applyTo(textView: TextView, textToHighlight: String){
        if (textToHighlight.isEmpty()) return

        // TODO: change variable names
        val text = textView.text.toString()
        val textToSpan: Spannable = SpannableString(text)
        var ofe = text.indexOf(textToHighlight, 0, true)

        var ofs = 0
        while (ofs < text.length && ofe != -1) {
            ofe = text.indexOf(textToHighlight, ofs, true)
            if (ofe == -1) break
            else {
                textToSpan.setSpan(
                    BackgroundColorSpan(color),
                    ofe,
                    ofe + textToHighlight.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            ofs = ofe + 1
        }
        textView.setText(textToSpan, TextView.BufferType.SPANNABLE)
    }
}
