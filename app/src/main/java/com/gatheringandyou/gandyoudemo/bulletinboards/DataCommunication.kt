package com.gatheringandyou.gandyoudemo.bulletinboards

import android.util.Log
import android.widget.Toast
import com.gatheringandyou.gandyoudemo.login.repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DataCommunication {

    lateinit var freeboardapi: FreeBoardInterface

    fun loadFreeboardData(mCallback: FreeBoard){

        val retrofit = repository.getApiClient()
        if (retrofit != null) {
            freeboardapi = retrofit.create(FreeBoardInterface::class.java)
        }


        var call: Call<FreeBoardResponse> = freeboardapi.getFreeBoardData()
        call.enqueue(object: Callback<FreeBoardResponse> {

            override fun onResponse(call: Call<FreeBoardResponse>, response: Response<FreeBoardResponse>) {
                if (response.isSuccessful && response.body() != null)
                {

                    if(response.body()!!.code == 200){
                        Toast.makeText(mCallback, response.body()!!.message, Toast.LENGTH_SHORT).show()

                        //서버에서 받아온 데이터를 loadComplete함수에 MutableList<FreeBoardData> 형식으로 넘긴다.
                        //이 방식은 retrofit이 enqueue로 비동기 방식(백그라운드에서 함수가 돌아감) 때문에 전역변수로 데이터 값을 저장할 수 없다.
                        //그래서 아래와 같이 따로 함수를 생성하고 그 함수가 데이터를 받아 BoardAdapter로 데이터를 넘겨준다.
                        mCallback.loadComplete(response.body()!!.data)

                    }else{
                        Toast.makeText(mCallback, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<FreeBoardResponse>, t: Throwable) {
                t.message?.let { Log.e("onFailure", it) }
            }

        })
    }

}