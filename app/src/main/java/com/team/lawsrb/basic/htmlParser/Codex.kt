package com.team.lawsrb.basic.htmlParser

/**
 * The enum stores the url per code
 */
enum class Codex(
    val URL: String,
    val rusName: String
) {
    UK("https://etalonline.by/document/?regnum=HK9900275", "УК"),
    UPK("https://etalonline.by/document/?regnum=HK9900295", "УПК"),
    KoAP("https://etalonline.by/document/?regnum=hk2100091&q_id=5677256", "КоАП"),
    PIKoAP("https://etalonline.by/document/?regnum=hk2100092&q_id=5677256", "ПИКоАП")
}