package com.team.lawsrb.basic

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.team.lawsrb.basic.htmlParser.Codex

/**
 * [Preferences] is a *object* class which main purpose is to provide us useful methods
 * for working with [SharedPreferences].
 */
object Preferences {

    /** Unique preferences name */
    private const val PREFERENCES_NAME = "com.team.lawsrb.preferences"

    /** Instance of [SharedPreferences] class */
    private var sharedPref: SharedPreferences? = null

    // keys
    private const val IS_DARK_THEME_KEY = "isDarkModeOn"
    private const val IS_RUN_FIRST_KEY = "isRunFirst"

    var isDarkTheme: Boolean
        get() {
            return sharedPref!!.getBoolean(IS_DARK_THEME_KEY, false)
        }
        set(value) {
            sharedPref!!.edit().apply {
                putBoolean(IS_DARK_THEME_KEY, value)
                apply()
            }
        }

    var isRunFirst: Boolean
        get() {
            return !sharedPref!!.contains(IS_RUN_FIRST_KEY)
        }
        set(value){
            if (isRunFirst){
                sharedPref?.edit()?.apply {
                    putBoolean(IS_RUN_FIRST_KEY, value)
                    apply()
                }
            }
        }

    fun update(context: Context){
        sharedPref = context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
    }

    fun setCodexChangesCount(codex: Codex, changesCount: Int){
        sharedPref!!.edit().apply {
            putInt(getChangesCountKey(codex), changesCount)
            apply()
        }
    }

    fun setCodexUpdateDate(codex: Codex, date: String){
        sharedPref!!.edit().apply {
            putString(getUpdateDateKey(codex), date)
            apply()
        }
    }

    fun setCodexInfo(codex: Codex, changesCount: Int, date: String){
        sharedPref!!.edit().apply {
            putInt(getChangesCountKey(codex), changesCount)
            putString(getUpdateDateKey(codex), date)
            apply()
        }
    }

    fun getCodexChangesCount(codex: Codex): Int{
        return sharedPref!!.getInt(getChangesCountKey(codex), -1)
    }

    fun getCodexUpdateDate(codex: Codex): String{
        return sharedPref!!.getString(getUpdateDateKey(codex), "")!!
    }

    private fun getChangesCountKey(codex: Codex) = "changesCount" + codex.name

    private fun getUpdateDateKey(codex: Codex) = "updateDate" + codex.name
}
