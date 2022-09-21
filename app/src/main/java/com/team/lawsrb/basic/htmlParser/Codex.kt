package com.team.lawsrb.basic.htmlParser

/** The enumeration stores a link to the web resource of each code and his name in Cyrillic */
enum class Codex(
    val URL: String,
    val rusName: String
) {
    UK("https://etalonline.by/document/?regnum=HK9900275", "УК"),
    UPK("https://etalonline.by/document/?regnum=HK9900295", "УПК"),
    KoAP("https://etalonline.by/document/?regnum=hk2100091&q_id=5677256", "КоАП"),
    PIKoAP("https://etalonline.by/document/?regnum=hk2100092&q_id=5677256", "ПИКоАП")
}