package com.requestfordinner.lawsrb.basic

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.requestfordinner.lawsrb.basic.htmlParser.Codex

/**
 * [Preferences] is a *object* class which main purpose is to provide us useful methods
 * for working with [SharedPreferences].
 */
object Preferences {

    /** Unique preferences name */
    private const val PREFERENCES_NAME = "com.requestfordinner.lawsrb.preferences"

    /** Instance of [SharedPreferences] class */
    private var sharedPref: SharedPreferences? = null

    /** Key for [isDarkTheme]. */
    private const val IS_DARK_THEME_KEY = "isDarkModeOn"

    /** Key for [isRunFirst]. */
    private const val IS_RUN_FIRST_KEY = "isRunFirst"

    /**
     * Return `true` if dark theme is enabled.
     *
     * Changing this variable also change [SharedPreferences] field.
     */
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

    /**
     * Return `true` if application run first time on this device.
     *
     * Changing this variable also change [SharedPreferences] field.
     */
    var isRunFirst: Boolean
        get() {
            return !sharedPref!!.contains(IS_RUN_FIRST_KEY)
        }
        set(value) {
            if (isRunFirst) {
                sharedPref?.edit()?.apply {
                    putBoolean(IS_RUN_FIRST_KEY, value)
                    apply()
                }
            }
        }

    /**
     * This method set or update local instance of [SharedPreferences].
     */
    fun update(context: Context) {
        sharedPref = context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
    }

    /**
     * This method set [changesCount] for given [codex].
     */
    fun setCodexChangesCount(codex: Codex, changesCount: Int) {
        sharedPref!!.edit().apply {
            putInt(getChangesCountKey(codex), changesCount)
            apply()
        }
    }

    /**
     * This method set [date] of the last update for given [codex]
     */
    fun setCodexUpdateDate(codex: Codex, date: String) {
        sharedPref!!.edit().apply {
            putString(getUpdateDateKey(codex), date)
            apply()
        }
    }

    /**
     * This method set [changesCount] and last update [date] for given [codex].
     */
    fun setCodexInfo(codex: Codex, changesCount: Int, date: String) {
        sharedPref!!.edit().apply {
            putInt(getChangesCountKey(codex), changesCount)
            putString(getUpdateDateKey(codex), date)
            apply()
        }
    }

    /**
     * This method return count of changes for given [codex].
     */
    fun getCodexChangesCount(codex: Codex): Int {
        return sharedPref!!.getInt(getChangesCountKey(codex), -1)
    }

    /**
     * This method return date of the last update for given [codex].
     */
    fun getCodexUpdateDate(codex: Codex): String {
        return sharedPref!!.getString(getUpdateDateKey(codex), "")!!
    }

    /**
     * This method return a changes count *key* for given [codex].
     */
    private fun getChangesCountKey(codex: Codex) = "changesCount" + codex.name

    /**
     * This method return a update date *key* for given [codex].
     */
    private fun getUpdateDateKey(codex: Codex) = "updateDate" + codex.name
}
