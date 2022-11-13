package com.requestfordinner.lawsrb.basic.htmlParser

/**
 * The enumeration stores a link to the web resource of each code and his name in Cyrillic
 *
 * @property URL A link to the web-resource for specific codex.
 * @property rusName The russian abbreviation of specific codex.
 */
enum class Codex(
    val URL: String,
    val rusName: String
) {

    /** Represents Criminal Code. */
    UK("https://etalonline.by/document/?regnum=HK9900275", "УК"),

    /** Represents Code of Criminal Procedure. */
    UPK("https://etalonline.by/document/?regnum=HK9900295", "УПК"),

    /** Represents Code of Administrative Offences. */
    KoAP("https://etalonline.by/document/?regnum=hk2100091&q_id=5677256", "КоАП"),

    /** Represents Procedural-Executive Code on Administrative Offenses. */
    PIKoAP("https://etalonline.by/document/?regnum=hk2100092&q_id=5677256", "ПИКоАП");

    companion object {
        fun forEach(action: (Codex) -> Unit) {
            enumValues<Codex>().forEach(action)
        }
    }
}
