package com.example.githubuserapp.view

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.model.User
import com.example.githubuserapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        // initiate toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // pemanggilan class MainViewModel
        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)
        showRecyclerList()

        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchUser(if (searchview.query.isNotEmpty()) searchview.query.toString() else "A")
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                searchUser(if (searchview.query.isNotEmpty()) searchview.query.toString() else "A")
                return false
            }
        })

    }

    private fun showRecyclerList() {
        layout_nodata.visibility = View.INVISIBLE
        recyclerview_userlist.setHasFixedSize(true) // optimasi ukuran lebar dan tinggi secara otomatis
        recyclerview_userlist.layoutManager = GridLayoutManager(this, 2)
        val gridUserAdapter = UserAdapter()

        // cara mendapatkan value dari LiveData yang ada pada kelas ViewModel
        userViewModel.getUserList().observe(this, Observer { user ->
            user?.let {
                layout_nodata.visibility = if (user.isEmpty()) View.VISIBLE else View.GONE
                progressbar.visibility = View.GONE
                gridUserAdapter.setData(user)
            }
        })

        searchUser("A")
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

    private fun searchUser(username: String) {
        progressbar.visibility = View.VISIBLE
        userViewModel.searchUsername(this, username)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_language_setting) startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        return super.onOptionsItemSelected(item)
    }
}