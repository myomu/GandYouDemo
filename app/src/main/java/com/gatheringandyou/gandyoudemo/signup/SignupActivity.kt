package com.gatheringandyou.gandyoudemo.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.gatheringandyou.gandyoudemo.databinding.ActivitySignupactivityBinding
import com.gatheringandyou.gandyoudemo.login.loginActivity
import com.gatheringandyou.gandyoudemo.login.repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupactivityBinding
    lateinit var nickName : String // 나중에 닉네임 변수를 사용할 때 초기화 하겠다. lateinit
    private var checkNick = false
    lateinit var email : String
    lateinit var signupapi:SignupInterface
    lateinit var checknickapi:checknicknameinterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupactivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        editnickchangeListener()
        binding.btnRegister.setOnClickListener { doSignUp() }
        binding.checknick.setOnClickListener { nicknamecheck() }
    }

    private fun editnickchangeListener() {
        binding.etNick.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               checkNick = false
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun nicknamecheck()
    {
        nickName = binding.etNick.text.toString().trim()
        if (nickName.isEmpty())
        {
            Toast.makeText(this, "닉네임을 입력해 주세요", Toast.LENGTH_SHORT).show() //this는 sighup액티비티를 기준으로 뜬다고 생각.
            return
        }
        val chk = checknickname(nickName)
        val retrofit = repository.getApiClient()
        if (retrofit != null)
        {
            checknickapi=retrofit.create(checknicknameinterface::class.java)

        }
        val call : Call<SignupResponse> = checknickapi.checknick(chk)
        call.enqueue(object:Callback<SignupResponse>{
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>)
            {
                if (response.isSuccessful && response.body() != null)
                {
                    if (response.body()!!.code == 1) {
                        checkNick = true
                        Log.e("onSuccess", response.body()!!.message)
                        Toast.makeText(
                                this@SignupActivity,
                                response.body()!!.message,
                                Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        checkNick = false
                        Log.e("onFailed", response.body()!!.message)
                        Toast.makeText(
                                this@SignupActivity,
                                response.body()!!.message,
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable)
            {
                t.message?.let { Log.e("onFailure", it) }
            }
        })
    }


    private fun doSignUp() {
        if (!checkNick) {
            Toast.makeText(this, "닉네임 중복확인을 해주세요", Toast.LENGTH_SHORT).show() //중복확인
            return
        }

        nickName = binding.etNick.text.toString().trim() //et닉이란 사람들이 글자를 쓰면 .text가 가져오는것 tostring으로 변환 trim 공백 제거
        email = binding.etEmail.text.toString().trim()
        val password = binding.etPass.text.toString().trim()
        if (nickName.isEmpty() || email.isEmpty() || password.isEmpty()) // 닉네임 이메일 패스워드중 하나라도 공백이면 에러방지로 메시지 띄우기
        {
            Toast.makeText(this, "빈 칸을 입력해 주세요", Toast.LENGTH_SHORT).show()
            return // 리턴x시 의미 없음
        }
        if (!binding.radio1.isChecked && !binding.radio2.isChecked)
        {
            Toast.makeText(this, "성별을 선택해 주세요", Toast.LENGTH_SHORT).show()
            return
        }

        val gender = if(binding.radio1.isChecked)
        {
           binding.radio1.text.toString()
        }
            else
            {
                binding.radio2.text.toString()
            }

        val signUpdata = Signup(email, password, nickName, gender)
        Log.e("Error",password)
        val retrofit = repository.getApiClient()
        if (retrofit != null) {
            signupapi = retrofit.create(SignupInterface::class.java)
        }
        val call: Call<SignupResponse> = signupapi.getSignUp(signUpdata)
        call.enqueue(object : Callback<SignupResponse>{
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>)
            {
                if (response.isSuccessful && response.body() != null)
                {
                    Log.e("onSuccess", response.body()!!.message)
                    Toast.makeText(
                            this@SignupActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                    ).show()
                }
                goLogin()
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable)
            {  t.message?.let { Log.e("onFailure", it) }
                Toast.makeText(this@SignupActivity, "회원가입 에러 발생", Toast.LENGTH_SHORT).show()

            }
        })
    }

    fun goLogin() {
        var intent = Intent(this, loginActivity::class.java)
        startActivity(intent)
        finish()
    }
}