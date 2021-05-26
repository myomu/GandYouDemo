package com.gatheringandyou.gandyoudemo.shared

import com.gatheringandyou.gandyoudemo.profile.EditProfileResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserDataInterface {

    @POST("/userData/getUserData")
    fun postUserEmail(
        @Body data: PostEmail
    ): Call<UserDataResponse>

    @POST("/userData/editProfile")
    fun postProfileData(
        @Body data: PostProfileData
    ): Call<EditProfileResponse>

}