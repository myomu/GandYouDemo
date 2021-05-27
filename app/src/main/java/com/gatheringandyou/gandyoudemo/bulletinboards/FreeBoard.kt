package com.gatheringandyou.gandyoudemo.bulletinboards

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    //lateinit var freeboardapi: FreeBoardInterface
    private lateinit var boardAdapter: BoardAdapter
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_board)

        setContentView(binding.root)

        context = this

        //기본 액션바 숨김처리
        //var actionBar : ActionBar? = supportActionBar
        //actionBar?.hide()

        DataCommunication.loadFreeboardData(this)

        boardAdapter = BoardAdapter()

        binding.freeBoardRecyclerView.adapter = boardAdapter
        binding.freeBoardRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.btnPostCreate.setOnClickListener {

            val intent = Intent(this, PostCreateActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onRestart() {
        super.onRestart()

        DataCommunication.loadFreeboardData(this)

    }



//    override fun onBackPressed() {
//        val postCreateFragment = PostCreateFragment()
//
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.setCustomAnimations(R.anim.out_bottom, R.anim.enter_from_right)
//            .remove(postCreateFragment)
//            .commit()
//    }



    fun loadComplete(data: MutableList<FreeBoardData>) {
        //boardAdapter.setList(data)
        boardAdapter.listData = data
        boardAdapter.notifyDataSetChanged()
        Log.d("데이타확인", data.toString())
    }
}