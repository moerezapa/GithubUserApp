package com.example.githubuserapp.view.activity

import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.localdb.AppDatabase
import com.example.githubuserapp.model.UserDao
import com.example.githubuserapp.model.UserFavorite
import com.example.githubuserapp.provider.MappingHelper
import com.example.githubuserapp.provider.UserProvider
import com.example.githubuserapp.view.adapter.UserFavoriteAdapter
import kotlinx.android.synthetic.main.activity_user_favorite.*
import kotlinx.coroutines.*

class UserFavoriteActivity : AppCompatActivity() {

    private var coroutineScope = CoroutineScope(Dispatchers.Default)

    private lateinit var daoUser : UserDao
    private lateinit var uriWithId: Uri
    private lateinit var userFavAdapter : UserFavoriteAdapter
    private val TAG = UserFavoriteActivity::class.java.simpleName

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_favorite)

        initView(savedInstanceState)
    }

    private fun initView(savedInstanceState: Bundle?) {
        val database = AppDatabase.getAppDatabase(applicationContext)
        database?.let {
            daoUser = it.getUserDao()
        }

        recyclerview_userfavorite.setHasFixedSize(true)
        recyclerview_userfavorite.layoutManager = LinearLayoutManager(this)
        userFavAdapter = UserFavoriteAdapter()

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
                list?.let { userFavAdapter.listUserFavorites = it }
            }

        userFavAdapter.setOnItemClickCallback(object : UserFavoriteAdapter.OnItemClickCallback{
            override fun onItemCLicked(userFav: UserFavorite) {
                coroutineScope.launch {
                    uriWithId = Uri.parse("${UserProvider.CONTENT_URI}/${userFav.id}")
                    contentResolver.delete(uriWithId, null, null)
                    withContext(Dispatchers.Main) {
                        layout_nodata_userfavorite.isVisible = userFavAdapter.itemCount == 0
                    }
                }
            }
        })

        recyclerview_userfavorite.adapter = userFavAdapter

        btn_back_userfavorite.setOnClickListener { finish() }
    }

    private fun loadUserAsync() {
        coroutineScope.launch {
            val getUserFav = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(UserProvider.CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToUserArrayList(cursor)
            }
            val userFavList = getUserFav.await()
            withContext(Dispatchers.Main) {
                progressbar_userfavorite.visibility = View.INVISIBLE
                if (userFavList.isNotEmpty()) {
                    userFavAdapter.setData(userFavList, this@UserFavoriteActivity)
                    layout_nodata_userfavorite.visibility = View.INVISIBLE
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
        outState.putParcelableArrayList(EXTRA_STATE, userFavAdapter.listUserFavorites)
    }
}