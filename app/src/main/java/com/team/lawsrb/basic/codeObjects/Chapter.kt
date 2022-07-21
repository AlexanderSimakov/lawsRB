package com.team.lawsrb.basic.codeObjects

data class Chapter(val id: Int,
                   val sectionId: Int,
                   val title: String,
                   var isLiked: Boolean = false)