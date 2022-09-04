package com.team.lawsrb.basic.htmlParser

import android.util.Log
import com.team.lawsrb.MainActivity
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object CodexVersionParser {

    private var document: Document? = null
    private const val TEST_LOG = "TestLog"
    private const val ERROR_LOG = "Error"

    fun get(codex: Codex): Boolean{
        try
        {
            document = Jsoup.connect(codex.URL).get()
        }
        catch (e: Exception)
        {
            Log.e(ERROR_LOG, "Failed getting document")
        }

        val countOfElements = getCountOfElementsWithChanges()
        val sharedPrefCodexVersions = MainActivity.mSettingCodexVersions
        var oldCountOfElements = 0
        if (sharedPrefCodexVersions.contains(codex.name))
        {
            oldCountOfElements = sharedPrefCodexVersions.getInt(codex.name, countOfElements)
        }

        Log.d(TEST_LOG, "$oldCountOfElements")
        if (countOfElements != oldCountOfElements)
        {
            sharedPrefCodexVersions.edit().remove(codex.name).apply()
            sharedPrefCodexVersions.edit().putInt(codex.name, countOfElements).apply()
            return true
        }

        return false
    }

    private fun getCountOfElementsWithChanges(): Int
    {
        var quantityOfElements = 0
        val mainTable = document!!.select("main")
        val elements = mainTable.select("p")

        for (element in elements)
        {
            if (element.attr("class").equals("changeadd"))
            {
                quantityOfElements++
            }
        }
        return quantityOfElements
    }
}