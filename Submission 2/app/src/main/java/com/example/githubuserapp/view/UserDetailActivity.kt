package com.example.githubuserapp.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.model.User
import com.example.githubuserapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_userdetail.*

class UserDetailActivity : AppCompatActivity() {

    private lateinit var userViewModel : UserViewModel
    companion object { const val userInfo = "userInfo"}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userdetail)

        initView()
    }

    private fun initView() {
        val userData = intent.getParcelableExtra<User>(userInfo)
        userData?.username?.let { getUserDetail(it) }
        btn_back.setOnClickListener { backToMainActivity() }
    }

    private fun getUserDetail(username: String) {
        // connect to userviewmodel
        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)
        // get data
        userViewModel.getUserDetail(username)
        userViewModel.getUser().observe(this, Observer { userDetail ->
            progressbar_userdetail.visibility = View.INVISIBLE
            txt_username_toolbar.text = userDetail.username

            Glide.with(this)
                .load(userDetail.avatar_url)
                .fitCenter()
                .centerCrop()
                .into(img_avatar_userdetail)

            txt_following.text = userDetail.following
            txt_follower.text = userDetail.followers
            txt_name_userdetail?.text = userDetail.name
            txt_bio_userdetail?.text = if (userDetail.bio != null) userDetail.bio else ""
            txt_company_userdetail?.text = if (userDetail.company != null) "Work at ${userDetail.company}" else ""
            txt_location_userdetail?.text = userDetail.location
            txt_repository.text = userDetail.public_repos

            btn_showgithub.setOnClickListener {
                val intentBrowser = Intent(Intent.ACTION_VIEW)
                intentBrowser.data = Uri.parse(userDetail.html_url)
                startActivity(intentBrowser)
            }
        })

        getFollowingFollowerTab()
    }

    private fun getFollowingFollowerTab() {
        val sectionsPagerAdapter = FollowListPageAdapter(this, supportFragmentManager)
        viewpager_userdetail.adapter = sectionsPagerAdapter
        layout_tab.setupWithViewPager(viewpager_userdetail)
    }
    private fun backToMainActivity() {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
    override fun onBackPressed() {
        backToMainActivity()
    }
}