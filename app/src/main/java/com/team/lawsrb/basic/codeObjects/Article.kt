package com.team.lawsrb.basic.codeObjects

data class Article(val id: Int, val chapterId: Int, val title: String, var isLiked: Boolean = false)