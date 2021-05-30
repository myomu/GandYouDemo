package com.gatheringandyou.gandyoudemo.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.gatheringandyou.gandyoudemo.MainActivity
import com.gatheringandyou.gandyoudemo.bulletinboards.PostCreateActivity
import com.gatheringandyou.gandyoudemo.chatting.ChatLayoutData
import com.gatheringandyou.gandyoudemo.databinding.FragmentProfileBinding
import com.gatheringandyou.gandyoudemo.dialog.ProfileDialog
import com.gatheringandyou.gandyoudemo.profile.ProfileEditActivity
import com.gatheringandyou.gandyoudemo.shared.PreferenceManger
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ProfileFragment : Fragment() {

    private var mBinding : FragmentProfileBinding? = null

    //val currentUserEmail = PreferenceManger(this).getString("userEmail")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProfileBinding.inflate(inflater, container, false)

        mBinding = binding

        val context = requireContext()

        val nickname = PreferenceManger(context).getString("userNickname")//.getString("userNickname")//(activity as PreferenceManger).getString("userNickname")
        val age = PreferenceManger(context).getInt("userAge")
        val email = PreferenceManger(context).getString("userEmail")
        val department = PreferenceManger(context).getString("userDepartment")
        val hobby1 = PreferenceManger(context).getString("userHobby1")
        val hobby2 = PreferenceManger(context).getString("userHobby2")
        val hobby3 = PreferenceManger(context).getString("userHobby3")

        binding.tvProfileName.text = nickname
        binding.tvProfileAge.text = age.toString()
        binding.tvProfileEmail.text = email
        binding.tvProfileDepartment.text = department
        binding.tvProfileHobby1.text = hobby1
        binding.tvProfileHobby2.text = hobby2
        binding.tvProfileHobby3.text = hobby3

        binding.btnLogout.setOnClickListener {

            PreferenceManger(context).clearData()
            (activity as MainActivity).finish()

        }

        binding.btnProfileEdit.setOnClickListener {
            //val dialog = ProfileDialog(context)
            //dialog.editProfile()
            //onProfileEditDialogBtnClicked(it)
            val intent = Intent(activity, ProfileEditActivity::class.java)
            startActivity(intent)

        }


        return mBinding?.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false)
    }





//    override fun onClick(v: View) {
//        when (v.id) {
//            //button.id
//        }
//    }


    fun refreshFragment() {



        //mainActivity.supportFragmentManager.beginTransaction().detach(ProfileFragment()).attach(ProfileFragment()).commit()
        if( !isAdded) return
        val ft: FragmentTransaction = childFragmentManager.beginTransaction()
        ft.detach(this).attach(this).commit()
    }

    fun onProfileEditDialogBtnClicked(view: View) {
        val context = requireContext()
        val myDialog = ProfileDialog(context)
        myDialog.show()
    }


    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}