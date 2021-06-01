package com.gatheringandyou.gandyoudemo.bulletinboards

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.gatheringandyou.gandyoudemo.R
import com.gatheringandyou.gandyoudemo.adapters.BoardAdapter
import com.gatheringandyou.gandyoudemo.databinding.ActivityFreeBoardBinding
import com.gatheringandyou.gandyoudemo.login.repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class FreeBoard : AppCompatActivity() {

    private val binding by lazy { ActivityFreeBoardBinding.inflate(layoutInflater) }
    private lateinit var boardAdapter: BoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //기본 액션바 처리
        setSupportActionBar(binding.tbFreeBoard)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbFreeBoard.title = "자유게시판"

        // 데이터 불러오는 함수
        DataCommunication.loadFreeboardData(this)

        // 리사이클러뷰 관련 코드
        boardAdapter = BoardAdapter()
        binding.freeBoardRecyclerView.adapter = boardAdapter
        binding.freeBoardRecyclerView.layoutManager = LinearLayoutManager(this)

        // 게시글 생성 버튼. 우하단 Floating Button.
        binding.btnPostCreate.setOnClickListener {

            val intent = Intent(this, PostCreateActivity::class.java)
            startActivity(intent)

        }
    }

    // 게시글 작성되면 데이터를 다시 받아와 새로고침 시켜준다.
    override fun onRestart() {
        super.onRestart()

        DataCommunication.loadFreeboardData(this)

    }

    // 상단 액션바의 아이템 선택 액션.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            android.R.id.home -> {
                someFunction()
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }

    }

    // 종료 애니메이션.
    private fun someFunction() {
        finish()
        overridePendingTransition(R.anim.none, R.anim.horizon_exit)
    }



//    override fun onBackPressed() {
//        val postCreateFragment = PostCreateFragment()
//
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.setCustomAnimations(R.anim.out_bottom, R.anim.enter_from_right)
//            .remove(postCreateFragment)
//            .commit()
//    }


    // 리사이클러뷰 어댑터에 데이터 setting 시켜주는 함수.
    fun loadComplete(data: MutableList<FreeBoardData>) {
        //boardAdapter.setList(data)
        boardAdapter.listData = data
        boardAdapter.notifyDataSetChanged()
        Log.d("데이타확인", data.toString())
    }
}