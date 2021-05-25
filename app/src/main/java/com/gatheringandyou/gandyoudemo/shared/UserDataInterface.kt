package com.gatheringandyou.gandyoudemo.shared

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserDataInterface {

    @POST("/getUserData")
    fun postUserEmail(
        @Body data: PostEmail
    ): Call<UserDataResponse>

}