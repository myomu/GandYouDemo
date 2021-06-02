package com.gatheringandyou.gandyoudemo.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gatheringandyou.gandyoudemo.R
import com.gatheringandyou.gandyoudemo.bulletinboards.ExtensionActivity
import com.gatheringandyou.gandyoudemo.bulletinboards.FreeBoard
import com.gatheringandyou.gandyoudemo.bulletinboards.FreeBoardData
import com.gatheringandyou.gandyoudemo.databinding.LayoutRecyclerItemBinding
import com.gatheringandyou.gandyoudemo.shared.PreferenceManger
import java.text.SimpleDateFormat
import java.util.*

class BoardAdapter:RecyclerView.Adapter<Holder>() {

    var listData = mutableListOf<FreeBoardData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = LayoutRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val free = listData[position]

        holder.setFreeboard(free)
        holder.clickItem(free)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

}


class Holder(val binding: LayoutRecyclerItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun clickItem(data: FreeBoardData) {

        binding.root.setOnClickListener {

            val intent = Intent(binding.root.context, ExtensionActivity::class.java)

            // Preference로도 저장 시키자. 이걸 업데이트 시켜저 댓글 추가 삭제에도 사용해보도록..시도 댓글 삭제시 activity를 새로고침하게 되는데 필요한 자료들..
            // 이것이 필요한 이유는 Freeboard -> ExtensionActivity 이동시 intent로 id, title, content, userEmail이 넘어가는데
            // 이를 ExtensionActivity 에서 데이터 통신을 위한 변수로 사용하기 때문이다.
            PreferenceManger(binding.root.context).setInt("BoardId", data.id_freeboard)
            PreferenceManger(binding.root.context).setString("BoardTitle", data.freeboard_title)
            PreferenceManger(binding.root.context).setString("BoardContent", data.freeboard_content)
            PreferenceManger(binding.root.context).setString("BoardUserEmail", data.useremail)

            /*
            PreferenceManger(binding.root.context).setString("BoardDate", binding.boardRecyclerItemTime.text.toString())
            PreferenceManger(binding.root.context).setInt("BoardLikeCount", data.freeboard_like_count)
            PreferenceManger(binding.root.context).setInt("BoardCommentsCount", data.freeboard_comments_count)
            PreferenceManger(binding.root.context).setString("BoardUserName", data.username)
            */

            Log.d("게시판 id 제대로 들어옴?", data.id_freeboard.toString())

            // ExtensionActivity 에서 필요로 하는 데이터. 꼭 있어야함. Preference 로 저장시켜 사용하기에 이제 필요 없을듯? 이중 데이터 전달..
            //intent.putExtra("id", data.id_freeboard)
            //intent.putExtra("title", data.freeboard_title)
            //intent.putExtra("content", data.freeboard_content)
            //intent.putExtra("userEmail", data.useremail)
            //시간만 약간 차이나게

            // 이것도 이제 필요 없을듯.
            /*intent.putExtra("date", binding.boardRecyclerItemTime.text)

            intent.putExtra("likeCount", data.freeboard_like_count)
            intent.putExtra("commentsCount", data.freeboard_comments_count)
            intent.putExtra("userId", data.userid)
            intent.putExtra("userName", data.username)*/

            it.context.startActivity(intent)
            (binding.root.context as FreeBoard).overridePendingTransition(R.anim.horizon_enter, R.anim.none)

            //Log.d("클릭체크용", "체크")

            //Toast.makeText(binding.root.context, "클릭된 아이템 = ${binding.boardRecyclerItemTitle.text}",
            //Toast.LENGTH_SHORT).show()
        }

    }

    fun setFreeboard(data: FreeBoardData) {

        binding.boardRecyclerItemTitle.text = data.freeboard_title
        binding.boardRecyclerItemSubstance.text = data.freeboard_content
        binding.boardRecyclerItemUsername.text = data.username

        val dataTime = data.freeboard_date //mysql에서 받아오는 시간과 날짜 데이터

        // 시간이 데이터베이스도 서울 표준시로 되어 있지만 안드로이드 기기에서도 서울 시간으로 하면 시간이 +9시가 되어 calendar 타입 변수에 Date타입
        // 을 넣고 -9시간을 설정한 뒤 해당 값들을 calendar.time 으로 넣어주었다.
        val calendar = Calendar.getInstance()
        calendar.time = dataTime
        calendar.add(Calendar.HOUR, -9)

        val dataYearFormat = SimpleDateFormat("yyyy", Locale.KOREA).format(calendar.time)
        val dataDateFormat = SimpleDateFormat("dd", Locale.KOREA).format(calendar.time)

        val cal = Calendar.getInstance() // 현재 시간
        val years = cal.get(Calendar.YEAR)
        val date = cal.get(Calendar.DATE)

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
        } else {
            Log.d("현재시간년도와 데이터의시간년도가 다를경우", "출력")
            val formatterYear = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            val formattedYear = formatterYear.format(calendar.time)
            binding.boardRecyclerItemTime.text = formattedYear
        }
    }

}