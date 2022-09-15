package com.team.lawsrb.basic.htmlParser

import android.util.Log
import com.team.lawsrb.basic.Preferences
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements

object CodexVersionParser {
    private const val TAG = "CodexVersionParser"

    private val scope = CoroutineScope (Dispatchers.IO + SupervisorJob ())

    private var changesCount: MutableMap<Codex, Int> =
        mutableMapOf(
            Codex.UK to 82,
            Codex.UPK to 61,
            Codex.KoAP to 1,
            Codex.PIKoAP to 1
        )

    private var lastChangeDate: MutableMap<Codex, String> =
        mutableMapOf(
            Codex.UK to "От 13 мая 2022",
            Codex.UPK to "От 20 июля 2022",
            Codex.KoAP to "От 4 января 2022",
            Codex.PIKoAP to "От 4 января 2022"
        )

    fun update() = scope.launch {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.e(TAG, "$exception: No internet connection")
        }

        supervisorScope {
            launch(handler) { parse(Codex.UK) }
            launch(handler) { parse(Codex.UPK) }
            launch(handler) { parse(Codex.KoAP) }
            launch(handler) { parse(Codex.PIKoAP) }
        }
    }

    private fun parse(codex: Codex){
        val document = Jsoup.connect(codex.URL).get()
        val elements = document.select("main").select("p")

        changesCount[codex] = getChangeCount(elements)
        lastChangeDate[codex] = getChangeDate(elements)
        Log.d(TAG, "Parse ${codex.name}: ${lastChangeDate[codex]}, ${changesCount[codex]}")
    }

    private fun getChangeCount(elements: Elements): Int {
        var changesCount = 0
        for (element in elements) {
            if (element.attr("class").equals("changeadd")) {
                changesCount++
            }
        }

        return changesCount
    }

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

    private fun formatDate(text: String): String {
        return text.substring(text.indexOf("от"), text.indexOf("г.") - 1)
            .replace("от", "От")
    }

    fun isHaveChanges(codex: Codex): Boolean {
        val oldCountOfChanges = Preferences.getCodexVersion(codex)
        return changesCount[codex] != oldCountOfChanges
    }

    fun isHaveChanges(): Boolean {
        return isHaveChanges(Codex.UK) ||
               isHaveChanges(Codex.UPK) ||
               isHaveChanges(Codex.KoAP) ||
               isHaveChanges(Codex.PIKoAP)
    }

    fun getChangesCount(codex: Codex): Int = changesCount[codex]!!

    fun getChangeDate(codex: Codex): String = lastChangeDate[codex]!!
}
