package com.gatheringandyou.gandyoudemo.matching

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MatchInterface
{
    @GET("/user/getmatchuser")
    fun getMatchingUser(
    @Query("hobby1") hobby1: String?,
    @Query("hobby2") hobby2: String?,
    @Query("hobby3") hobby3: String?
    ): Call<UserResponse>
}