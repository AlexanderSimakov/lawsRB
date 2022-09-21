package com.team.lawsrb.ui.codexPageFragments

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class CenterLayoutManager : LinearLayoutManager {
    constructor(context: Context) : super(context)

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView?,
        state: RecyclerView.State?,
        position: Int
    ) {
        val smoothScroller = CenterSmoothScroller(recyclerView!!.context)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

}

class CenterSmoothScroller(context: Context) : LinearSmoothScroller(context) {
    companion object {
        private const val MILLISECONDS_PER_INCH = 5F
    }

    override fun getVerticalSnapPreference() = SNAP_TO_START

    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
        return MILLISECONDS_PER_INCH / displayMetrics!!.densityDpi
    }

    override fun calculateTimeForDeceleration(dx: Int): Int {
        return super.calculateTimeForDeceleration(dx) * 5
    }
}
