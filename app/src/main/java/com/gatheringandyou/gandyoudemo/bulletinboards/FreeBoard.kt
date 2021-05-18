package com.gatheringandyou.gandyoudemo.bulletinboards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gatheringandyou.gandyoudemo.Memo
import com.gatheringandyou.gandyoudemo.R
import com.gatheringandyou.gandyoudemo.adapters.BoardAdapter
import com.gatheringandyou.gandyoudemo.databinding.ActivityFreeBoardBinding

//테스트


class FreeBoard : AppCompatActivity() {

    private val binding by lazy { ActivityFreeBoardBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_board)

        setContentView(binding.root)

        val data:MutableList<Memo> = loadData()

        var adapter = BoardAdapter()
        adapter.listData = data
        binding.freeBoardRecyclerView.adapter = adapter

        binding.freeBoardRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun loadData(): MutableList<Memo> {
        val data: MutableList<Memo> = mutableListOf()

        for (no in 1..100){
            val title = "이것이 안드로이드다~~!! ${no}"
            val substance = "내용 ${no}"
            val date = System.currentTimeMillis()
            val username = "유저이름 ${no}"

            var memo = Memo(title, substance, date, username, no)
            data.add(memo)
        }
        return data
    }
}