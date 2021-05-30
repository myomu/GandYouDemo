package com.gatheringandyou.gandyoudemo.matching

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MatchInterface
{
    @POST("/user/getmatchuser")
    fun getMatchingUser(
        @Body hobby: Hobby

    ): Call<UserResponse>
}