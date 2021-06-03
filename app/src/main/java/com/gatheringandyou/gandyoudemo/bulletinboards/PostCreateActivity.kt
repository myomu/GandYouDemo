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
    lateinit var dataApi: InterfaceCollection

    var checkItem: Int = 0 // 게시글 생성과 수정 버튼 구분을 위한 변수.
    var freeBoardId: Int = 0
    var date: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        overridePendingTransition(R.anim.vertical_enter, R.anim.none)

        binding.etTitle.setText(intent.getStringExtra("title"))
        binding.etContent.setText(intent.getStringExtra("content"))


        checkItem = intent.getIntExtra("checkEdit", 0)
        freeBoardId = intent.getIntExtra("id", 0)
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

        val item1 = menu?.findItem(R.id.action_add)
        val item2 = menu?.findItem(R.id.action_edit)

        if (checkItem == 1) {
            item1?.isVisible = false
            item2?.isVisible = true
        } else {
            item1?.isVisible = true
            item2?.isVisible = false
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            android.R.id.home -> {
                someFunction()
                return true
            }
            R.id.action_add -> {

                sendPostData()

                val intent = Intent()
                intent.putExtra("result", 1)
                setResult(RESULT_OK, intent)

                return true
            }
            R.id.action_edit -> {

                editBulletin()

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
        val useremail = PreferenceManger(this).getString("userEmail").toString()
        val nowTime = Calendar.getInstance().time // 현재 시간
        val changeTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(nowTime)

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "제목과 내용을 입력해 주세요", Toast.LENGTH_SHORT).show()
            return
        }
        val postData = PostData(title, content, userid, username, useremail, changeTime)
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
                        someFunction()
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

    private fun editBulletin() {
        val title = binding.etTitle.text.toString()
        val content = binding.etContent.text.toString()
        val username = PreferenceManger(this).getString("userNickname").toString()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "제목과 내용을 입력해 주세요", Toast.LENGTH_SHORT).show()
            return
        }
        val postEditData = DataCollection.UpdateBulletin(freeBoardId, title, content, username)
        val retrofit = repository.getApiClient()
        if (retrofit != null) {
            dataApi = retrofit.create(InterfaceCollection::class.java)
        }
        val call: Call<DataCollection.UpdateBulletinResponse> = dataApi.updateBulletin(postEditData)
        call.enqueue(object: Callback<DataCollection.UpdateBulletinResponse> {
            override fun onResponse(call: Call<DataCollection.UpdateBulletinResponse>, response: Response<DataCollection.UpdateBulletinResponse>) {
                if (response.isSuccessful && response.body() != null)
                {
                    if(response.body()!!.code == 200){
                        Toast.makeText(this@PostCreateActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                        someFunction()

                    }else{
                        Toast.makeText(this@PostCreateActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<DataCollection.UpdateBulletinResponse>, t: Throwable) {
                t.message?.let { Log.e("onFailure", it) }
            }

        })
    }

}