package com.gatheringandyou.gandyoudemo.shared

data class UserDataResponse(
    val data: MutableList<UserData>,
    val code: Int,
    val message: String
)
