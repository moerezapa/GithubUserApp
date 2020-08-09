package com.example.userconsumerapp.view.activity

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.userconsumerapp.R
import com.example.userconsumerapp.model.UserFavorite
import com.example.userconsumerapp.provider.MappingHelper
import com.example.userconsumerapp.provider.UserProvider
import com.example.userconsumerapp.view.adapter.UserFavoriteAdapter
import com.example.userconsumerapp.viewmodel.UserViewModel
import com.example.userconsumerapp.viewmodel.UserViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottomsheet_userdetail.view.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private var gridUserAdapter = UserFavoriteAdapter()
    private var coroutineScope = CoroutineScope(Dispatchers.Default)
    private val TAG = MainActivity::class.java.simpleName

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView(savedInstanceState)
    }

    private fun initView(savedInstanceState: Bundle?) {
        // initiate toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // pemanggilan class MainViewModel
        userViewModel = ViewModelProvider(this, UserViewModelFactory(this.application)).get(UserViewModel::class.java)
        showRecyclerList(savedInstanceState)

    }

    private fun showRecyclerList(savedInstanceState: Bundle?) {
        layout_nodata.visibility = View.INVISIBLE
        recyclerview_userlist.setHasFixedSize(true) // optimasi ukuran lebar dan tinggi secara otomatis
        recyclerview_userlist.layoutManager = LinearLayoutManager(this)
        gridUserAdapter = UserFavoriteAdapter()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler =  Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadUserAsync()
            }
        }

        contentResolver.registerContentObserver(UserProvider.CONTENT_URI, true, myObserver)

        // menjaga data ketika rotasi layar
        if (savedInstanceState == null)
            loadUserAsync()
            else {
                val list = savedInstanceState.getParcelableArrayList<UserFavorite>(EXTRA_STATE)
                list?.let { gridUserAdapter.listUserFavorites = it }
            }

//        searchUser("A")
        recyclerview_userlist.adapter = gridUserAdapter

        gridUserAdapter.setOnItemClickCallback(object : UserFavoriteAdapter.OnItemClickCallback {
            override fun onItemCLicked(userFav: UserFavorite) {
                showBottomSheetUserDetail(userFav)
            }
        })
    }

    private fun showBottomSheetUserDetail(user: UserFavorite) {
        val view = layoutInflater.inflate(R.layout.bottomsheet_userdetail, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(view)
        userViewModel.getUserDetail(user.username)
        userViewModel.getUser().observe(this, Observer { userDetail ->
            Glide.with(view.context) // memasukkan context.
                .load(user.avatar_url) // memasukkan sumber gambar, contohnya menggunakan url atau asset dari Drawable.
                .fitCenter()
                .centerCrop()
                .into(view.img_avatar_bottomsheet)

            view.txt_following_bottomsheet.text = resources.getString(R.string.following_bottomsheet, userDetail.following)
            view.txt_follower_bottomsheet.text = resources.getString(R.string.follower_bottomsheet, userDetail.followers)
            view.txt_name_bottomsheet.text = userDetail.name
            view.txt_repository_bottomsheet.text = resources.getString(R.string.repository_bottomsheet, userDetail.public_repos)

        })
        dialog.show()
    }

    private fun loadUserAsync() {
        coroutineScope.launch {
            val getUserFav = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(UserProvider.CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToUserArrayList(cursor)
            }
            val userFavList = getUserFav.await()
            withContext(Dispatchers.Main) {
                progressbar.visibility = View.INVISIBLE
                if (userFavList.isNotEmpty()) {
                    gridUserAdapter.setData(userFavList, this@MainActivity)
                    layout_nodata.visibility = View.INVISIBLE
                }
                else {
                    Log.d(TAG, "loadUserAsync: ")
                    Toast.makeText(applicationContext, resources.getString(R.string.nouserfavorite_message), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // yg bikin njaga data ketika layar dirotasi (?)
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, gridUserAdapter.listUserFavorites)
    }
}