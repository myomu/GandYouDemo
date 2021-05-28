package com.gatheringandyou.gandyoudemo.matching

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gatheringandyou.gandyoudemo.databinding.ActivityMatchingBinding
import com.gatheringandyou.gandyoudemo.login.repository
import com.gatheringandyou.gandyoudemo.shared.PreferenceManger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchingActivity:AppCompatActivity() {
    lateinit var binding: ActivityMatchingBinding
    private var userList = mutableListOf<User>()
    private var mynickname : String? = null

    private var depart : String? = null
    private var hobby1 : String? = null
    private var hobby2 : String? = null
    private var hobby3 : String? = null
    lateinit var matchapi : MatchInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getMyData()
        getMatch()
        initData()
    }

    private fun getMyData() {

        mynickname = PreferenceManger(applicationContext).getString("userNickname")
        depart = PreferenceManger(applicationContext).getString("userDepartment")
        hobby1 = PreferenceManger(applicationContext).getString("userHobby1")
        hobby2 = PreferenceManger(applicationContext).getString("userHobby2")
        hobby3 = PreferenceManger(applicationContext).getString("userHobby3")
    }

    private fun getMatch() {
        val retrofit = repository.getApiClient()
    if(retrofit!= null)
    {
        matchapi = retrofit.create(MatchInterface::class.java)
    }
        val call: Call<UserResponse> = matchapi.getMatchingUser(hobby1,hobby2,hobby3)


        call.enqueue(object:Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if(response.isSuccessful&&response.body()!=null) {
                val list = response.body()!!.list
                    for (i in list.indices){
                        if(list[i].nickname != mynickname && list[i].depart != depart){
                            val otherdata = User(list[i].nickname,list[i].depart,list[i].age,list[i].hobby1,list[i].hobby2,list[i].hobby3)
                            userList.add(otherdata)
                        }
                    }
                }

                }


            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t.message?.let { Log.e("onFailure", it) }
            }
        })
    }



    private fun initData() {


        val adapter = UserAdapter(userList,this)
        binding.viewPager.adapter = adapter

    }
}