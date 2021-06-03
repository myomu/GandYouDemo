package com.gatheringandyou.gandyoudemo.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gatheringandyou.gandyoudemo.MainActivity
import com.gatheringandyou.gandyoudemo.databinding.ActivityLoginBinding
import com.gatheringandyou.gandyoudemo.shared.PreferenceManger
import com.gatheringandyou.gandyoudemo.signup.SignupActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class loginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var loginapi:Logininterface
    val context : Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        binding.btnLogin.setOnClickListener { dologin() }
        binding.btnRegister.setOnClickListener { goResister() }

        setContentView(binding.root)

        val checkLoginEmail = PreferenceManger(context).getString("userEmail")

        if (checkLoginEmail != "Default") {

            goToMain()

        } else { return }

    }

    private fun dologin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPass.text.toString().trim()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "빈 칸을 입력해 주세요", Toast.LENGTH_SHORT).show()
            return
        }
        val logindata = Login(email,password)
        val retrofit = repository.getApiClient()
        if (retrofit != null) {
            loginapi = retrofit.create(Logininterface::class.java)
        }
        val call: Call<loginResponse> = loginapi.getLogin(logindata)
        call.enqueue(object: Callback<loginResponse> {
            override fun onResponse(call: Call<loginResponse>, response: Response<loginResponse>) {
                if (response.isSuccessful && response.body() != null)
                {
                    if(response.body()!!.code == 200){

                        //SharedPreferences 사용 사용자 email 을 저장
                        PreferenceManger(context).setString("userEmail", email)

                        Toast.makeText(this@loginActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        val checkEmail = PreferenceManger(context).getString("userEmail")
                        Log.d("이메일저장 확인", checkEmail.toString())
                        goToMain()

                    }else{
                        Toast.makeText(this@loginActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    }
                }
                // goToMain() // 틀려도 넘어가서 위쪽으로 수정
            }

            override fun onFailure(call: Call<loginResponse>, t: Throwable) {
                t.message?.let { Log.e("onFailure", it) }

            }
        })
        }

    private fun goResister() {

        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)

    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

