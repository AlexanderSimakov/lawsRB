package com.team.lawsrb.basic.htmlParser

import android.util.Log
import com.team.lawsrb.MainActivity
import com.team.lawsrb.basic.Preferences
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object CodexVersionParser {

    private var document: Document? = null
    private const val TAG = "CodexVersionParser"

    fun verifyForChanges(codex: Codex): Boolean{
        try
        {
            document = Jsoup.connect(codex.URL).get()
        }
        catch (e: Exception)
        {
            Log.e(TAG, "Failed getting document")
        }

        val countOfElements = getCountOfElementsWithChanges()
        val dateOfLastChange = getLastChangeDate()

        val oldCountOfElements = Preferences.getCodexVersion(codex)
        val oldDateOfLastChange: String = Preferences.getCodexUpdateDate(codex)

        Log.d(TAG, "$oldCountOfElements")
        Log.d(TAG, oldDateOfLastChange)
        if (countOfElements != oldCountOfElements)
        {
            //sharedPrefCodexVersions.edit().remove(codex.name).apply()
            //sharedPrefCodexVersions.edit().putInt(codex.name, countOfElements).apply()
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

    private fun getLastChangeDate(): String
    {
        var dateOfLastChange = ""
        val mainTable = document!!.select("main")
        val elements = mainTable.select("p")

        for (element in elements)
        {
            if (element.attr("class").equals("changeadd")
                && !element.nextElementSibling().attr("class").equals("changeadd"))
            {
                dateOfLastChange = element.text()
                dateOfLastChange = leaveDateOnly(dateOfLastChange)
                Log.d(TAG, dateOfLastChange)
            }
        }
        return dateOfLastChange
    }

    private fun leaveDateOnly(line: String): String
    {
        var toDate = line

        toDate = toDate.substring(toDate.indexOf("от"), toDate.indexOf("г."))
        toDate = toDate.replace("от", "От")
        toDate = toDate.substring(0, toDate.length - 1)

        return toDate
    }
}