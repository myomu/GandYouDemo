package com.gatheringandyou.gandyoudemo.chatting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.gatheringandyou.gandyoudemo.databinding.ActivityChattingBinding
import com.gatheringandyou.gandyoudemo.shared.PreferenceManger
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*

class ChattingActivity : AppCompatActivity() {

    private val binding by lazy { ActivityChattingBinding.inflate(layoutInflater) }

    private lateinit var currentUserNickname: String            // 현재 닉네임
    private val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스
    private val chattingList = arrayListOf<ChattingLayoutData>()    // 리사이클러 뷰 목록
    private lateinit var chattingAdapter: ChattingAdapter   // 리사이클러 뷰 어댑터

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        Log.d("문서 아이디 넘겨오는 것 확인", intent.getStringExtra("DocumentId").toString())
        val DocumentId = intent.getStringExtra("DocumentId")
        val currentUserEmail = PreferenceManger(this).getString("userEmail").toString()
        currentUserNickname = PreferenceManger(this).getString("userNickname").toString()

        // 리사이클러뷰 설정
        chattingAdapter = ChattingAdapter(currentUserEmail)
        binding.rvChattingList.adapter = chattingAdapter
        binding.rvChattingList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        Log.d("스택 파악", LinearLayoutManager(this).stackFromEnd.toString())



        // 채팅창이 공백일 경우 버튼 비활성화
        binding.etChatting.addTextChangedListener { text ->
            binding.btnChattingSend.isEnabled = text.toString() != ""
        }

        // 입력 버튼
        binding.btnChattingSend.setOnClickListener {
            contentsDataSend(DocumentId, currentUserEmail)

        }

        // 채팅 데이터 불러오기
        getContentsData(DocumentId)

        // 채팅 데이터 실시간 통신
        //chattingList.add(ChattingLayoutData("알림", "$currentUserNickname 닉네임으로 입장했습니다.", "", ""))
        //val enterTime = Date(System.currentTimeMillis())

//        registration = db.collection("Chatting/" + DocumentId + "/chat")
//            .orderBy("time", Query.Direction.DESCENDING)
//            .limit(1)
//            .addSnapshotListener { snapshots, e ->
//                // 오류 발생 시
//                if (e != null) {
//                    Log.w("ChattingActivity", "Listen failed: $e")
//                    return@addSnapshotListener
//                }
//
//                // 원하지 않는 문서 무시
//                //if (snapshots!!.metadata.isFromCache) return@addSnapshotListener
//
//                // 문서 수신
//                for (doc in snapshots!!.documentChanges) {
//
//                    Log.d("chat 문서에서 데이터 받아오기 확인", doc.document.toString())
//
//                    val timestamp = doc.document["time"] as Timestamp
//                    //val timess = DateTime.
//
//                    // 문서가 추가될 경우 리사이클러 뷰에 추가
//                    if (doc.type == DocumentChange.Type.ADDED && timestamp.toDate() > enterTime) {
//                        val nickname = doc.document["nickname"].toString()
//                        Log.d("닉네임 확인", nickname)
//                        val contents = doc.document["contents"].toString()
//                        Log.d("내용 확인", contents)
//
//                        // 타임스탬프를 한국 시간, 문자열로 바꿈
//                        val sf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.KOREA)
//                        sf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
//                        val time = sf.format(timestamp.toDate())
//
//                        val item = ChattingLayoutData(nickname, contents, time, currentUserEmail)
//                        chattingList.add(item)
//                    }
//                    chattingAdapter.chattingListData = chattingList
//                    chattingAdapter.notifyDataSetChanged()
//                }
//            }


    }

    // 채팅 프래그먼트의 메인 화면. 리사이클러뷰로 채팅방 대화들을 표시해준다.
    private fun getContentsData(DocumentId: String?) {

        db.collection("Chatting/" + DocumentId + "/chat")
            .orderBy("time", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                // 오류 발생 시
                if (e != null) {
                    Log.w("ChatFragment", "Listen failed: $e")
                    return@addSnapshotListener
                }

                chattingList.clear()
                chattingList.add(ChattingLayoutData("알림", "$currentUserNickname 닉네임으로 입장했습니다.", "", ""))

                // 문서 수신 - 변경사항 없이 그냥 바로 문서를 불러옴. 실시간이기에 내용 수정되면 바로 표시됨!!
                for (doc in snapshots!!.documents) {

                    val nickname = doc.data?.get("nickname").toString()
                    val contents = doc.data?.get("contents").toString()
                    val timestamp = doc.data?.get("time") as Timestamp
                    val email = doc. data?.get("email").toString()

                    // 타임스탬프를 한국 시간, 문자열로 바꿈
                    val sf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.KOREA)
                    sf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
                    val time = sf.format(timestamp.toDate())
                    val data = ChattingLayoutData(nickname, contents, time, email)

                    chattingList.add(data)
                }
                chattingAdapter.chattingListData = chattingList
                chattingAdapter.notifyDataSetChanged()
                binding.rvChattingList.scrollToPosition(chattingList.size - 1)
            }
    }



    private fun contentsDataSend(DocumentId: String?, currentUserEmail:String) {

        val data = hashMapOf(
            "nickname" to currentUserNickname,
            "contents" to binding.etChatting.text.toString(),
            "time" to Timestamp.now(),
            "email" to currentUserEmail
        )

        db.collection("Chatting/" + DocumentId + "/chat").add(data)
            .addOnSuccessListener {
                binding.etChatting.text.clear()
                Log.w("ChattingActivity", "Document added: $it")
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "메세지 전송에 실패했습니다", Toast.LENGTH_SHORT).show()
                Log.w("ChattingActivity", "Error occurs: $e")
            }


        db.collection("Chatting").document(DocumentId!!)
            .update(
                mapOf(
                    "latestTime" to Timestamp.now(),
                    "recentContent" to binding.etChatting.text.toString(),
                )
            )
            .addOnSuccessListener {
                Log.w("ChattingDocumentUpdate", "Document updated: $it")
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "메세지 전송에 실패했습니다", Toast.LENGTH_SHORT).show()
                Log.w("ChattingDocumentUpdate", "Error updated: $e")
            }

    }
}

// 입력 데이터
// Firestore에 기록. 이때 입장하는 방의 문서Id를 받아와 chat Collection으로 이동하고 그곳에 채팅 데이터를 추가함.
// 입력된 데이터를 문서에 있는 최근 시간, 최근 메세지 내용을 update 시켜준다.