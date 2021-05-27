package com.gatheringandyou.gandyoudemo.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.ArrayAdapter
import com.gatheringandyou.gandyoudemo.R

class ProfileDialog(context: Context): Dialog(context) {
    private val dialog = Dialog(context)

    //private val binding by lazy { ProfileEditDialogBinding.inflate(layoutInflater) }

    private val goContext = context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentView(R.layout.profile_edit_dialog)
//
//        //배경 투명하게
//        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//        val data = listOf("- 선택하세요 -", "자전거타기", "헬스", "수영", "독서", "영화감상", "음악회", "팝송 듣기", "산책")
//
//        val adapter = ArrayAdapter<String>(this.context, android.R.layout.simple_list_item_1, data)
//
//        binding.spinnerHobby1.adapter = adapter
//        binding.spinnerHobby2.adapter = adapter
//        binding.spinnerHobby3.adapter = adapter

    }

//    fun editProfile() {
//        //dialog.show()
//        dialog.setTitle("프로필 수정")
//        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
//                                    WindowManager.LayoutParams.WRAP_CONTENT)
//        dialog.setContentView(R.layout.profile_edit_dialog)
//
//        //dialog.setCancelable(true)
//        dialog.setCanceledOnTouchOutside(true)
//        dialog.show()
//    }
}