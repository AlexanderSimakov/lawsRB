package com.requestfordinner.lawsrb.ui

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView

/**
 * [NotificationBadge] is a class which main purpose is to store notification [image] for
 * **Codex Update Page** and change it visibility.
 *
 * Before use [isVisible] make sure you set notification [image].
 */
@SuppressLint("StaticFieldLeak")
object NotificationBadge {

    /** The **Codex Update Page** notification image. */
    var image: ImageView? = null
        set(value) {
            field = value
            isVisible = false
        }

    /**
     * Equals `true` if [image] is visible.
     *
     * Changing this property also change [image] visibility.
     *
     * @see image
     */
    var isVisible: Boolean = false
        set(value) {
            field = value

            if (isVisible) image?.visibility = View.VISIBLE
            else image?.visibility = View.GONE
        }
}
