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
import com.gatheringandyou.gandyoudemo.chatting.ChatUsers
import com.gatheringandyou.gandyoudemo.databinding.FragmentChatBinding
import com.gatheringandyou.gandyoudemo.shared.PreferenceManger
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ChatFragment : Fragment() {

    private var mBinding : FragmentChatBinding? = null
    private val binding get() = mBinding!!

    private lateinit var currentUserNickname: String            // 현재 닉네임
    private val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스
    private lateinit var registration: ListenerRegistration    // 문서 수신
    private val chatList = arrayListOf<ChatLayoutData>()    // 리사이클러 뷰 목록
    private lateinit var chatAdapter: ChatAdapter   // 리사이클러 뷰 어댑터

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

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
        //registration = db.collection("Chatting")
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

                Log.d("스냅샷", snapshots.toString())



                // 원하지 않는 문서 무시
                // if (snapshots!!.metadata.isFromCache) return@addSnapshotListener - 이것 제거해야 프래그먼트로 이동하면 바로 뜸.

                // 문서 수신 - 변경사항 없이 그냥 바로 문서를 불러옴. 실시간이기에 내용 수정되면 바로 표시됨!!
                for (doc in snapshots!!.documents) {

                    //val def = {email:String , nickname:ArrayList<*>}

                    lateinit var otherUserNickname: String//doc.data?.get("otherUserNickname").toString()
                    val recentContent = doc.data?.get("recentContent").toString()
                    val latestTime = doc.data?.get("latestTime") as Timestamp
                    val test: HashMap<String, Any> = doc.data!!["users"] as HashMap<String, Any>

                    val email: ArrayList<*> = test["email"] as ArrayList<*>
                    val nickname: ArrayList<*> = test["nickname"] as ArrayList<*>

                    val documentId = doc.id

                    Log.d("Array 테스트", test.toString())
                    Log.d("DocumentId 테스트", doc.id) // 문서 아이디 받아오는 것!! 예쓰 ㅋㅋㅋ!!
                    //val timestamp = doc.document["time"] as Timestamp

                    //Log.d("현재 유저 이름", currentUserNickname)

                    // 테스트용. 본인 이메일이 저장된 배열의 위치를 알아보고자 함. 나중에 프로필 수정 시 파이어베이스에도 수정이 되어야 닉네임 수정된 것으로 표시되기 때문.


                    //Log.d("본인 이메일이 저장된 배열의 위치는?", count.toString())

                     //users 배열에서 현재 유저의 이름이 아닌 것을 선택하여 data로 넘겨준다. 그렇게 함으로 채팅 리스트에 다른 사람의 이름이 보이게 된다.
                     //users 는 HashMap 타입으로 email과 nickname을 지닌다.
                    otherUserNickname = if (currentUserNickname != nickname[0].toString()) {
                        nickname[0].toString()
                    } else {
                        nickname[1].toString()
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