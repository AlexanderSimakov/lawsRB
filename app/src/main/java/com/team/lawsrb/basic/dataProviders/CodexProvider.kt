package com.team.lawsrb.basic.dataProviders

import com.team.lawsrb.basic.codexObjects.*

/**
 * Description
 */
interface CodexProvider{

    /**
     * Return All [Part]s from codex.
     * @return All [Part]s
     */
    fun getParts(): MutableList<Part>

    /**
     * @return All [Section]s.
     */
    fun getSections(): MutableList<Section>

    /**
     * Find [Section]s which contains input [Part].
     *
     * @param part Using for search its [Section]s.
     * @return All [Section]s of input [Part]
     */
    fun getSections(part: Part): MutableList<Section>

    /**
     * Description
     *
     * @return param
     */
    fun getChapters(): MutableList<Chapter>

    /**
     * Description
     *
     * @param section description
     * @return param
     */
    fun getChapters(section: Section): MutableList<Chapter>

    /**
     * Description
     *
     * @return param
     */
    fun getArticles(): MutableList<Article>

    /**
     * Description
     *
     * @param chapter description
     * @return param
     */
    fun getArticles(chapter: Chapter): MutableList<Article>
}