package com.gatheringandyou.gandyoudemo.bulletinboards

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.gatheringandyou.gandyoudemo.R
import com.gatheringandyou.gandyoudemo.bulletinboards.DataCommunication.postCommentsData
import com.gatheringandyou.gandyoudemo.databinding.ActivityExtensionBinding
import com.gatheringandyou.gandyoudemo.shared.PreferenceManger
import kotlin.properties.Delegates


class ExtensionActivity : AppCompatActivity() {

    private val binding by lazy { ActivityExtensionBinding.inflate(layoutInflater) }

    private lateinit var commentsAdapter: CommentsAdapter // 댓글 RecyclerView Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //boardId = intent.getIntExtra("id", 0)

        //기본 액션바 처리 부분
        setSupportActionBar(binding.tbExtension)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbExtension.title = " "

        // 선택되어져 들어온 게시글 데이터를 intent 로 넘겨받아 setting 시켜준다.
        binding.tvExtensionUsername.text = intent.getStringExtra("userName")
        binding.tvExtensionDate.text = intent.getStringExtra("date")
        binding.tvExtensionTitle.text = intent.getStringExtra("title")
        binding.tvExtensionContent.text = intent.getStringExtra("content")
        binding.tvExtensionLikeCount.text = "${intent.getIntExtra("likeCount", 0)}"



        // 게시글의 id 값을 넘겨준다. 이 게시판 데이터의 primary_key
        val freeBoardId = intent.getIntExtra("id", 0)
        Log.d("게시글 id 테스트", freeBoardId.toString())

        // 리사이클러뷰 관련 코드
        commentsAdapter = CommentsAdapter(this)
        binding.rvComments.adapter = commentsAdapter
        binding.rvComments.layoutManager = LinearLayoutManager(this)

        // 데이터 불러오는 함수
        DataCommunication.loadCommentsData(this, freeBoardId)



        binding.tvExtensionCommentsCount.text = commentsAdapter.itemCount.toString()//"${intent.getIntExtra("commentsCount", 0)}"

        Log.d("리스트 사이즈 확인", commentsAdapter.itemCount.toString())

        //binding.btnExtensionLike

        // 채팅창이 공백일 경우 버튼 비활성화
        binding.etBoardChatting.addTextChangedListener { text ->
            binding.btnBoardChatSend.isEnabled = text.toString() != ""
        }

        binding.btnBoardChatSend.setOnClickListener {
            postCommentsData(this, binding.etBoardChatting.text.toString(), freeBoardId)
            binding.etBoardChatting.text.clear()

            val boardId = PreferenceManger(this).getInt("BoardId")
            val boardTitle = PreferenceManger(this).getString("BoardTitle")
            val boardContent = PreferenceManger(this).getString("BoardContent")
            val boardDate = PreferenceManger(this).getString("BoardDate")
            val boardLikeCount = PreferenceManger(this).getInt("BoardLikeCount")
            val boardCommentsCount = PreferenceManger(this).getInt("BoardCommentsCount")
            val boardUserName = PreferenceManger(this).getString("BoardUserName")
            finish()

            val intent = Intent(this, ExtensionActivity::class.java)

            intent.putExtra("id", boardId)
            intent.putExtra("title", boardTitle)
            intent.putExtra("content", boardContent)
            //시간만 약간 차이나게
            intent.putExtra("date", boardDate)

            intent.putExtra("likeCount", boardLikeCount)
            intent.putExtra("commentsCount", boardCommentsCount)
            intent.putExtra("userName", boardUserName)


            startActivity(intent)
            overridePendingTransition(R.anim.none, R.anim.none)

            // 리스트를 한번 클리어시키고 갱신. 그다음 다시 데이터를 서버에 요청. 한번식 제대로 새로고침 안되는데 그 이유를 모르겠음..
            //commentsAdapter.commentsListData.clear()
            //commentsAdapter.notifyDataSetChanged()

            //DataCommunication.loadCommentsData(this, freeBoardId)

        }

        // 뷰 애니메이션 처리
        //overridePendingTransition(R.anim.horizon_enter, R.anim.none)
    }

//    override fun onRestart() {
//        super.onRestart()
//        val boardId = PreferenceManger(this).getInt("BoardId")
//        DataCommunication.loadCommentsData(this, boardId)
//    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.extension_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            android.R.id.home -> {
                someFunction()
                return true
            }
            R.id.item_extension_menu -> {

                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }

    }


    private fun someFunction() {
        finish()
        overridePendingTransition(R.anim.none, R.anim.horizon_exit)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            // back 버튼으로 화면 종료가 야기되면 동작한다.
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }
    }

    // 리사이클러뷰 어댑터에 데이터 setting 시켜주는 함수.
    fun loadCommentsComplete(data: MutableList<DataCollection.GetCommentsData>) {
        commentsAdapter.commentsListData = data
        commentsAdapter.notifyDataSetChanged()
        //Log.d("데이타확인", data.toString())
    }


}