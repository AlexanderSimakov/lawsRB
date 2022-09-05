package com.team.lawsrb.basic.htmlParser

import android.util.Log
import com.team.lawsrb.MainActivity
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object CodexVersionParser {

    private var document: Document? = null
    private const val TAG = "CodexVersionParser"
    private const val DATE_KEY = "date_"

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
        val sharedPrefCodexVersions = MainActivity.sharedPrefCodexVersions
        var oldCountOfElements = 0
        if (sharedPrefCodexVersions.contains(codex.name))
        {
            oldCountOfElements = sharedPrefCodexVersions.getInt(codex.name, countOfElements)
        }

        var oldDateOfLastChange: String? = ""
        if (sharedPrefCodexVersions.contains("$DATE_KEY${codex.name}"))
        {
            oldDateOfLastChange = sharedPrefCodexVersions.getString("$DATE_KEY${codex.name}", dateOfLastChange)
        }

        Log.d(TAG, "$oldCountOfElements")
        Log.d(TAG, oldDateOfLastChange!!)
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