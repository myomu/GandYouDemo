package com.gatheringandyou.gandyoudemo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.gatheringandyou.gandyoudemo.chatting.ChatAdapter
import com.gatheringandyou.gandyoudemo.chatting.ChatLayoutData
import com.gatheringandyou.gandyoudemo.databinding.FragmentChatBinding
import com.gatheringandyou.gandyoudemo.shared.PreferenceManger
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*


class ChatFragment : Fragment() {

    private var mBinding : FragmentChatBinding? = null
    private val binding get() = mBinding!!
    private lateinit var currentUserNickname: String            // 현재 닉네임
    private val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스
    private val chatList = arrayListOf<ChatLayoutData>()    // 리사이클러 뷰 목록
    private lateinit var chatAdapter: ChatAdapter   // 리사이클러 뷰 어댑터

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        mBinding = FragmentChatBinding.inflate(inflater, container, false)

        val context = requireContext()
        val currentUserEmail = PreferenceManger(context).getString("userEmail").toString()

        Log.d("현재 이메일 확인", currentUserEmail)

        // 리사이클러뷰 설정
        chatAdapter = ChatAdapter()
        binding.rvChat.adapter = chatAdapter
        binding.rvChat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        currentUserNickname = PreferenceManger(context).getString("userNickname").toString()

        // FireStore로 부터 데이터 불러오기
        db.collection("Chatting")
            .whereArrayContains("users.email", currentUserEmail) //여기 currentUserEmail 로 쿼리 selection을 한다. 해당 유저의 이메일이 있는 대화창 전부 찾음.
            .orderBy("latestTime", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, e ->
                // 오류 발생 시
                if (e != null) {
                    Log.w("ChatFragment", "Listen failed: $e")
                    return@addSnapshotListener
                }

                chatList.clear()

                // 문서 수신 - 변경사항 없이 그냥 바로 문서를 불러옴. 실시간이기에 내용 수정되면 바로 표시됨!!
                for (doc in snapshots!!.documents) {

                    lateinit var otherUserNickname: String
                    val recentContent = doc.data?.get("recentContent").toString()
                    val latestTime = doc.data?.get("latestTime") as Timestamp
                    val nickname1 = doc.data?.get("nickname_1").toString()
                    val nickname2 = doc.data?.get("nickname_2").toString()
                    val documentId = doc.id

                    otherUserNickname = if (currentUserNickname != nickname1) {
                        nickname1
                    } else {
                        nickname2
                    }

                    // 타임스탬프를 한국 시간, 문자열로 바꿈
                    val sf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.KOREA)
                    sf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
                    val time = sf.format(latestTime.toDate())
                    val data = ChatLayoutData(otherUserNickname, recentContent, time, documentId)

                    chatList.add(data)
                }
                chatAdapter.chatListData = chatList
                chatAdapter.notifyDataSetChanged()
            }

        return binding.root

    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }


}