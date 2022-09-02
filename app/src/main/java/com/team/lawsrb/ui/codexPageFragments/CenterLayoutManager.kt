package com.team.lawsrb.ui.codexPageFragments

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class CenterLayoutManager : LinearLayoutManager {
    constructor(context: Context) : super(context)

    constructor(context: Context, orientation: Int, reverseLayout: Boolean)
            : super(context, orientation, reverseLayout)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView?,
        state: RecyclerView.State?,
        position: Int
    ) {
        val smoothScroller = CenterSmoothScroller(recyclerView!!.context)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    companion object{
        class CenterSmoothScroller(context: Context) : LinearSmoothScroller(context) {
            private val millisecondsPerInch = 40F // default is 25 (bigger = slower)

            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                if (displayMetrics != null) {
                    return millisecondsPerInch / displayMetrics.densityDpi
                }
                return 4F
            }
        }
    }

}
