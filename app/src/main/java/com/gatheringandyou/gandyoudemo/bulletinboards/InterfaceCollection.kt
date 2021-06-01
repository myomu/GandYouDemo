package com.gatheringandyou.gandyoudemo.bulletinboards

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface InterfaceCollection {

    @POST("/community/getCommentsData")
    fun getCommentsData(
        @Body data: DataCollection.PostFreeBoardId
    ): Call<DataCollection.GetCommentsResponse>

    @POST("/community/sendCommentsData")
    fun sendCommentsData(
        @Body data: DataCollection.SendCommentsData
    ): Call<DataCollection.SendCommentsResponse>

    @POST("/community/deleteCommentsData")
    fun deleteCommentsData(
        @Body data: DataCollection.DeleteCommentsData
    ): Call<DataCollection.DeleteCommentsResponse>

}