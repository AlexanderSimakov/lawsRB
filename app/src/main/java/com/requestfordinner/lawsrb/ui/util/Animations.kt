package com.requestfordinner.lawsrb.ui.util

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import com.requestfordinner.lawsrb.R

/** [View] extension function
 *
 * Performs a movement animation depending on the passed parameter [visibility]
 * with range from 0.0 to 700 along the Y coordinate
 */
fun View.movementAnimation(context: Context, visibility: Int) {
    this.visibility = visibility

    when (visibility) {
        View.VISIBLE -> {
            val animShow =
                AnimationUtils.loadAnimation(context, R.anim.translation_appear)
            startAnimation(animShow)
        }
        View.GONE -> {
            val animShow =
                AnimationUtils.loadAnimation(context, R.anim.translation_fade)
            startAnimation(animShow)
        }
    }
}