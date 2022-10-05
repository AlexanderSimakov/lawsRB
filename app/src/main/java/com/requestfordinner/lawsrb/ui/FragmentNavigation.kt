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

        val navHost = activity.supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main)

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
        val fragmentName: String = getOpenedFragment().toString()

        // PIKoAP should be always before KoAP because 'FragmentPIKoAP'
        // contains 'KoAP' and 'PIKoAP' both, but should return Codex.PIKoAP
        //TODO: try to find a better solution
        return if (fragmentName.contains("UK")) Codex.UK
        else if (fragmentName.contains("UPK")) Codex.UPK
        else if (fragmentName.contains("PIKoAP")) Codex.PIKoAP
        else if (fragmentName.contains("KoAP")) Codex.KoAP
        else throw Exception("The code type is not defined!")
    }
}
