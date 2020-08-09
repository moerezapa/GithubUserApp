package com.example.githubuserapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_userdetail.*

class UserDetailActivity : AppCompatActivity() {

    companion object { val userInfo = "userInfo"}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userdetail)

        initView()
    }

    private fun initView() {
        val userData = intent.getParcelableExtra<User>(userInfo)

        Glide.with(this)
                .load(userData?.avatar)
                    .fitCenter()
                        .centerCrop()
                            .into(img_avatar_userdetail)
        txt_username_toolbar.text = userData?.username
        txt_following.text = userData?.following
        txt_follower.text = userData?.follower
        txt_name_userdetail.text = userData?.name
        txt_company_userdetail.text = "Work at ${userData?.company}"
        txt_location_userdetail.text = userData?.location
        txt_repository.text = userData?.repository

        btn_showgithub.setOnClickListener {
            val intentBrowser = Intent(Intent.ACTION_VIEW)
            intentBrowser.data = Uri.parse("https://github.com/${userData?.username}")
            startActivity(intentBrowser)
        }

        btn_back.setOnClickListener { backtoMainActivity() }
    }

    private fun backtoMainActivity() {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
    override fun onBackPressed() {
//        super.onBackPressed()
        backtoMainActivity()
    }
}