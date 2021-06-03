package com.gatheringandyou.gandyoudemo.bulletinboards


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gatheringandyou.gandyoudemo.R
import com.gatheringandyou.gandyoudemo.databinding.ItemCommentsBinding
import com.gatheringandyou.gandyoudemo.login.repository
import com.gatheringandyou.gandyoudemo.shared.PreferenceManger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CommentsAdapter(context: Context): RecyclerView.Adapter<CommentsHolder>() {

    var commentsListData = mutableListOf<DataCollection.GetCommentsData>()

    var con = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsHolder {
        val binding = ItemCommentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CommentsHolder(binding, con)
    }

    override fun onBindViewHolder(holder: CommentsHolder, position: Int) {
        val comments = commentsListData[position]

        holder.setCommentsData(comments)
        holder.checkDelete(comments)

    }

    override fun getItemCount(): Int {
        return commentsListData.size
    }

}


class CommentsHolder(val binding: ItemCommentsBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {

    lateinit var dataApi: InterfaceCollection

    fun setCommentsData(data: DataCollection.GetCommentsData){

        binding.itemCommentsNickname.text = data.user_nickname
        binding.itemCommentsContent.text = data.comments_content

        // 아이템의 이메일과 현재 사용자의 이메일을 비교하여 작성된 댓글이 본인 것이라면 댓글 삭제 아이콘이 뜨도록 함.
        val currentUserEmail = PreferenceManger(context).getString("userEmail")
        if (currentUserEmail != data.user_email) {
            binding.btnCommentsDelete.visibility = View.GONE
        } else {
            binding.btnCommentsDelete.visibility = View.VISIBLE
        }

        val dataTime = data.comments_date //mysql에서 받아오는 시간과 날짜 데이터

        // 시간이 데이터베이스도 서울 표준시로 되어 있지만 안드로이드 기기에서도 서울 시간으로 하면 시간이 +9시가 되어 calendar 타입 변수에 Date타입
        // 을 넣고 -9시간을 설정한 뒤 해당 값들을 calendar.time 으로 넣어주었다.
        val calendar = Calendar.getInstance()
        calendar.time = dataTime
        calendar.add(Calendar.HOUR, -9)

        val dataYearFormat = SimpleDateFormat("yyyy", Locale.KOREA).format(calendar.time) // 년도만 빼옴

        val cal = Calendar.getInstance() // 현재 시간
        val years = cal.get(Calendar.YEAR) // 현재 시간 년도

        if (years == dataYearFormat.toInt()) {

            val formatterDate = SimpleDateFormat("MM/dd HH:mm", Locale.KOREA)
            val formattedDate = formatterDate.format(calendar.time)
            binding.itemCommentsTime.text = formattedDate

        } else {

            val formatterDate = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.KOREA)
            val formattedDate = formatterDate.format(calendar.time)
            binding.itemCommentsTime.text = formattedDate
        }

    }

    fun checkDelete(data: DataCollection.GetCommentsData){
        binding.btnCommentsDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(binding.root.context)
            dialog.setTitle("댓글을 삭제하시겠습니까?")
            dialog.setIcon(R.drawable.ic_round_delete)

            val dialogListener = object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            deleteComments(data)
                        }
                    }
                }
            }

            dialog.setPositiveButton("네", dialogListener)
            dialog.setNegativeButton("아니오", null)
            dialog.show()

        }
    }

    fun deleteComments(data: DataCollection.GetCommentsData) {

            val id = DataCollection.DeleteCommentsData(data.id_comments)
            val retrofit = repository.getApiClient()
            if (retrofit != null) {
                dataApi = retrofit.create(InterfaceCollection::class.java)
            }

            val call: Call<DataCollection.DeleteCommentsResponse> = dataApi.deleteCommentsData(id)
            call.enqueue(object: Callback<DataCollection.DeleteCommentsResponse> {

                override fun onResponse(call: Call<DataCollection.DeleteCommentsResponse>,
                                        response: Response<DataCollection.DeleteCommentsResponse>) {
                    if (response.isSuccessful && response.body() != null)
                    {
                        if(response.body()!!.code == 200){
                            Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<DataCollection.DeleteCommentsResponse>, t: Throwable) {
                    t.message?.let { Log.e("onFailure", it) }
                }
            })

            // 댓글 삭제 후 다시 ExtensionActivity 새로고침 하도록.
            (binding.root.context as ExtensionActivity).finish()
            val intent = Intent(binding.root.context, ExtensionActivity::class.java)
            (binding.root.context as ExtensionActivity).startActivity(intent)
            (binding.root.context as ExtensionActivity).overridePendingTransition(R.anim.none, R.anim.none)

    }

/*
            val boardId = PreferenceManger(binding.root.context).getInt("BoardId")
            val boardTitle = PreferenceManger(binding.root.context).getString("BoardTitle")
            val boardContent = PreferenceManger(binding.root.context).getString("BoardContent")
            val boardDate = PreferenceManger(binding.root.context).getString("BoardDate")
            val boardLikeCount = PreferenceManger(binding.root.context).getInt("BoardLikeCount")
            val boardCommentsCount = PreferenceManger(binding.root.context).getInt("BoardCommentsCount")
            val boardUserName = PreferenceManger(binding.root.context).getString("BoardUserName")
            intent.putExtra("id", boardId)
            intent.putExtra("title", boardTitle)
            intent.putExtra("content", boardContent)
            */

}