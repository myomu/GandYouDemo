package com.gatheringandyou.gandyoudemo.adapters

import android.content.pm.FeatureGroupInfo
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.gatheringandyou.gandyoudemo.Memo
import com.gatheringandyou.gandyoudemo.bulletinboards.FreeBoardData
import com.gatheringandyou.gandyoudemo.databinding.LayoutRecyclerItemBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class BoardAdapter:RecyclerView.Adapter<Holder>() {

    var listData = mutableListOf<FreeBoardData>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = LayoutRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val memo = listData.get(position)
        val free = listData.get(position)

        holder.setFreeboard(free)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setList(notice: MutableList<FreeBoardData>){
        listData = notice
        Log.d("체크합니다listData", listData.toString())
    }

}


class Holder(val binding: LayoutRecyclerItemBinding): RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            Toast.makeText(binding.root.context, "클릭된 아이템 = ${binding.boardRecyclerItemTitle.text}",
            Toast.LENGTH_SHORT).show()
        }
    }

    fun setMemo(memo: Memo) {
        //binding.boardRecyclerItemTitle.text = "${memo.no}"
        binding.boardRecyclerItemTitle.text = memo.title
        binding.boardRecyclerItemSubstance.text = memo.substance
        binding.boardRecyclerItemUsername.text = memo.username
        var sdf = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.KOREA)
        var formattedDate = sdf.format(memo.time)
        binding.boardRecyclerItemTime.text = formattedDate
    }


    fun setFreeboard(data: FreeBoardData) {
        //binding.boardRecyclerItemTitle.text = "${memo.no}"
        binding.boardRecyclerItemTitle.text = data.freeboard_title
        binding.boardRecyclerItemSubstance.text = data.freeboard_content
        binding.boardRecyclerItemUsername.text = data.username
        //var sdf = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.KOREA)
        //var formattedDate = sdf.format(memo.time)

        //var todayTimes = System.currentTimeMillis()

        var dataTime = data.freeboard_date // 데이터 시간

        var dataYearFormat = SimpleDateFormat("yyyy").format(dataTime)
        var dataMonthFormat = SimpleDateFormat("MM").format(dataTime)
        var dataDateFormat = SimpleDateFormat("dd").format(dataTime)

        var cal = Calendar.getInstance() // 현재 시간
        //cal.time = check
        var years = cal.get(Calendar.YEAR)
        var date = cal.get(Calendar.DATE)
        val hours = cal.get(Calendar.HOUR)
        val second = cal.get(Calendar.SECOND)

        //dataTime.time.times(11)

        if (years == dataYearFormat.toInt()) {

            if (date == dataDateFormat.toInt()) {

                var formatter = SimpleDateFormat("HH:mm", java.util.Locale.KOREA)
                val formattedDate = formatter.format(data.freeboard_date)
                binding.boardRecyclerItemTime.text = formattedDate

            } else {

                var formatter = SimpleDateFormat("MM/dd", java.util.Locale.KOREA)
                val formattedDate = formatter.format(data.freeboard_date)
                binding.boardRecyclerItemTime.text = formattedDate

            }
            Log.d("현재시간 년도", years.toString())
            Log.d("현재시간 날짜", date.toString())
            Log.d("현재시간", second.toString())
            Log.d("데이터 년도", dataYearFormat+" "+ dataMonthFormat +" "+ dataDateFormat)

        } else {
            Log.d("현재시간년도와 데이터의시간년도가 다를경우", "출력")
            var formatter = SimpleDateFormat("yyyy/MM/dd", java.util.Locale.KOREA)
            val formattedDate = formatter.format(data.freeboard_date)
            binding.boardRecyclerItemTime.text = formattedDate
        }

//        var formatter = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초", java.util.Locale.KOREA)
//        val formattedDate = formatter.format(data.freeboard_date)
//        binding.boardRecyclerItemTime.text = formattedDate
    }


}