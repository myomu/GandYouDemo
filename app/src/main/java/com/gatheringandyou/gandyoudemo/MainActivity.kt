package com.gatheringandyou.gandyoudemo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.gatheringandyou.gandyoudemo.databinding.ActivityMainBinding
import com.gatheringandyou.gandyoudemo.login.repository
import com.gatheringandyou.gandyoudemo.shared.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/** 로그인 후 보여지는 메인 화면 하단 네비게이션바로 4개의 프래그먼트들로 구성되어져 있음. **/
class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding

    lateinit var userDataApi: UserDataInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(mBinding.root)

        // 네비게이션들을 담는 호스트
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment
        // 네비게이션 컨트롤러
        val navController = navHostFragment.navController
        // 바텀 네비게이션 뷰 와 네비게이션 묶어준다
        NavigationUI.setupWithNavController(mBinding.myBottomNav, navController)

        loadUserData(this)

        val checkEmail = PreferenceManger(this).getString("userEmail")
        Log.d("이메일저장 확인 메인에서", checkEmail.toString())

    }

    // 먼저 로그인 후 MainActivity 화면에 들어오면 사용자의 정보를 PreferenceManger 를 이용해 저장시킨다.
    private fun loadUserData(context: Context){

        val useremail = PreferenceManger(context).getString("userEmail")
        Log.d("유저 이메일 테스트", useremail.toString())
        val emailData = PostEmail(useremail)

        var listData: MutableList<UserData>

        val retrofit = repository.getApiClient()
        if (retrofit != null) {
            userDataApi = retrofit.create(UserDataInterface::class.java)
        }

        val call: Call<UserDataResponse> = userDataApi.postUserEmail(emailData)
        call.enqueue(object: Callback<UserDataResponse> {

            override fun onResponse(call: Call<UserDataResponse>, response: Response<UserDataResponse>) {
                if (response.isSuccessful && response.body() != null)
                {

                    if(response.body()!!.code == 200){
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        Log.d("성공 메세지", response.body()!!.message)

                        //Preference 에 사용자 데이터 저장
                        listData = response.body()!!.data

                        PreferenceManger(context).setInt("userId", listData[0].user_id)
                        PreferenceManger(context).setString("userNickname", listData[0].nickname)
                        PreferenceManger(context).setString("userGender", listData[0].gender)
                        PreferenceManger(context).setString("userDepartment", listData[0].department)
                        PreferenceManger(context).setInt("userAge", listData[0].age)
                        PreferenceManger(context).setString("userHobby1", listData[0].hobby1)
                        PreferenceManger(context).setString("userHobby2", listData[0].hobby2)
                        PreferenceManger(context).setString("userHobby3", listData[0].hobby3)

                    }else{
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<UserDataResponse>, t: Throwable) {
                t.message?.let { Log.e("onFailure", it) }
            }
        })
    }

}