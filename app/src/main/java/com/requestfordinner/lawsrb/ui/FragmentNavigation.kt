package com.requestfordinner.lawsrb.ui

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.requestfordinner.lawsrb.R
import com.requestfordinner.lawsrb.basic.htmlParser.Codex


/** The class provides information about the currently used fragment and code. */
class FragmentNavigation(private val activity: FragmentActivity) {

    /** Log tag field */
    private val TAG = "FragmentNavigation"

    /** The method will return the currently opened [Fragment] */
    fun getOpenedFragment(): Fragment? {
        var openedFragment: Fragment? = null

        val navHost =
            activity.supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
        navHost?.let { navFragment ->
            navFragment.childFragmentManager.primaryNavigationFragment?.let { fragment ->
                Log.i(TAG, "The current fragment is: $fragment")
                openedFragment = fragment
            }
        }
        return openedFragment
    }

    /** The method returns the code displayed on the current open fragment */
    fun getOpenedCode(): Codex {
        val containingAttribute: String = getOpenedFragment().toString()

        //TODO: try to find a better solution
        return if (containingAttribute.contains("UK")) Codex.UK
        else if (containingAttribute.contains("UPK")) Codex.UPK
        else if (containingAttribute.contains("KoAP")) Codex.KoAP
        else if (containingAttribute.contains("PIKoAP")) Codex.PIKoAP
        else throw Exception("The code type is not defined!")
    }

}