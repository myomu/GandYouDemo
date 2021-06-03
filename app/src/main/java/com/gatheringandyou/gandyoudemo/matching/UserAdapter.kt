package com.gatheringandyou.gandyoudemo.matching

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gatheringandyou.gandyoudemo.MainActivity
import com.gatheringandyou.gandyoudemo.R
import com.gatheringandyou.gandyoudemo.shared.PreferenceManger
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class UserAdapter(
    private val userList :MutableList<User>,val context : Context
) : RecyclerView.Adapter<UserAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(userList[position])

        holder.btnLike.setOnClickListener {
            //좋아요 기능
            Toast.makeText(context,userList[position].nickname,Toast.LENGTH_SHORT).show()

            val currentUserEmail = PreferenceManger(context).getString("userEmail").toString()
            val currentUserNickname = PreferenceManger(context).getString("userNickname").toString()
            val otherUserEmail = userList[position].email
            val otherUserNickname = userList[position].nickname
            holder.createChatRoom(currentUserEmail, otherUserEmail, currentUserNickname, otherUserNickname)

            //val intent = Intent(context, )
            (context as MatchingActivity).finish()
            //(context as MainActivity).move()


        }
        holder.btnDislike.setOnClickListener {
            //미안해요 기능
            userList.remove(userList[position])
            notifyDataSetChanged()
        }


    }

    override fun getItemCount() = userList.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val db = FirebaseFirestore.getInstance()

        private val imageView : ImageView = itemView.findViewById(R.id.image)
        private val nameTextView : TextView = itemView.findViewById(R.id.nicknameTextView)
        private val departTextView : TextView = itemView.findViewById(R.id.departTextView)
        private val ageTextView : TextView = itemView.findViewById(R.id.ageTextView)
        private val hobby1TextView : TextView = itemView.findViewById(R.id.hobby1TextView)
        private val hobby2TextView : TextView = itemView.findViewById(R.id.hobby2TextView)
        private val hobby3TextView : TextView = itemView.findViewById(R.id.hobby3TextView)
        val btnLike : Button = itemView.findViewById(R.id.btn1)
        val btnDislike : Button = itemView.findViewById(R.id.btn2)

        fun bind(user : User){
            nameTextView.text = user.nickname
            departTextView.text = user.department
            ageTextView.text = user.age.toString()
            hobby1TextView.text = user.hobby1
            hobby2TextView.text = user.hobby2
            hobby3TextView.text = user.hobby3

        }

        // 좋아요 버튼을 누르면 방을 생성.
        fun createChatRoom(currentUserEmail:String, otherUserEmail:String, currentUserNickname: String, otherUserNickname: String) {
            // 입력 데이터
            val data = hashMapOf(
                "latestTime" to Timestamp.now(),
                "recentContent" to " ",
                "entryExitCheck" to 0, // 방 출입 체크용.
                "nickname_1" to currentUserNickname,
                "nickname_2" to otherUserNickname,
                "users" to hashMapOf(
                    "email" to arrayListOf(currentUserEmail, otherUserEmail),
                )
            )
            // Firestore에 기록.
            db.collection("Chatting").add(data)
                .addOnSuccessListener {
                    Log.w("ChattingActivity", "Document added: $it")
                }
                .addOnFailureListener { e ->
                    Toast.makeText(itemView.rootView.context, "메세지 전송에 실패했습니다", Toast.LENGTH_SHORT).show()
                    Log.w("ChattingActivity", "Error occurs: $e")
                }

        }


    }



}