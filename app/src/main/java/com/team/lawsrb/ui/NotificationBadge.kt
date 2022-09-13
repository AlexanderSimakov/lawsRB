package com.team.lawsrb.ui

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView

@SuppressLint("StaticFieldLeak")
object NotificationBadge {
    private var image: ImageView? = null

    var isVisible: Boolean = false
        set(value) {
            field = value

            if (isVisible) image?.visibility = View.VISIBLE
            else image?.visibility = View.GONE
        }

    fun setImage(image: ImageView){
        this.image = image
        isVisible = false
    }
}