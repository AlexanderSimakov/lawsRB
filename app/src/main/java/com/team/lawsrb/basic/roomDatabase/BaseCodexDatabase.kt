package com.team.lawsrb.basic.roomDatabase

import android.content.Context
import com.team.lawsrb.basic.htmlParser.Codex
import com.team.lawsrb.basic.htmlParser.CodexLists

object BaseCodexDatabase {
    private val databaseNames: MutableMap<Codex, String> = mutableMapOf()
    private val assetPaths: MutableMap<Codex, String> = mutableMapOf()

    private var UKInstance: CodexDatabase? = null
    private var UPKInstance: CodexDatabase? = null
    private var KoAPInstance: CodexDatabase? = null
    private var PIKoAPInstance: CodexDatabase? = null

    init {
        for (codex in Codex.values()){
            databaseNames[codex] = "${codex.name}_database.db"
            assetPaths[codex] = "database/${codex.name}_database.db"
        }
    }

    val UK: CodexDatabase
        get() {
            if (UKInstance != null) return UKInstance as CodexDatabase
            else throw IllegalAccessException("Cannot access to UK database because class was not initialized")
        }

    val UPK: CodexDatabase
        get() {
            if (UPKInstance != null) return UPKInstance as CodexDatabase
            else throw IllegalAccessException("Cannot access to UPK database because class was not initialized")
        }

    val KoAP: CodexDatabase
        get() {
            if (KoAPInstance != null) return KoAPInstance as CodexDatabase
            else throw IllegalAccessException("Cannot access to KoAP database because class was not initialized")
        }

    val PIKoAP: CodexDatabase
        get() {
            if (PIKoAPInstance != null) return PIKoAPInstance as CodexDatabase
            else throw IllegalAccessException("Cannot access to PIKoAP database because class was not initialized")
        }

    fun init(context: Context){
        if (UKInstance == null) UKInstance = getCodexDatabase(context, Codex.UK)
        if (UPKInstance == null) UPKInstance = getCodexDatabase(context, Codex.UPK)
        if (KoAPInstance == null) KoAPInstance = getCodexDatabase(context, Codex.KoAP)
        if (PIKoAPInstance == null) PIKoAPInstance = getCodexDatabase(context, Codex.PIKoAP)
    }

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

    fun getDatabase(codex: Codex): CodexDatabase{
        return when(codex){
            Codex.UK -> UK
            Codex.UPK -> UPK
            Codex.KoAP -> KoAP
            Codex.PIKoAP -> PIKoAP
        }
    }

    fun insertCodexLists(codex: Codex, codexLists: CodexLists){
        val db = getDatabase(codex)
        db.partsDao().insert(codexLists.parts)
        db.sectionsDao().insert(codexLists.sections)
        db.chaptersDao().insert(codexLists.chapters)
        db.articlesDao().insert(codexLists.articles)
    }

    fun clearDatabase(codex: Codex){
        val db = getDatabase(codex)
        db.articlesDao().clearAll()
        db.chaptersDao().clearAll()
        db.sectionsDao().clearAll()
        db.partsDao().clearAll()
    }

    fun clearDatabases(){
        clearDatabase(Codex.UK)
        clearDatabase(Codex.UPK)
        clearDatabase(Codex.KoAP)
        clearDatabase(Codex.PIKoAP)
    }
}
