package com.example.githubuserapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.model.User
import com.example.githubuserapp.model.UserFavorite
import com.example.githubuserapp.view.activity.UserDetailActivity
import com.example.githubuserapp.view.adapter.FollowListsAdapter
import com.example.githubuserapp.viewmodel.FollowingViewModel
import com.example.githubuserapp.viewmodel.FollowingViewModelFactory
import kotlinx.android.synthetic.main.fragment_followlists.*

class FollowingFragment(private var userType: String) : Fragment() {

    // contains list of following and followers each user
    private lateinit var followingViewModel: FollowingViewModel

    // Inflate the layout for this fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_followlists, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followingViewModel = ViewModelProvider(requireActivity(), FollowingViewModelFactory(requireActivity().application)).get(FollowingViewModel::class.java)
        if (userType == UserDetailActivity.detailUser) {
            val user = requireActivity().intent.getParcelableExtra<User>(UserDetailActivity.userInfo)
            user?.let { followingViewModel.getFollowingList(it.username) }
        }
            else {
                val user = requireActivity().intent.getParcelableExtra<UserFavorite>(UserDetailActivity.userInfo)
                user?.let { followingViewModel.getFollowingList(it.username) }
            }
        showRecyclerList()
    }

    private fun showRecyclerList() {
        recyclerview_followingList.setHasFixedSize(true) // optimasi ukuran lebar dan tinggi secara otomatis
        recyclerview_followingList.layoutManager = LinearLayoutManager(activity)
        val followListsAdapter = FollowListsAdapter()

        followingViewModel.followingUserList().observe(requireActivity(), Observer {
            it?.let {
                progressbar_fragment.visibility = View.GONE
                followListsAdapter.setData(it)
            }
        })

        recyclerview_followingList.adapter = followListsAdapter
        followListsAdapter.setOnItemClickCallback(object :
            FollowListsAdapter.OnItemClickCallback {
            override fun onItemCLicked(data: User) {
                val intent = Intent(activity, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.userInfo, data)
                startActivity(intent)
            }
        })
    }
}