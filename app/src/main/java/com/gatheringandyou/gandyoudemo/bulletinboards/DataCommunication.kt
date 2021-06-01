package com.gatheringandyou.gandyoudemo.bulletinboards

import android.content.Context
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
import java.text.SimpleDateFormat
import java.util.*

object DataCommunication {

    lateinit var freeboardapi: FreeBoardInterface
    lateinit var profileEditAPI: UserDataInterface
    lateinit var dataApi: InterfaceCollection // 댓글 DB 통신. 댓글 가져오는 것

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

    fun loadCommentsData(mCallback: ExtensionActivity, freeBoardId: Int){

        val freeId = DataCollection.PostFreeBoardId(freeBoardId)
        //var listData: MutableList<DataCollection.GetCommentsData>

        val retrofit = repository.getApiClient()
        if (retrofit != null) {
            dataApi = retrofit.create(InterfaceCollection::class.java)
        }

        val call: Call<DataCollection.GetCommentsResponse> = dataApi.getCommentsData(freeId)
        call.enqueue(object: Callback<DataCollection.GetCommentsResponse> {

            override fun onResponse(call: Call<DataCollection.GetCommentsResponse>, response: Response<DataCollection.GetCommentsResponse>) {
                if (response.isSuccessful && response.body() != null)
                {

                    if(response.body()!!.code == 200){
                        Toast.makeText(mCallback, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        Log.d("성공 메세지", response.body()!!.message)
//                        Log.d("사용자 데이터 받아와지나?", response.body()!!.data.toString())

                        //데이터 불러와서 넘겨줌. 넘겨진 데이터는 리사이클러뷰 어댑터에 장착됨.
                        mCallback.loadCommentsComplete(response.body()!!.data)

                    }else{
                        Toast.makeText(mCallback, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<DataCollection.GetCommentsResponse>, t: Throwable) {
                t.message?.let { Log.e("onFailure", it) }
            }
        })
    }

    fun postCommentsData(mCallback: ExtensionActivity, comments: String, freeBoardId: Int) {
        val userId = PreferenceManger(mCallback).getInt("userId")
        val userEmail = PreferenceManger(mCallback).getString("userEmail").toString()
        val userNickname = PreferenceManger(mCallback).getString("userNickname").toString()

        val nowTime = Calendar.getInstance().time // 현재 시간
        val commentsDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(nowTime)

        val commentsData = DataCollection.SendCommentsData(userId, userEmail, userNickname, comments, commentsDate, freeBoardId)
        //var listData: MutableList<DataCollection.GetCommentsData>

        val retrofit = repository.getApiClient()
        if (retrofit != null) {
            dataApi = retrofit.create(InterfaceCollection::class.java)
        }

        val call: Call<DataCollection.SendCommentsResponse> = dataApi.sendCommentsData(commentsData)
        call.enqueue(object: Callback<DataCollection.SendCommentsResponse> {

            override fun onResponse(call: Call<DataCollection.SendCommentsResponse>, response: Response<DataCollection.SendCommentsResponse>) {
                if (response.isSuccessful && response.body() != null)
                {
                    if(response.body()!!.code == 200){
                        Toast.makeText(mCallback, response.body()!!.message, Toast.LENGTH_SHORT).show()


                    }else{
                        Toast.makeText(mCallback, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<DataCollection.SendCommentsResponse>, t: Throwable) {
                t.message?.let { Log.e("onFailure", it) }
            }
        })

    }

}