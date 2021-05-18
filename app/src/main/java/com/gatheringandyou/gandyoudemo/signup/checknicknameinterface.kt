package com.gatheringandyou.gandyoudemo.signup

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface checknicknameinterface {

    @POST("/user/checknick")
    fun checknick
            (
                    @Body data:checknickname
    ): Call<SignupResponse>
}