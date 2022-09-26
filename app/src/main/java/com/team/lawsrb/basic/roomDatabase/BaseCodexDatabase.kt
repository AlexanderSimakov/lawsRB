package com.team.lawsrb.basic.roomDatabase

import android.content.Context
import com.team.lawsrb.basic.htmlParser.Codex
import com.team.lawsrb.basic.htmlParser.CodexLists
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * [BaseCodexDatabase] is a *object* class that initialize
 * UK, UPK, KoAP and PIKoAP [CodexDatabase]s,
 * provide single access point to them, and contain some general methods.
 *
 * @see [CodexDatabase]
 */
object BaseCodexDatabase {

    /** [CoroutineScope] for executing async operations */
    private val coroutine = CoroutineScope(Dispatchers.Main)

    /** Contain file names for each [CodexDatabase]. */
    private val databaseNames: MutableMap<Codex, String> = mutableMapOf()

    /** Contain paths in assets/ to each [CodexDatabase]. */
    private val assetPaths: MutableMap<Codex, String> = mutableMapOf()

    private var UKInstance: CodexDatabase? = null
    private var UPKInstance: CodexDatabase? = null
    private var KoAPInstance: CodexDatabase? = null
    private var PIKoAPInstance: CodexDatabase? = null

    /**
     * Use this property to access UK [CodexDatabase].
     *
     * Also you can use [BaseCodexDatabase.get] method for this purpose.
     *
     * @throws IllegalAccessException if [CodexDatabase] was not initialized.
     */
    val UK: CodexDatabase
        get() {
            if (UKInstance != null) return UKInstance as CodexDatabase
            else throw IllegalAccessException("Cannot access to UK database because class was not initialized")
        }

    /**
     * Use this property to access UPK [CodexDatabase].
     *
     * Also you can use [BaseCodexDatabase.get] method for this purpose.
     *
     * @throws IllegalAccessException if [CodexDatabase] was not initialized.
     */
    val UPK: CodexDatabase
        get() {
            if (UPKInstance != null) return UPKInstance as CodexDatabase
            else throw IllegalAccessException("Cannot access to UPK database because class was not initialized")
        }

    /**
     * Use this property to access KoAP [CodexDatabase].
     *
     * Also you can use [BaseCodexDatabase.get] method for this purpose.
     *
     * @throws IllegalAccessException if [CodexDatabase] was not initialized.
     */
    val KoAP: CodexDatabase
        get() {
            if (KoAPInstance != null) return KoAPInstance as CodexDatabase
            else throw IllegalAccessException("Cannot access to KoAP database because class was not initialized")
        }

    /**
     * Use this property to access PIKoAP [CodexDatabase].
     *
     * Also you can use [BaseCodexDatabase.get] method for this purpose.
     *
     * @throws IllegalAccessException if [CodexDatabase] was not initialized.
     */
    val PIKoAP: CodexDatabase
        get() {
            if (PIKoAPInstance != null) return PIKoAPInstance as CodexDatabase
            else throw IllegalAccessException("Cannot access to PIKoAP database because class was not initialized")
        }

    init {
        for (codex in Codex.values()){
            databaseNames[codex] = "${codex.name}_database.db"
            assetPaths[codex] = "database/${codex.name}_database.db"
        }
    }

    /**
     * This method initialize UK, UPK, KoAP and PIKoAP [CodexDatabase]s.
     *
     * Call it before using any other methods.
     */
    fun init(context: Context){
        if (UKInstance == null) UKInstance = getCodexDatabase(context, Codex.UK)
        if (UPKInstance == null) UPKInstance = getCodexDatabase(context, Codex.UPK)
        if (KoAPInstance == null) KoAPInstance = getCodexDatabase(context, Codex.KoAP)
        if (PIKoAPInstance == null) PIKoAPInstance = getCodexDatabase(context, Codex.PIKoAP)
    }

    /**
     * Build and return [CodexDatabase] by given [Codex],
     * also it use [Context] for [androidx.room.Room.databaseBuilder].
     */
    private fun getCodexDatabase(context: Context, codex: Codex): CodexDatabase{
        synchronized(this) {
            return androidx.room.Room.databaseBuilder(
                context.applicationContext,
                CodexDatabase::class.java,
                databaseNames[codex]!!
            ).createFromAsset(assetPaths[codex]!!)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
    }

    /**
     * Return [CodexDatabase] by given [Codex].
     *
     * You can also use [BaseCodexDatabase] property [UK], [UPK], [KoAP], [PIKoAP]
     * for this purpose.
     */
    fun get(codex: Codex): CodexDatabase{
        return when(codex){
            Codex.UK -> UK
            Codex.UPK -> UPK
            Codex.KoAP -> KoAP
            Codex.PIKoAP -> PIKoAP
        }
    }

    /**
     * Update [CodexDatabase] chosen by given [Codex] with given [CodexLists].
     * It transfer all favorites from old database to new.
     */
    fun update(codex: Codex, codexLists: CodexLists){
        coroutine.launch {
            val favorites = get(codex).articlesDao().getFavorites()
            val articles = codexLists.articles

            for (article in articles){
                if (article.title in favorites.map { it.title }){
                    article.isLiked = true
                }
            }

            codexLists.articles = articles
            insertCodexLists(codex, codexLists)
        }
    }

    /**
     * Insert given [CodexLists] to [CodexDatabase] chosen by [Codex].
     */
    private fun insertCodexLists(codex: Codex, codexLists: CodexLists){
        val db = get(codex)
        coroutine.launch {
            db.partsDao().insert(codexLists.parts)
            db.sectionsDao().insert(codexLists.sections)
            db.chaptersDao().insert(codexLists.chapters)
            db.articlesDao().insert(codexLists.articles)
        }
    }

    /**
     * Choose [CodexDatabase] by given [Codex] and clear it.
     *
     * **Attention:** this changes cannot be undone.
     */
    fun clear(codex: Codex){
        val db = get(codex)
        coroutine.launch {
            db.articlesDao().clearAll()
            db.chaptersDao().clearAll()
            db.sectionsDao().clearAll()
            db.partsDao().clearAll()
        }
    }

    /**
     * Clear all [CodexDatabase]s *([UK], [UPK], [KoAP], [PIKoAP]).*
     *
     * **Attention:** this changes cannot be undone.
     */
    fun clearAll(){
        clear(Codex.UK)
        clear(Codex.UPK)
        clear(Codex.KoAP)
        clear(Codex.PIKoAP)
    }
}
