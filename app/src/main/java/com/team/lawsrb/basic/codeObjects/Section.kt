package com.team.lawsrb.basic.codeObjects

data class Section(val id: Int,
                   val partId: Int,
                   val title: String,
                   var isLiked: Boolean = false)