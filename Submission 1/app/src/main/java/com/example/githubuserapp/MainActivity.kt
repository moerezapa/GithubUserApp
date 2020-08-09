package com.example.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var userList = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // fetch array
        userList.addAll(UserData(this).userListData)
        showRecyclerList()
    }

    private fun showRecyclerList() {
        recyclerview_userlist.setHasFixedSize(true) // optimasi ukuran lebar dan tinggi secara otomatis
        recyclerview_userlist.layoutManager = GridLayoutManager(this, 2)
        val gridUserAdapter = UserAdapter(userList)
        recyclerview_userlist.adapter = gridUserAdapter

        gridUserAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemCLicked(data: User) {
                val intent = Intent(this@MainActivity, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.userInfo, data)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        })
    }

}