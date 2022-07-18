package com.team.lawsrb.ui.informationViewers

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.team.lawsrb.R

@SuppressLint("AppCompatCustomView")
class ChapterViwer(context: Context, title: String) : TextView(context) {

    init { // init text style
        text = title
        gravity = Gravity.CENTER_HORIZONTAL
        textSize = 20F

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(40, 20, 40, 20)
        this.setPadding(20, 20, 20, 20)
        layoutParams = params

        this.setBackgroundResource(R.color.purple_200)
    }

    // TODO: onClickListener to switch between tabs -> maybe
}