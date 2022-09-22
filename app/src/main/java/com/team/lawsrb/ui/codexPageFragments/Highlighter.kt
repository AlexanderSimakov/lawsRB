package com.team.lawsrb.ui.codexPageFragments

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.widget.TextView

/**
 * [Highlighter] is a *object* class which provide method to highlight text in a [TextView].
 *
 * **Note:** [Highlighter.isDarkMode] should always match app theme for correct work.
 */
object Highlighter {

    /** This field return highlight color depending on the [isDarkMode]. */
    private val color: Int
        get() {
            // TODO: adjust colors
            return if (isDarkMode) Color.parseColor("#413F42")
                   else Color.parseColor("#EBD671")
        }

    /** [isDarkMode] used to determine highlight color. It should always match app theme. */
    var isDarkMode = false

    /** This method highlight [textToHighlight] in a given [textView]. */
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
