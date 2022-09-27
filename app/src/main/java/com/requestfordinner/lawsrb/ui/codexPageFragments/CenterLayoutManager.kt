package com.requestfordinner.lawsrb.ui.codexPageFragments

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/**
 * [CenterLayoutManager] is a custom [LinearLayoutManager] which main difference and purpose
 * is to scroll to the beginning of the element.
 *
 * @see LinearLayoutManager
 */
class CenterLayoutManager(context: Context) : LinearLayoutManager(context) {

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView?,
        state: RecyclerView.State?,
        position: Int
    ) {
        val smoothScroller = getSmoothScroller(recyclerView!!.context)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    private fun getSmoothScroller(context: Context) = object : LinearSmoothScroller(context){
        private val MILLISECONDS_PER_INCH = 5F

        override fun getVerticalSnapPreference() = SNAP_TO_START

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
            return MILLISECONDS_PER_INCH / displayMetrics!!.densityDpi
        }

        override fun calculateTimeForDeceleration(dx: Int): Int {
            return super.calculateTimeForDeceleration(dx) * 5
        }
    }
}
