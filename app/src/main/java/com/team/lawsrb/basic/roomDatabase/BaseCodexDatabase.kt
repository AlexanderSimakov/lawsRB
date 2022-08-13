package com.team.lawsrb.basic.roomDatabase

import android.content.Context

object BaseCodexDatabase {
    private enum class Codex { UK, UPK, KoAP, PIKoAP }

    val UK: CodexDatabase
        get() {
            if (UKInstance != null){
                return UKInstance as CodexDatabase
            }else{
                throw IllegalAccessException("Fuck u")
            }
        }

    val UPK: CodexDatabase
        get() {
            if (UPKInstance != null){
                return UPKInstance as CodexDatabase
            }else{
                throw IllegalAccessException("Fuck u")
            }
        }

    val KoAP: CodexDatabase
        get() {
            if (KoAPInstance != null){
                return KoAPInstance as CodexDatabase
            }else{
                throw IllegalAccessException("Fuck u")
            }
        }

    val PIKoAP: CodexDatabase
        get() {
            if (PIKoAPInstance != null){
                return PIKoAPInstance as CodexDatabase
            }else{
                throw IllegalAccessException("Fuck u")
            }
        }

    private var UKInstance: CodexDatabase? = null
    private var UPKInstance: CodexDatabase? = null
    private var KoAPInstance: CodexDatabase? = null
    private var PIKoAPInstance: CodexDatabase? = null

    fun init(context: Context){
        if (UKInstance == null) UKInstance = getCodexDatabase(context, Codex.UK)
        if (UPKInstance == null) UPKInstance = getCodexDatabase(context, Codex.UPK)
        if (KoAPInstance == null) KoAPInstance = getCodexDatabase(context, Codex.KoAP)
        if (PIKoAPInstance == null) PIKoAPInstance = getCodexDatabase(context, Codex.PIKoAP)
    }

    private fun getCodexDatabase(context: Context, codex: Codex): CodexDatabase{
        val databaseName = when (codex){
            Codex.UK -> "UK_database.db"
            Codex.UPK -> "UPK_database.db"
            Codex.KoAP -> "KoAP_database.db"
            Codex.PIKoAP -> "PIKoAP_database.db"
        }

        val assetsPath = when (codex){
            Codex.UK -> "database/codex_database"
            Codex.UPK -> "database/codex_database"
            Codex.KoAP -> "database/codex_database"
            Codex.PIKoAP -> "database/codex_database"
        }

        synchronized(this) {
            return androidx.room.Room.databaseBuilder(
                context.applicationContext,
                CodexDatabase::class.java,
                databaseName
            ).createFromAsset(assetsPath)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
    }
}
