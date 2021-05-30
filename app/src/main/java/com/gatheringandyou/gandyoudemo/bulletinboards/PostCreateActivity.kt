package com.gatheringandyou.gandyoudemo.bulletinboards

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.gatheringandyou.gandyoudemo.R
import com.gatheringandyou.gandyoudemo.databinding.ActivityPostCreateBinding
import com.gatheringandyou.gandyoudemo.login.repository
import com.gatheringandyou.gandyoudemo.shared.PreferenceManger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class PostCreateActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPostCreateBinding.inflate(layoutInflater) }

    lateinit var freeboardapi:FreeBoardInterface

    //private lateinit var test1 : FreeBoard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        overridePendingTransition(R.anim.vertical_enter, R.anim.none)


        //boardAdapter = BoardAdapter()

        //freeBoardBinding.freeBoardView.adapter

        //기본 액션바 처리 부분
        setSupportActionBar(binding.toolbarPostCreate)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarPostCreate.title = "글 쓰기"


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_post_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            android.R.id.home -> {
                someFunction()
                return true
            }
            R.id.action_add -> {

                sendPostData()

                var intent = Intent()
                intent.putExtra("result", 1)
                setResult(RESULT_OK, intent)

                someFunction()



                //val dd : FreeBoard = context
                //dd.refresh()

                //DataCommunication.loadFreeboardData(context)
                //boardAdapter.notifyDataSetChanged()

                //val cc = FreeBoard

                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }

    }

    private fun someFunction() {
        finish()
        overridePendingTransition(R.anim.none, R.anim.vertical_exit)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            // back 버튼으로 화면 종료가 야기되면 동작한다.
            overridePendingTransition(R.anim.none, R.anim.vertical_exit)
        }
    }

    private fun sendPostData() {
        val title = binding.etTitle.text.toString()
        val content = binding.etContent.text.toString()
        val userid = PreferenceManger(this).getInt("userId")
        val username = PreferenceManger(this).getString("userNickname").toString()
        val nowTime = Calendar.getInstance().time // 현재 시간

        val changeTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(nowTime)

        Log.d("보내기 전 현재 시간", nowTime.toString())
        Log.d("변환된 현재 시간", changeTime.toString())

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "제목과 내용을 입력해 주세요", Toast.LENGTH_SHORT).show()
            return
        }
        val postData = PostData(title, content, userid, username, changeTime)
        val retrofit = repository.getApiClient()
        if (retrofit != null) {
            freeboardapi = retrofit.create(FreeBoardInterface::class.java)
        }
        val call: Call<PostResponse> = freeboardapi.sendPostData(postData)
        call.enqueue(object: Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.isSuccessful && response.body() != null)
                {
                    if(response.body()!!.code == 200){
                        Toast.makeText(this@PostCreateActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        //val cc = FreeBoard()
                        //cc.onResume
                        //FreeBoard().onResume()


                    }else{
                        Toast.makeText(this@PostCreateActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    }
                }

            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                t.message?.let { Log.e("onFailure", it) }
            }

        })
    }

}