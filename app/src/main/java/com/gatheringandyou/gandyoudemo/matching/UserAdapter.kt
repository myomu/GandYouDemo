package com.gatheringandyou.gandyoudemo.matching

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gatheringandyou.gandyoudemo.R

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

        }
        holder.btnDislike.setOnClickListener {
            //미안해요 기능
            userList.remove(userList[position])
            notifyDataSetChanged()
        }


    }

    override fun getItemCount() = userList.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

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
            departTextView.text = user.depart
            ageTextView.text = user.age
            hobby1TextView.text = user.hobby1
            hobby2TextView.text = user.hobby2
            hobby3TextView.text = user.hobby3

        }
    }

}