package com.gatheringandyou.gandyoudemo.bulletinboards

data class FreeBoardResponse(
    val data: MutableList<FreeBoardData>,
    val code: Int,
    val message: String
)
