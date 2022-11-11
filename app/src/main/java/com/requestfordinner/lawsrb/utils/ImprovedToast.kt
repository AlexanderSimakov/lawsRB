package com.requestfordinner.lawsrb.utils

import android.content.Context
import android.widget.Toast

/**
 *
 */
class ImprovedToast(private val context: Context) {
    private var toast: Toast? = null

    /**
     *
     */
    fun show(message: Int, length: Int = Toast.LENGTH_SHORT) {
        toast?.cancel()
        toast = Toast.makeText(context, message, length)
        toast?.show()
    }

    /**
     *
     */
    fun show(message: String, length: Int = Toast.LENGTH_SHORT) {
        toast?.cancel()
        toast = Toast.makeText(context, message, length)
        toast?.show()
    }
}