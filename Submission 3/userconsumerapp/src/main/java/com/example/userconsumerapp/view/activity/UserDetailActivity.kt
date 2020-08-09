package com.example.userconsumerapp.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.userconsumerapp.R
import com.example.userconsumerapp.model.User
import com.example.userconsumerapp.model.UserFavorite
import com.example.userconsumerapp.view.adapter.FollowListPageAdapter
import com.example.userconsumerapp.viewmodel.UserViewModel
import com.example.userconsumerapp.viewmodel.UserViewModelFactory
import kotlinx.android.synthetic.main.activity_userdetail.*

class UserDetailActivity : AppCompatActivity() {

    private lateinit var userViewModel : UserViewModel
    private var userType = ""

    companion object {
        const val from = "from"
        const val detailUser = "detailUser"
        const val favoriteUser = "favoriteUser"
        const val userInfo = "userInfo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userdetail)

        initView()
    }

    private fun initView() {
        userType = intent.getStringExtra(from).toString()

        if (userType == detailUser) {
            val userData = intent.getParcelableExtra<User>(userInfo)
            userData?.let { getUserDetail(it.username) }
        }
            else {
                val userFav = intent.getParcelableExtra<UserFavorite>(userInfo)
                userFav?.let { getUserDetail(it.username) }
            }

        btn_back.setOnClickListener { backToMainActivity() }
    }

    private fun getUserDetail(username: String) {
        // connect to userviewmodel
        userViewModel = ViewModelProvider(this, UserViewModelFactory(this.application)).get(UserViewModel::class.java)
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
            txt_company_userdetail?.text = if (userDetail.company != null) resources.getString(R.string.workat_company, userDetail.company) else ""
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
        val sectionsPagerAdapter = FollowListPageAdapter(this, supportFragmentManager, userType)
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