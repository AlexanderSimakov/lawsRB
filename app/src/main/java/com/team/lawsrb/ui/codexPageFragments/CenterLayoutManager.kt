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

            override fun getVerticalSnapPreference() = SNAP_TO_START

            override fun calculateTimeForScrolling(dx: Int) =
                when (dx){
                    10_000 -> 100
                    else -> 300
                }
        }
    }

}
