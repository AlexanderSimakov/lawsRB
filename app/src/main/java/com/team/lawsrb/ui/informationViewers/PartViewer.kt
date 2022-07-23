package com.team.lawsrb.ui.informationViewers

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.team.lawsrb.R
import com.team.lawsrb.basic.codeObjects.Part

@SuppressLint("AppCompatCustomView")
class PartViewer(context: Context, part: Part,
                 private val isDark: Boolean = true) : TextView(context) {

    init { // init text style
        text = part.title
        gravity = Gravity.CENTER_HORIZONTAL
        textSize = 20F

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(40, 20, 40, 20)
        this.setPadding(20, 20, 20, 20)
        layoutParams = params

        if (isDark){
            this.setBackgroundResource(R.color.purple_200)
        }else{

        }
    }

    // TODO: onClickListener to switch between tabs -> maybe
}