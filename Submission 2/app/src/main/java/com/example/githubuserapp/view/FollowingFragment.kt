package com.example.githubuserapp.view

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
import com.example.githubuserapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_followlists.*

class FollowingFragment : Fragment() {

    // contains list of following and followers each user
    private lateinit var userViewModel: UserViewModel

    // Inflate the layout for this fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_followlists, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(activity!!, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)
        val user = activity!!.intent.getParcelableExtra<User>(UserDetailActivity.userInfo)
        userViewModel.getFollowingList(activity!!, user.username)
        showRecyclerList()
    }

    private fun showRecyclerList() {
        recyclerview_followingList.setHasFixedSize(true) // optimasi ukuran lebar dan tinggi secara otomatis
        recyclerview_followingList.layoutManager = LinearLayoutManager(activity)
        val followListsAdapter = FollowListsAdapter()

        // cara mendapatkan value dari LiveData yang ada pada kelas ViewModel
        userViewModel.followingUserList().observe(activity!!, Observer { user ->
            user?.let {
                progressbar_fragment.visibility = View.GONE
                followListsAdapter.setData(user)
            }
        })

        recyclerview_followingList.adapter = followListsAdapter
        followListsAdapter.setOnItemClickCallback(object : FollowListsAdapter.OnItemClickCallback {
            override fun onItemCLicked(data: User) {
                val intent = Intent(activity, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.userInfo, data)
                startActivity(intent)
            }
        })
    }
}