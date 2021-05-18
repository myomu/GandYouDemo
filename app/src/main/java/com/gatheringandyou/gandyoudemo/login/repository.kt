package com.gatheringandyou.gandyoudemo.login

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object repository {
    private const val BASE_URL = "http://ec2-3-34-248-232.ap-northeast-2.compute.amazonaws.com:8080/"
    var retrofit: Retrofit? = null

    fun getApiClient(): Retrofit? {
        val gson =
                GsonBuilder()
                        .setLenient()
                        .create()

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
        }
        return retrofit

    }
}
//레트로핏의 기본 구조 오브젝트로 만든 이유