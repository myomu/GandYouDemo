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

    @POST("/community/deleteBulletin")
    fun deleteBulletin(
        @Body data: DataCollection.DeleteBulletin
    ): Call<DataCollection.DeleteBulletinResponse>

    @POST("/community/updateBulletin")
    fun updateBulletin(
        @Body data: DataCollection.UpdateBulletin
    ): Call<DataCollection.UpdateBulletinResponse>

    @POST("/community/extensionBulletinGet")
    fun getExtensionBulletinData(
        @Body data: DataCollection.PostFreeBoardId
    ): Call<FreeBoardResponse>

    @POST("/community/sendBulletinReport")
    fun sendReportData(
        @Body data: DataCollection.SendReportData
    ): Call<DataCollection.ReportResponse>

}