package com.requestfordinner.lawsrb.basic.htmlParser

import android.util.Log
import com.requestfordinner.lawsrb.basic.Preferences
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements

/**
 * The singleton class that parsing html code from the web resource.
 *
 * Provides data on all changes in codes.
 */
object CodexVersionParser {
    /** Log tag field. */
    private const val TAG = "CodexVersionParser"

    /** Custom coroutine creation field. */
    private val scope = CoroutineScope (Dispatchers.IO + SupervisorJob ())

    /**
     * Map storing the count of code changes.
     *
     * Initialized with values that are true for the version of codes written to the application by default.
     */
    private var changesCount: MutableMap<Codex, Int> =
        mutableMapOf(
            Codex.UK to 82,
            Codex.UPK to 61,
            Codex.KoAP to 1,
            Codex.PIKoAP to 1
        )

    /**
     * Map storing the dates of lasts code's change.
     *
     * Initialized with values that are true for the version of codes written to the application by default.
     */
    private var lastChangeDate: MutableMap<Codex, String> =
        mutableMapOf(
            Codex.UK to "От 13 мая 2022",
            Codex.UPK to "От 20 июля 2022",
            Codex.KoAP to "От 4 января 2022",
            Codex.PIKoAP to "От 4 января 2022"
        )

    /**
     * The function for updating values of last change date and count of changes.
     * Connects asynchronously to each code's web page.
     * @exception CodexVersionParser - can throw an exception?, NetworkException
     */
    fun update() = scope.launch {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.e(TAG, "No internet connection: $exception")
        }

        supervisorScope {
            launch(handler) { parse(Codex.UK) }
            launch(handler) { parse(Codex.UPK) }
            launch(handler) { parse(Codex.KoAP) }
            launch(handler) { parse(Codex.PIKoAP) }
        }
    }

    /**
     * The function of connecting to a web resource of a certain code.
     *
     * Updates the values of the maps of a certain code.
     * @param codex certain code from enum [Codex]
     */
    private fun parse(codex: Codex){
        val document = Jsoup.connect(codex.URL).get()
        val elements = document.select("main").select("p")

        changesCount[codex] = getChangeCount(elements)
        lastChangeDate[codex] = getChangeDate(elements)
        Log.d(TAG, "Parse ${codex.name}: ${lastChangeDate[codex]}, ${changesCount[codex]}")
    }

    /**
     * The function counts the number of changes.
     *
     * Changes received from the web resource.
     * @param elements array of elements received from the web resource
     * @return count of elements with changes
     */
    private fun getChangeCount(elements: Elements): Int {
        var changesCount = 0
        for (element in elements) {
            if (element.attr("class").equals("changeadd")) {
                changesCount++
            }
        }

        return changesCount
    }

    /**
     * Function to get the date of the last element.
     *
     * Changes received from the web resource.
     * @param elements array of elements received from the web resource
     * @return date of last change
     */
    private fun getChangeDate(elements: Elements): String {
        var lastChangeDate = ""
        for (element in elements) {
            if (element.attr("class").equals("changeadd")
                && !element.nextElementSibling()!!.attr("class").equals("changeadd")) {
                lastChangeDate = formatDate(element.text())
            }
        }

        return lastChangeDate
    }

    /**
     * The function excludes everything except the date from the received string with the change.
     * @param text string storing info about code change
     * @return date from input string
     */
    private fun formatDate(text: String): String {
        return text.substring(text.indexOf("от"), text.indexOf("г.") - 1)
            .replace("от", "От")
    }

    /**
     * The function of checking a certain code for changes.
     *
     * Old data is taken from parameter file.
     * @param codex certain code from enum [Codex]
     * @return state of code change
     */
    fun isHaveChanges(codex: Codex): Boolean {
        val oldCountOfChanges = Preferences.getCodexChangesCount(codex)
        return changesCount[codex] != oldCountOfChanges
    }

    /**
     * The function of checking all codes for changes.
     * @return state of codes changes, it will be true if at least one code changed
     */
    fun isHaveChanges(): Boolean {
        return isHaveChanges(Codex.UK) ||
               isHaveChanges(Codex.UPK) ||
               isHaveChanges(Codex.KoAP) ||
               isHaveChanges(Codex.PIKoAP)
    }

    /**
     * Function to get the number of changes of a certain code.
     * @param codex certain code from enum [Codex]
     * @return changes count of certain code
     */
    fun getChangesCount(codex: Codex): Int = changesCount[codex]!!

    /**
     * Function to get the last change's date of a certain code.
     * @param codex certain code from enum [Codex]
     * @return last change's date of certain code
     */
    fun getChangeDate(codex: Codex): String = lastChangeDate[codex]!!
}
