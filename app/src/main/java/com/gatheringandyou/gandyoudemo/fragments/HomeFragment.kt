package com.gatheringandyou.gandyoudemo.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringandyou.gandyoudemo.databinding.FragmentHomeBinding
import com.gatheringandyou.gandyoudemo.matching.MatchingActivity


class HomeFragment : Fragment() {

    private var mBinding : FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        mBinding = binding
        binding.btnMatch.setOnClickListener {
            activity?.let {
                var intent = Intent(context, MatchingActivity::class.java)
                startActivity(intent)
            }
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