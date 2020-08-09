package com.example.userconsumerapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userconsumerapp.R
import com.example.userconsumerapp.model.User
import com.example.userconsumerapp.model.UserFavorite
import com.example.userconsumerapp.view.activity.UserDetailActivity
import com.example.userconsumerapp.view.adapter.FollowListsAdapter
import com.example.userconsumerapp.viewmodel.FollowersViewModel
import com.example.userconsumerapp.viewmodel.FollowersViewModelFactory
import kotlinx.android.synthetic.main.fragment_followlists.*

class FollowersFragment(private var userType: String) : Fragment() {

    // contains list of following and followers each user
    private lateinit var followersViewModel: FollowersViewModel

    // Inflate the layout for this fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_followlists, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followersViewModel = ViewModelProvider(requireActivity(), FollowersViewModelFactory(requireActivity().application)).get(FollowersViewModel::class.java)
        if (userType == UserDetailActivity.detailUser) {
            val user = requireActivity().intent.getParcelableExtra<User>(UserDetailActivity.userInfo)
            user?.let { followersViewModel.getFollowersList(it.username) }
        }
            else {
                val userFav = requireActivity().intent.getParcelableExtra<UserFavorite>(UserDetailActivity.userInfo)
                userFav?.let { followersViewModel.getFollowersList(it.username) }
            }
        showRecyclerList()
    }

    private fun showRecyclerList() {
        recyclerview_followingList.setHasFixedSize(true) // optimasi ukuran lebar dan tinggi secara otomatis
        recyclerview_followingList.layoutManager = LinearLayoutManager(activity)
        val followListsAdapter = FollowListsAdapter()

        // cara mendapatkan value dari LiveData yang ada pada kelas ViewModel
        followersViewModel.followersUserList().observe(requireActivity(), Observer {
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