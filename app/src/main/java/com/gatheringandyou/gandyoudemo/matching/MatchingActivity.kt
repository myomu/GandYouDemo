package com.gatheringandyou.gandyoudemo.matching

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gatheringandyou.gandyoudemo.databinding.ActivityMatchingBinding

class MatchingActivity:AppCompatActivity() {
    lateinit var binding: ActivityMatchingBinding
    private var userList = mutableListOf<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initData()
    }

    private fun initData() {
       val u1 = User("abc","abc","abc","abc","abc")
       val u2 = User("def","def","def","def","def")

        userList.add(u1)
        userList.add(u2)

        val adapter = UserAdapter(userList,this)
        binding.viewPager.adapter = adapter

    }
}