package com.gatheringandyou.gandyoudemo.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gatheringandyou.gandyoudemo.MainActivity
import com.gatheringandyou.gandyoudemo.R
import com.gatheringandyou.gandyoudemo.bulletinboards.FreeBoard
import com.gatheringandyou.gandyoudemo.databinding.ActivityLoginBinding
import com.gatheringandyou.gandyoudemo.signup.SignupActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class loginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var loginapi:Logininterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        binding.btnLogin.setOnClickListener { dologin() }
        binding.btnRegister.setOnClickListener { goResister() }

        setContentView(binding.root)
    }

    private fun dologin() {
        var email = binding.etEmail.text.toString().trim()
        var password = binding.etPass.text.toString().trim()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "빈 칸을 입력해 주세요", Toast.LENGTH_SHORT).show()
            return
        }
        var logindata = Login(email,password)
        val retrofit = repository.getApiClient()
        if (retrofit != null) {
            loginapi = retrofit.create(Logininterface::class.java)
        }
        var call: Call<loginResponse> = loginapi.getLogin(logindata)
        call.enqueue(object: Callback<loginResponse> {
            override fun onResponse(call: Call<loginResponse>, response: Response<loginResponse>) {
                if (response.isSuccessful && response.body() != null)
                {
                    if(response.body()!!.code == 200){
                        Toast.makeText(this@loginActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
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

        var intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)

    }

    private fun goToMain() {
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

