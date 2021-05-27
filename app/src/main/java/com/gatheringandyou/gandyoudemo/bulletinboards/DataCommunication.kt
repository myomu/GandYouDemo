package com.gatheringandyou.gandyoudemo.bulletinboards

import android.util.Log
import android.widget.Toast
import com.gatheringandyou.gandyoudemo.login.repository
import com.gatheringandyou.gandyoudemo.profile.EditProfileResponse
import com.gatheringandyou.gandyoudemo.profile.ProfileEditActivity
import com.gatheringandyou.gandyoudemo.shared.PostProfileData
import com.gatheringandyou.gandyoudemo.shared.PreferenceManger
import com.gatheringandyou.gandyoudemo.shared.UserDataInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DataCommunication {

    lateinit var freeboardapi: FreeBoardInterface
    lateinit var profileEditAPI: UserDataInterface

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


    //프로필 수정 데이터 DB로 전송
    fun postProfileData(activity: ProfileEditActivity, nickname: String, department: String, age: Int, hobby1: String, hobby2: String, hobby3: String ){

        val retrofit = repository.getApiClient()
        if (retrofit != null) {
            profileEditAPI = retrofit.create(UserDataInterface::class.java)
        }

        val userEmail = PreferenceManger(activity).getString("userEmail")

        val postData = PostProfileData(userEmail, nickname, department, age, hobby1, hobby2, hobby3)


        val call: Call<EditProfileResponse> = profileEditAPI.postProfileData(postData)
        call.enqueue(object: Callback<EditProfileResponse> {

            override fun onResponse(call: Call<EditProfileResponse>, response: Response<EditProfileResponse>) {
                if (response.isSuccessful && response.body() != null)
                {

                    if(response.body()!!.code == 200){
                        Toast.makeText(activity, response.body()!!.message, Toast.LENGTH_SHORT).show()



                    }else{
                        Toast.makeText(activity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                t.message?.let { Log.e("onFailure", it) }
            }

        })
    }

}