package com.gatheringandyou.gandyoudemo.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringandyou.gandyoudemo.bulletinboards.FreeBoard
import com.gatheringandyou.gandyoudemo.databinding.FragmentBulletinBinding


class BulletinBoardFragment : Fragment() {

    private var mBinding : FragmentBulletinBinding? = null

    //val binding by lazy { FragmentBulletinBinding.inflate(layoutInflater) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        binding.btnGotoFreeBoard.setOnClickListener {
//            activity?.let {
//                var intent = Intent(context, FreeBoard::class.java)
//                startActivity(intent)
//            }
//        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentBulletinBinding.inflate(inflater, container, false)

        mBinding = binding

        //프래그먼트에서 인텐트 사용하는 방법
        binding.btnGotoFreeBoard.setOnClickListener {
            activity?.let {
                var intent = Intent(context, FreeBoard::class.java)
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