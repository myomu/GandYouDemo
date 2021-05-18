package com.gatheringandyou.gandyoudemo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringandyou.gandyoudemo.databinding.FragmentChatBinding


class ChatFragment : Fragment() {

    private var mBinding : FragmentChatBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentChatBinding.inflate(inflater, container, false)

        mBinding = binding

        return mBinding?.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}