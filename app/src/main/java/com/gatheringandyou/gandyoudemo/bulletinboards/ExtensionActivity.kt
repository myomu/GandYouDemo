package com.gatheringandyou.gandyoudemo.bulletinboards

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.gatheringandyou.gandyoudemo.R
import com.gatheringandyou.gandyoudemo.bulletinboards.DataCommunication.postCommentsData
import com.gatheringandyou.gandyoudemo.databinding.ActivityExtensionBinding
import com.gatheringandyou.gandyoudemo.databinding.LoadingBinding
import com.gatheringandyou.gandyoudemo.dialog.LoadingDialog
import com.gatheringandyou.gandyoudemo.login.repository
import com.gatheringandyou.gandyoudemo.shared.PreferenceManger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class ExtensionActivity : AppCompatActivity() {

    private val binding by lazy { ActivityExtensionBinding.inflate(layoutInflater) }
    private lateinit var commentsAdapter: CommentsAdapter // 댓글 RecyclerView Adapter
    val mCallBack = this
    lateinit var listData: MutableList<FreeBoardData>
    var freeBoardId: Int = 0
    private val reportData = arrayOf(
        "낚시/놀람/도배",
        "음란물/불건전한 만남 및 대화",
        "유출/사칭/사기",
        "상업적 광고 및 판매",
        "욕설/비하",
        "정당/정치인 비하 및 선거운동")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    // 메뉴의 수정을 누르면 PostCreateActivity로 이동하고 종료 후 -> ExtensionActivity 로 돌아오면서
    // onPause가 onResume() 단계로 오기 때문에 새로고침 하는 것처럼 된다. 액티비티 생명주기를 참고할 것.
    override fun onResume() {
        super.onResume()

        // 처음 화면 진입시 로딩창 띄우도록.
        showLoadingDialog()

        // 게시글의 id 값을 넘겨준다. 이 게시판 데이터의 primary_key
        freeBoardId = PreferenceManger(this).getInt("BoardId")

        // 게시판 데이터 받아옴
        loadFreeBoardData()

        //기본 액션바 처리 부분
        setSupportActionBar(binding.tbExtension)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbExtension.title = " "

        // 리사이클러뷰 관련 코드
        commentsAdapter = CommentsAdapter(this)
        binding.rvComments.adapter = commentsAdapter
        binding.rvComments.layoutManager = LinearLayoutManager(this)

        // 댓글 데이터 불러오는 함수
        DataCommunication.loadCommentsData(this, freeBoardId)

        // 채팅창이 공백일 경우 버튼 비활성화
        binding.etBoardChatting.addTextChangedListener { text ->
            binding.btnBoardChatSend.isEnabled = text.toString() != ""
        }

        // 댓글 추가
        binding.btnBoardChatSend.setOnClickListener {
            postCommentsData(this, binding.etBoardChatting.text.toString(), freeBoardId)
            binding.etBoardChatting.text.clear()
            finish()
            startActivity(intent)
            overridePendingTransition(R.anim.none, R.anim.none)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.extension_menu, menu)

        // 게시글이 본인 이메일과 같으면 수정, 삭제 메뉴가 보임.
        val user = PreferenceManger(this).getString("userEmail")
        val boardOwner = PreferenceManger(this).getString("BoardUserEmail")

        val item1 = menu?.findItem(R.id.item_edit)
        val item2 = menu?.findItem(R.id.item_delete)

        if ( user != boardOwner) {

            item1?.isVisible = false
            item2?.isVisible = false

        } else {

            item1?.isVisible = true
            item2?.isVisible = true

        }

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            android.R.id.home -> {
                someFunction()
                return true
            }

            R.id.item_report -> {

                reportDialog()
                return true

            }
            R.id.item_edit -> {

                val id = freeBoardId
                val title = PreferenceManger(this).getString("BoardTitle")
                val content = PreferenceManger(this).getString("BoardContent")

                val intent = Intent(this, PostCreateActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("title", title)
                intent.putExtra("content", content)
                intent.putExtra("checkEdit", 1)
                startActivity(intent)
                return true
            }
            R.id.item_delete -> {

                val dialog = AlertDialog.Builder(binding.root.context)
                dialog.setTitle("게시글을 삭제하시겠습니까?")
                dialog.setIcon(R.drawable.ic_round_delete)

                val dialogListener = object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE ->
                                DataCommunication.deleteBulletin(mCallBack, freeBoardId)
                        }
                    }
                }
                dialog.setPositiveButton("네", dialogListener)
                dialog.setNegativeButton("아니오", null)
                dialog.show()

                return true

            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }

    }

    // 종료 함수
    private fun someFunction() {
        finish()
        overridePendingTransition(R.anim.none, R.anim.horizon_exit)
    }

    // 뒤로가기 버튼 함수
    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            // back 버튼으로 화면 종료가 야기되면 동작한다.
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }
    }

    // 리사이클러뷰 어댑터에 데이터 setting 시켜주는 함수.
    fun loadCommentsComplete(data: MutableList<DataCollection.GetCommentsData>) {
        commentsAdapter.commentsListData = data
        commentsAdapter.notifyDataSetChanged()
        val commentsCount = data.size
        binding.tvExtensionCommentsCount.text = commentsCount.toString()
    }


    // 받아온 data 를 layout 에 setting 시켜줌.
    fun loadFreeBoardComplete(data: MutableList<FreeBoardData>) {

        listData = data

        Log.d("데이터 확인", listData.toString())

        // 받아온 데이터를 ExtensionActivity layout 에 setting 해준다.
        binding.tvExtensionUsername.text = listData[0].username
        binding.tvExtensionTitle.text = listData[0].freeboard_title
        binding.tvExtensionContent.text = listData[0].freeboard_content

        val dataTime = listData[0].freeboard_date //mysql에서 받아오는 시간과 날짜 데이터

        // 시간이 데이터베이스도 서울 표준시로 되어 있지만 안드로이드 기기에서도 서울 시간으로 하면 시간이 +9시가 되어 calendar 타입 변수에 Date타입
        // 을 넣고 -9시간을 설정한 뒤 해당 값들을 calendar.time 으로 넣어주었다.
        val calendar = Calendar.getInstance()
        calendar.time = dataTime
        calendar.add(Calendar.HOUR, -9)

        val dataYearFormat = SimpleDateFormat("yyyy", Locale.KOREA).format(calendar.time)
        val dataDateFormat = SimpleDateFormat("dd", Locale.KOREA).format(calendar.time)

        val cal = Calendar.getInstance() // 현재 시간
        val years = cal.get(Calendar.YEAR) // 현재 년도
        val date = cal.get(Calendar.DATE) // 현자 일자

        if (years == dataYearFormat.toInt()) {

            if (date == dataDateFormat.toInt()) {
                val formatterHour = SimpleDateFormat("HH:mm", Locale.getDefault())
                val formattedHour = formatterHour.format(calendar.time)
                binding.tvExtensionDate.text  = formattedHour
            } else {
                val formatterDate = SimpleDateFormat("MM/dd", Locale.getDefault())
                val formattedDate = formatterDate.format(calendar.time)
                binding.tvExtensionDate.text = formattedDate
            }

        } else {
            //Log.d("현재시간년도와 데이터의시간년도가 다를경우", "출력")
            val formatterYear = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            val formattedYear = formatterYear.format(calendar.time)
            binding.tvExtensionDate.text  = formattedYear
        }
    }

    // Freeboard 에서 아이템 클릭하면 해당 아이템의 id 값을 intent 로 넘겨받아 이 id_freeboard 로 다시 서버에 DB query 요청후 데이터를 받아옴.
    fun loadFreeBoardData(){

        val retrofit = repository.getApiClient()
        if (retrofit != null) {
            DataCommunication.dataApi = retrofit.create(InterfaceCollection::class.java)
        }

        val postIdData = DataCollection.PostFreeBoardId(freeBoardId)

        val call: Call<FreeBoardResponse> = DataCommunication.dataApi.getExtensionBulletinData(postIdData)
        call.enqueue(object: Callback<FreeBoardResponse> {

            override fun onResponse(call: Call<FreeBoardResponse>, response: Response<FreeBoardResponse>) {
                if (response.isSuccessful && response.body() != null)
                {
                    if(response.body()!!.code == 200){
                        // 여기서 데이터를 받아와 mutableList 로 넘겨준다.
                        loadFreeBoardComplete(response.body()!!.data)

                    }else{
                        Toast.makeText(this@ExtensionActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<FreeBoardResponse>, t: Throwable) {
                t.message?.let { Log.e("onFailure", it) }
            }

        })
    }

    private fun reportDialog() {
        AlertDialog.Builder(this)
            .setTitle(" 신고 사유 선택 ")
            .setNegativeButton("취소", null)
            .setItems(reportData, ListListener())
            .show()
    }

    inner class ListListener: DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {

            DataCommunication.sendReportData(this@ExtensionActivity, reportData[which], freeBoardId)

        }

    }

    // 로딩 다이얼로그.
    private fun showLoadingDialog() {
        val dialog = LoadingDialog(this)
        CoroutineScope(Dispatchers.Main).launch {
            dialog.show()
            delay(800)
            dialog.dismiss()
        }
    }

}