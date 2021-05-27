package com.gatheringandyou.gandyoudemo.adapters


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gatheringandyou.gandyoudemo.Memo
import com.gatheringandyou.gandyoudemo.bulletinboards.ExtensionActivity
import com.gatheringandyou.gandyoudemo.bulletinboards.FreeBoardData
import com.gatheringandyou.gandyoudemo.databinding.LayoutRecyclerItemBinding
import java.text.SimpleDateFormat
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
        holder.clickItem(free)
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

//    init {
//        binding.root.setOnClickListener {
//
//
//
//
//            //fragment로 전환 시도 했으나..실패
//            //val activity = it!!.context as AppCompatActivity
//            //val extensionFragment = ExtensionFragment()
//            //activity.supportFragmentManager.beginTransaction().replace(R.id.freeBoard_view, extensionFragment).addToBackStack(null).commit()
//
//
//            val intent: Intent = Intent(binding.root.context, MainActivity::class.java)
//            intent.putExtra("title", binding.boardRecyclerItemTitle.text)
//            intent.putExtra("title", binding.boardRecyclerItemTitle.text)
//            it.context.startActivity(intent)
//            Log.d("클릭체크용", "체크")
//
//
//
//            Toast.makeText(binding.root.context, "클릭된 아이템 = ${binding.boardRecyclerItemTitle.text}",
//            Toast.LENGTH_SHORT).show()
//        }
//    }

    fun clickItem(data: FreeBoardData) {

        binding.root.setOnClickListener {

            val intent: Intent = Intent(binding.root.context, ExtensionActivity::class.java)
            intent.putExtra("id", data.id_freeboard)
            intent.putExtra("title", data.freeboard_title)
            intent.putExtra("content", data.freeboard_content)
            //시간만 약간 차이나게
            intent.putExtra("date", binding.boardRecyclerItemTime.text)

            intent.putExtra("likeCount", data.freeboard_like_count)
            intent.putExtra("commentsCount", data.freeboard_comments_count)
            intent.putExtra("userId", data.userid)
            intent.putExtra("userName", data.username)

            it.context.startActivity(intent)
            Log.d("클릭체크용", "체크")

            Toast.makeText(binding.root.context, "클릭된 아이템 = ${binding.boardRecyclerItemTitle.text}",
            Toast.LENGTH_SHORT).show()

        }

    }

    fun setMemo(memo: Memo) {
        //binding.boardRecyclerItemTitle.text = "${memo.no}"
        binding.boardRecyclerItemTitle.text = memo.title
        binding.boardRecyclerItemSubstance.text = memo.substance
        binding.boardRecyclerItemUsername.text = memo.username
        var sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        var formattedDate = sdf.format(memo.time)
        binding.boardRecyclerItemTime.text = formattedDate
    }


    fun setFreeboard(data: FreeBoardData) {

        binding.boardRecyclerItemTitle.text = data.freeboard_title
        binding.boardRecyclerItemSubstance.text = data.freeboard_content
        binding.boardRecyclerItemUsername.text = data.username

        var dataTime = data.freeboard_date //mysql에서 받아오는 시간과 날짜 데이터

        // 시간이 데이터베이스도 서울 표준시로 되어 있지만 안드로이드 기기에서도 서울 시간으로 하면 시간이 +9시가 되어 calendar 타입 변수에 Date타입
        // 을 넣고 -9시간을 설정한 뒤 해당 값들을 calendar.time 으로 넣어주었다.
        val calendar = Calendar.getInstance()
        calendar.time = dataTime

        calendar.add(Calendar.HOUR, -9)

        val dataYearFormat = SimpleDateFormat("yyyy", Locale.KOREA).format(calendar.time)
        val dataMonthFormat = SimpleDateFormat("MM", Locale.KOREA).format(calendar.time)
        val dataDateFormat = SimpleDateFormat("dd", Locale.KOREA).format(calendar.time)

        val cal = Calendar.getInstance() // 현재 시간

        val years = cal.get(Calendar.YEAR)
        val date = cal.get(Calendar.DATE)
        val hours = cal.get(Calendar.HOUR)
        val minute = cal.get(Calendar.MINUTE)


        if (years == dataYearFormat.toInt()) {

            if (date == dataDateFormat.toInt()) {

                val formatterHour = SimpleDateFormat("HH:mm", Locale.getDefault())
                val formattedHour = formatterHour.format(calendar.time)
                binding.boardRecyclerItemTime.text = formattedHour

            } else {

                val formatterDate = SimpleDateFormat("MM/dd", Locale.getDefault())
                val formattedDate = formatterDate.format(calendar.time)
                binding.boardRecyclerItemTime.text = formattedDate

            }
            Log.d("현재시간 년도", years.toString())
            Log.d("현재시간 날짜", date.toString())
            Log.d("현재시간", minute.toString())
            Log.d("데이터 년도", dataYearFormat+" "+ dataMonthFormat +" "+ dataDateFormat)

        } else {
            Log.d("현재시간년도와 데이터의시간년도가 다를경우", "출력")
            val formatterYear = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            val formattedYear = formatterYear.format(calendar.time)
            binding.boardRecyclerItemTime.text = formattedYear
        }
    }


}