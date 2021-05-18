package com.gatheringandyou.gandyoudemo.login

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Logininterface {




        @POST("/user/login")
        fun getLogin(
                @Body data:Login
        ): Call<loginResponse>



    }
