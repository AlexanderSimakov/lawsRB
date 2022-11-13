package com.requestfordinner.lawsrb.utils

import android.content.Context
import android.widget.Toast

/**
 * This class provides convenient methods to work with [Toast].
 *
 * @see [Toast]
 */
class ImprovedToast(private val context: Context) {
    private var toast: Toast? = null

    /**
     * Use this to show [Toast] message with given length.
     *
     * @param message R.string id to show.
     * @param length Message length ([Toast.LENGTH_SHORT] by default).
     */
    fun show(message: Int, length: Int = Toast.LENGTH_SHORT) {
        toast?.cancel()
        toast = Toast.makeText(context, message, length)
        toast?.show()
    }

    /**
     * Use this to show [Toast] message with given length.
     *
     * @param message [String] to show.
     * @param length Message length ([Toast.LENGTH_SHORT] by default).
     */
    fun show(message: String, length: Int = Toast.LENGTH_SHORT) {
        toast?.cancel()
        toast = Toast.makeText(context, message, length)
        toast?.show()
    }
}