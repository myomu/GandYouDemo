package com.gatheringandyou.gandyoudemo.bulletinboards


import java.util.*

data class FreeBoardData(
    val id_freeboard: Int,
    val freeboard_title: String,
    val freeboard_content: String,
    val freeboard_date: Date,
    val freeboard_like_count: Int,
    val freeboard_comments_count: Int,
    val userid: Int,
    val username: String
    )
