package com.gatheringandyou.gandyoudemo.bulletinboards

import java.sql.Timestamp
import java.util.*

data class PostData(
    val freeboard_title: String,
    val freeboard_content: String,
    val userid: Int,
    val username: String,
    val useremail: String,
    val freeboard_date: String
)
