package com.gatheringandyou.gandyoudemo.bulletinboards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import com.gatheringandyou.gandyoudemo.R
import com.gatheringandyou.gandyoudemo.databinding.ActivityExtensionBinding


class ExtensionActivity : AppCompatActivity() {

    private val binding by lazy { ActivityExtensionBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //기본 액션바 처리 부분
        setSupportActionBar(binding.tbExtension)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbExtension.title = " "

        binding.tvExtensionUsername.text = intent.getStringExtra("userName")
        binding.tvExtensionDate.text = intent.getStringExtra("date")

        binding.tvExtensionTitle.text = intent.getStringExtra("title")
        binding.tvExtensionContent.text = intent.getStringExtra("content")

        binding.tvExtensionLikeCount.text = "${intent.getIntExtra("likeCount", 0)}"
        binding.tvExtensionCommentCount.text = "${intent.getIntExtra("commentsCount", 0)}"

        //binding.btnExtensionLike

        //애니메이션 처리
        overridePendingTransition(R.anim.horizon_enter, R.anim.none)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.extension_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            android.R.id.home -> {
                someFunction()
                return true
            }
            R.id.item_extension_menu -> {

                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }

    }

    private fun someFunction() {
        finish()
        overridePendingTransition(R.anim.none, R.anim.horizon_exit)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            // back 버튼으로 화면 종료가 야기되면 동작한다.
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }
    }
}