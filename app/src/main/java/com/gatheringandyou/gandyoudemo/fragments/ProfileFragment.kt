package com.gatheringandyou.gandyoudemo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringandyou.gandyoudemo.MainActivity
import com.gatheringandyou.gandyoudemo.databinding.FragmentProfileBinding
import com.gatheringandyou.gandyoudemo.shared.PreferenceManger


class ProfileFragment : Fragment() {

    private var mBinding : FragmentProfileBinding? = null

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

        return mBinding?.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}