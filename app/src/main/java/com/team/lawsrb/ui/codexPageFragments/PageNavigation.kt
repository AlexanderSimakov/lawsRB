package com.team.lawsrb.ui.codexPageFragments

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import java.util.*
import kotlin.concurrent.schedule

object PageNavigation {
    var viewPager: ViewPager2? = null

    private class Recycler(val recyclerView: RecyclerView, val items: MutableList<Any>)
    private var recyclers: MutableList<Recycler> = mutableListOf()
    private val delayBeforeScrolling = 300L

    fun addRecyclerView(view: RecyclerView, items: MutableList<Any>, number: Int) {
        recyclers.add(number, Recycler(view, items))
    }

    fun moveLeftTo(codexObject: Any) {
        viewPager!!.setCurrentItem(viewPager!!.currentItem - 1, true)
        Timer().schedule(delayBeforeScrolling){
            scrollPageTo(codexObject)
        }
    }

    fun moveRightTo(codexObject: Any) {
        viewPager!!.setCurrentItem(viewPager!!.currentItem + 1, true)
        Timer().schedule(delayBeforeScrolling){
            scrollPageTo(codexObject)
        }
    }

    private fun scrollPageTo(codexObject: Any){
        recyclers[viewPager!!.currentItem].let { recycler ->
            val position = recycler.items.indexOfFirst { it == codexObject }
            recycler.recyclerView.smoothScrollToPosition(position)
        }
    }

    fun clear(){
        viewPager = null
        recyclers.clear()
    }
}