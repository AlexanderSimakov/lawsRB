package com.team.lawsrb.ui.informationViewers

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.team.lawsrb.R

@SuppressLint("AppCompatCustomView")
class SectionViewer(context: Context, title: String) : TextView(context) {

    init { // init text style
        text = title
        gravity = Gravity.CENTER_HORIZONTAL
        textSize = 20F

        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.setMargins(40, 20, 40, 20)
        this.setPadding(20, 20, 20, 20)
        layoutParams = params

        this.setBackgroundResource(R.color.purple_200)
    }

    // TODO: onClickListener to switch between tabs -> maybe
}