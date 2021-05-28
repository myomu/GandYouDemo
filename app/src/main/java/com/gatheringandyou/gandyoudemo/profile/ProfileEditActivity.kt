package com.gatheringandyou.gandyoudemo.profile

import android.content.Context
import android.icu.number.IntegerWidth
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.gatheringandyou.gandyoudemo.MainActivity
import com.gatheringandyou.gandyoudemo.R
import com.gatheringandyou.gandyoudemo.bulletinboards.DataCommunication.postProfileData
import com.gatheringandyou.gandyoudemo.databinding.ActivityProfileEditBinding
import com.gatheringandyou.gandyoudemo.fragments.ProfileFragment
import com.gatheringandyou.gandyoudemo.shared.PreferenceManger

class ProfileEditActivity : AppCompatActivity() {

    private val binding by lazy { ActivityProfileEditBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val nick = PreferenceManger(applicationContext).getString("userNickname")
        val age = PreferenceManger(applicationContext).getInt("userAge").toString()
        val depart = PreferenceManger(applicationContext).getString("userDepartment").toString()
        binding.etProfileEditNickname.setText(nick)
        binding.etProfileEditAge.setText(age)
        binding.etProfileEditDepartment.setText(depart)


        var selectHobby1 = "1"
        var selectHobby2 = "2"
        var selectHobby3 = "3"

        //애니메이션 처리
        overridePendingTransition(R.anim.horizon_enter, R.anim.none)

        val data = listOf("없음", "자전거타기", "헬스", "수영", "독서", "영화감상", "음악회", "팝송 듣기", "산책")

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)

        binding.spinnerHobby1.adapter = adapter
        binding.spinnerHobby2.adapter = adapter
        binding.spinnerHobby3.adapter = adapter

        //처음 스피너 세팅
        val sfv1 = PreferenceManger(this).getString("userHobby1")
        when (sfv1) {

            "자전거타기" -> binding.spinnerHobby1.setSelection(1)
            "헬스" -> binding.spinnerHobby1.setSelection(2)
            "수영" -> binding.spinnerHobby1.setSelection(3)
            "독서" -> binding.spinnerHobby1.setSelection(4)
            "영화감상" -> binding.spinnerHobby1.setSelection(5)
            "음악회" -> binding.spinnerHobby1.setSelection(6)
            "팝송 듣기" -> binding.spinnerHobby1.setSelection(7)
            "산책" -> binding.spinnerHobby1.setSelection(8)

        }
        val sfv2 = PreferenceManger(this).getString("userHobby2")
        when (sfv2) {

            "자전거타기" -> binding.spinnerHobby2.setSelection(1)
            "헬스" -> binding.spinnerHobby2.setSelection(2)
            "수영" -> binding.spinnerHobby2.setSelection(3)
            "독서" -> binding.spinnerHobby2.setSelection(4)
            "영화감상" -> binding.spinnerHobby2.setSelection(5)
            "음악회" -> binding.spinnerHobby2.setSelection(6)
            "팝송 듣기" -> binding.spinnerHobby2.setSelection(7)
            "산책" -> binding.spinnerHobby2.setSelection(8)

        }
        val sfv3 = PreferenceManger(this).getString("userHobby3")
        when (sfv3) {

            "자전거타기" -> binding.spinnerHobby3.setSelection(1)
            "헬스" -> binding.spinnerHobby3.setSelection(2)
            "수영" -> binding.spinnerHobby3.setSelection(3)
            "독서" -> binding.spinnerHobby3.setSelection(4)
            "영화감상" -> binding.spinnerHobby3.setSelection(5)
            "음악회" -> binding.spinnerHobby3.setSelection(6)
            "팝송 듣기" -> binding.spinnerHobby3.setSelection(7)
            "산책" -> binding.spinnerHobby3.setSelection(8)

        }


        //binding.spinnerHobby1.setSelection(0)

        binding.spinnerHobby1.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //코드 작성 부분
                //val selectedValue = data[position]
                selectHobby1 = data[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //코드 작성 부분
            }
        }

        binding.spinnerHobby2.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //코드 작성 부분
                selectHobby2 = data[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //코드 작성 부분
            }
        }

        binding.spinnerHobby3.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //코드 작성 부분
                selectHobby3 = data[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //코드 작성 부분
            }
        }



        //취소(X) 버튼 이벤트 처리
        binding.btnProfileEditCancel.setOnClickListener(BtnCloseListener())

        //수정 버튼 이벤트 처리
        binding.btnProfileEditSet.setOnClickListener {

            val nickname = binding.etProfileEditNickname.text.toString()
            val age: Int = Integer.parseInt(binding.etProfileEditAge.text.toString())
            val department = binding.etProfileEditDepartment.text.toString()


            PreferenceManger(applicationContext).setString("userNickname", nickname)
            PreferenceManger(applicationContext).setInt("userAge", age)
            PreferenceManger(applicationContext).setString("userDepartment", department)
            PreferenceManger(applicationContext).setString("userHobby1", selectHobby1)
            PreferenceManger(applicationContext).setString("userHobby2", selectHobby2)
            PreferenceManger(applicationContext).setString("userHobby3", selectHobby3)

            postProfileData(this, nickname, department, age, selectHobby1, selectHobby2, selectHobby3)


            val frag = ProfileFragment()
            //val fragM = FragmentManager().beginTransaction()
            //refreshFragment(frag, supportFragmentManager.getFragment(ProfileFragment))
            frag.refreshFragment()

            //val mainActivity = (activity as MainActivity)

            //MainActivity().supportFragmentManager.beginTransaction().detach(ProfileFragment()).attach(ProfileFragment()).commit()

//            val fr = supportFragmentManager.findFragmentById(R.id.freeBoard_view)
//            supportFragmentManager.beginTransaction().detach(ProfileFragment())
//            supportFragmentManager.beginTransaction().attach(ProfileFragment())
//            supportFragmentManager.beginTransaction().commit()

            onBackPressed()

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            // back 버튼으로 화면 종료가 야기되면 동작한다.
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }
    }

    inner class BtnCloseListener : View.OnClickListener {
        override fun onClick(v: View?) {
            Log.d("클릭 테스트", "클릭되나?")
            onBackPressed()
        }
    }

    private fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }

}