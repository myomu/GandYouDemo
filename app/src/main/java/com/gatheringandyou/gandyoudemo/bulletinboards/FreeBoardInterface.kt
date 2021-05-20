package com.gatheringandyou.gandyoudemo.bulletinboards

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FreeBoardInterface {

    @GET("/community/freeBoardGet")
    fun getFreeBoardData(
        //@Body data: FreeBoardData
    ): Call<FreeBoardResponse>

}