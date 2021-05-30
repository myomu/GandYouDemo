package com.gatheringandyou.gandyoudemo.chatting

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gatheringandyou.gandyoudemo.databinding.ChatItemBinding

class ChatAdapter: RecyclerView.Adapter<ChatHolder>() {

    var chatListData = arrayListOf<ChatLayoutData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val binding = ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ChatHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {

        val chatData = chatListData[position]

        holder.setChatList(chatData)
        holder.clickItem(chatData)

    }

    override fun getItemCount(): Int {
        return chatListData.size
    }


}

class ChatHolder(val binding: ChatItemBinding): RecyclerView.ViewHolder(binding.root) {


    fun setChatList(data: ChatLayoutData) {

        binding.chatTvNickname.text = data.nickname
        binding.chatTvContents.text = data.contents
        binding.chatTvTime.text = data.time

    }

    fun clickItem(data: ChatLayoutData) {

        binding.root.setOnClickListener {

            val intent = Intent(binding.root.context, ChattingActivity::class.java)
            intent.putExtra("DocumentId", data.DocumentId)


            it.context.startActivity(intent)
            Log.d("클릭체크용", "체크")

            Toast.makeText(binding.root.context, "클릭된 아이템 = ${binding.chatTvContents.text}",
                Toast.LENGTH_SHORT).show()

        }

    }


}