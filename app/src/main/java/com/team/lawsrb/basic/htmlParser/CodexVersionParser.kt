/**
 * The singleton class provides information about all changes to existing codes
 * @author by tokyolem
 */

package com.team.lawsrb.basic.htmlParser

import android.util.Log
import com.team.lawsrb.basic.Preferences
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements

object CodexVersionParser {
    /** Log tag field */
    private const val TAG = "CodexVersionParser"

    /** Custom coroutine creation field */
    private val scope = CoroutineScope (Dispatchers.IO + SupervisorJob ())

    /** Map storing the number of code changes by default */
    private var changesCount: MutableMap<Codex, Int> =
        mutableMapOf(
            Codex.UK to 82,
            Codex.UPK to 61,
            Codex.KoAP to 1,
            Codex.PIKoAP to 1
        )

    /** Map storing the date of last change by default */
    private var lastChangeDate: MutableMap<Codex, String> =
        mutableMapOf(
            Codex.UK to "От 13 мая 2022",
            Codex.UPK to "От 20 июля 2022",
            Codex.KoAP to "От 4 января 2022",
            Codex.PIKoAP to "От 4 января 2022"
        )

    /**
     * Function for updating values in maps, works async
     * @exception CodexVersionParser - can throw an NetworkException
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
     * The function of connecting to a web resource of a certain codex
     * Updates the value of the maps of a certain codex
     * @param codex certain code from enum [Codex]
     * @see Codex
     */
    private fun parse(codex: Codex){
        val document = Jsoup.connect(codex.URL).get()
        val elements = document.select("main").select("p")

        changesCount[codex] = getChangeCount(elements)
        lastChangeDate[codex] = getChangeDate(elements)
        Log.d(TAG, "Parse ${codex.name}: ${lastChangeDate[codex]}, ${changesCount[codex]}")
    }

    /**
     * The function counts the number of changes
     * Changes received from the web resource in the method [parse]
     * @param elements array of elements received from the web resource
     * @return count of elements with changes
     * @see parse
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
     * Function to get the date of the last element
     * Changes received from the web resource in the method [parse]
     * @param elements array of elements received from the web resource
     * @return date of last change
     * @see parse
     */
    private fun getChangeDate(elements: Elements): String {
        var lastChangeDate = ""
        for (element in elements) {
            if (element.attr("class").equals("changeadd")
                && !element.nextElementSibling().attr("class").equals("changeadd")) {
                lastChangeDate = formatDate(element.text())
            }
        }

        return lastChangeDate
    }

    /**
     * The function excludes everything except the date from the received string with the change
     * @param text string storing info about code change
     * @return date from input string
     * @see getChangeDate
     */
    private fun formatDate(text: String): String {
        return text.substring(text.indexOf("от"), text.indexOf("г.") - 1)
            .replace("от", "От")
    }

    /**
     * The function of checking a certain code for changes,
     * old data is taken from parameter file
     * @param codex certain code from enum [Codex]
     * @see Codex
     */
    fun isHaveChanges(codex: Codex): Boolean {
        val oldCountOfChanges = Preferences.getCodexVersion(codex)
        return changesCount[codex] != oldCountOfChanges
    }

    /**
     * The function of checking codes for changes
     * @return state of codes change
     * @see isHaveChanges
     */
    fun isHaveChanges(): Boolean {
        return isHaveChanges(Codex.UK) ||
               isHaveChanges(Codex.UPK) ||
               isHaveChanges(Codex.KoAP) ||
               isHaveChanges(Codex.PIKoAP)
    }

    /**
     * Function to get the number of changes of a certain code
     * Data is taken from the map [changesCount]
     * @param codex certain code from enum [Codex]
     * @return changes count of certain code
     * @see Codex
     * @see changesCount
     */
    fun getChangesCount(codex: Codex): Int = changesCount[codex]!!

    /**
     * Function to get the last change's date of a certain code
     * Data is taken from the map [lastChangeDate]
     * @param codex certain code from enum [Codex]
     * @return last change's date of certain code
     * @see Codex
     * @see lastChangeDate
     */
    fun getChangeDate(codex: Codex): String = lastChangeDate[codex]!!
}
