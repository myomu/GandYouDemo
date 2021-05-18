package com.gatheringandyou.gandyoudemo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gatheringandyou.gandyoudemo.Memo
import com.gatheringandyou.gandyoudemo.databinding.LayoutRecyclerItemBinding
import java.text.SimpleDateFormat

class BoardAdapter:RecyclerView.Adapter<Holder>() {

    var listData = mutableListOf<Memo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = LayoutRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val memo = listData.get(position)
        holder.setMemo(memo)
    }

    override fun getItemCount(): Int {
        return listData.size
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
}