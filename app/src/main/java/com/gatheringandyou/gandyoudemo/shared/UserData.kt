package com.gatheringandyou.gandyoudemo.shared

data class UserData(

    val user_id: Int,
    val email: String,
    val nickname: String,
    val gender: String,
    val department: String,
    val age: Int,
    val hobby1: String,
    val hobby2: String,
    val hobby3: String

)

data class PostEmail(

    val email: String?

    )

data class PostProfileData(

    val email: String?,
    val nickname: String,
    val department: String,
    val age: Int,
    val hobby1: String,
    val hobby2: String,
    val hobby3: String

)
