package com.team.lawsrb.basic.htmlParser

import android.util.Log
import com.team.lawsrb.basic.Preferences
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.coroutines.CoroutineContext

object CodexVersionParser {
    private val context: CoroutineContext = Dispatchers.IO
    private val scope = CoroutineScope (context + SupervisorJob ())

    private var documentUK: Document? = null
    private var documentUPK: Document? = null
    private var documentKoAP: Document? = null
    private var documentPIKoAP: Document? = null

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

    private const val TAG = "CodexVersionParser"

    suspend fun update() = scope.launch {
        val handler = CoroutineExceptionHandler {
                _, exception -> Log.e(TAG, "$exception: No internet connection")
        }
        supervisorScope {
            launch(handler) {
                documentUK = Jsoup.connect(Codex.UK.URL).get()
                changesCount[Codex.UK] = getChangesCountFromHtmlPage(documentUK!!)
                lastChangeDate[Codex.UK] = getLastChangeDateFromHtmlPage(documentUK!!)
                Log.d(TAG, "${changesCount[Codex.UK]}")
            }
            
            launch(handler) {
                documentUPK = Jsoup.connect(Codex.UPK.URL).get()
                changesCount[Codex.UPK] = getChangesCountFromHtmlPage(documentUPK!!)
                lastChangeDate[Codex.UPK] = getLastChangeDateFromHtmlPage(documentUPK!!)
                Log.d(TAG, "${changesCount[Codex.UPK]}")
            }

            launch (handler) {
                documentKoAP = Jsoup.connect(Codex.KoAP.URL).get()
                changesCount[Codex.KoAP] = getChangesCountFromHtmlPage(documentKoAP!!)
                lastChangeDate[Codex.KoAP] = getLastChangeDateFromHtmlPage(documentKoAP!!)
                Log.d(TAG, "${changesCount[Codex.KoAP]}")
            }
            
            launch (handler) {
                documentPIKoAP = Jsoup.connect(Codex.PIKoAP.URL).get()
                changesCount[Codex.PIKoAP] = getChangesCountFromHtmlPage(documentPIKoAP!!)
                lastChangeDate[Codex.PIKoAP] = getLastChangeDateFromHtmlPage(documentPIKoAP!!)
                Log.d(TAG, "${changesCount[Codex.PIKoAP]}")
            }
        }
    }

    fun isHaveChanges(codex: Codex): Boolean {
        val oldCountOfChanges = Preferences.getCodexVersion(codex)
        Log.d(TAG, "$oldCountOfChanges")
        return changesCount[codex] != oldCountOfChanges
    }

    fun getChangesCount(codex: Codex): Int = changesCount[codex]!!

    fun getChangeDate(codex: Codex): String = lastChangeDate[codex]!!

    private fun getChangesCountFromHtmlPage(document: Document): Int {
        var changesCount = 0
        val mainTable = document.select("main")
        val elements = mainTable.select("p")

        for (element in elements) {
            if (element.attr("class").equals("changeadd")) {
                changesCount++
            }
        }

        return changesCount
    }

    private fun getLastChangeDateFromHtmlPage(document: Document): String {
        var lastChangeDate = ""
        val mainTable = document.select("main")
        val elements = mainTable.select("p")

        for (element in elements) {
            if (element.attr("class").equals("changeadd")
                && !element.nextElementSibling().attr("class").equals("changeadd")) {
                lastChangeDate = element.text()
                lastChangeDate = leaveDateOnly(lastChangeDate)
                Log.d(TAG, lastChangeDate)
            }
        }

        return lastChangeDate
    }

    private fun leaveDateOnly(line: String): String {
        var toDate = line

        toDate = toDate.substring(toDate.indexOf("от"), toDate.indexOf("г."))
        toDate = toDate.replace("от", "От")
        toDate = toDate.substring(0, toDate.length - 1)

        return toDate
    }
}