package com.gatheringandyou.gandyoudemo.chatting

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gatheringandyou.gandyoudemo.R
import com.gatheringandyou.gandyoudemo.databinding.ChattingItemBinding

class ChattingAdapter(private val currentUserEmail: String): RecyclerView.Adapter<ChattingHolder>() {

    var chattingListData = arrayListOf<ChattingLayoutData>()
    //var currentUserEmail

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingHolder {
        val binding = ChattingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)


        return ChattingHolder(binding, currentUserEmail)
    }

    override fun onBindViewHolder(holder: ChattingHolder, position: Int) {

        if (currentUserEmail == chattingListData[position].email) {
            holder.binding.chattingCardView.setCardBackgroundColor(Color.parseColor("#FFCE3A"))
        } else {
            holder.binding.chattingCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
        }

        val chattingData = chattingListData[position]

        holder.setChattingList(chattingData)
    }

    override fun getItemCount(): Int {
        return chattingListData.size
    }


}

class ChattingHolder(val binding: ChattingItemBinding, private val currentUserEmail: String): RecyclerView.ViewHolder(binding.root) {

    fun setChattingList(data: ChattingLayoutData) {

        // 현재 유저의 이메일과 글쓴이의 이메일이 같을 경우 배경을 노란색으로 변경
//        if (currentUserEmail == data.email) {
//            // Color.parseColor("#afe3ff") 이런 방식도 있음.
//            binding.chattingCardView.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.currentUserContentsBox))
//        }

        binding.chattingTvNickname.text = data.nickname
        binding.chattingTvContents.text = data.contents
        binding.chattingTvTime.text = data.time

    }

}