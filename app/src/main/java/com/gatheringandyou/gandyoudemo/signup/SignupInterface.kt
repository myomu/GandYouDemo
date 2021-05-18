package com.gatheringandyou.gandyoudemo.signup

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.zip.DataFormatException

interface SignupInterface
{


                    @POST("/user/signup")
                    fun getSignUp(
                    @Body data:Signup
    ): Call<SignupResponse>



}