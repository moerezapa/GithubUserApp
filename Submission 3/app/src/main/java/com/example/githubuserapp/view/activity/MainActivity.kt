package com.example.githubuserapp.view.activity

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.localdb.AppDatabase
import com.example.githubuserapp.model.User
import com.example.githubuserapp.model.UserDao
import com.example.githubuserapp.provider.MappingHelper
import com.example.githubuserapp.provider.UserProvider
import com.example.githubuserapp.reminder.DailyReminderReceiver
import com.example.githubuserapp.view.adapter.UserAdapter
import com.example.githubuserapp.viewmodel.UserViewModel
import com.example.githubuserapp.viewmodel.UserViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottomsheet_notificationsetting.view.*
import kotlinx.android.synthetic.main.bottomsheet_userdetail.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var daoUser : UserDao
    private var coroutineScope = CoroutineScope(Dispatchers.Default)
    private var isReminder = false
    private var reminderReceiver = DailyReminderReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        prepareLocalDB()
    }

    private fun initView() {
        // initiate toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // pemanggilan class MainViewModel
        userViewModel = ViewModelProvider(this, UserViewModelFactory(this.application)).get(UserViewModel::class.java)
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

    private fun prepareLocalDB() {
        val database = AppDatabase.getAppDatabase(applicationContext)
        database?.let {
            daoUser = it.getUserDao()
        }
    }

    private fun showRecyclerList() {
        layout_nodata.visibility = View.INVISIBLE
        recyclerview_userlist.setHasFixedSize(true) // optimasi ukuran lebar dan tinggi secara otomatis
        recyclerview_userlist.layoutManager = GridLayoutManager(this, 2)
        val gridUserAdapter = UserAdapter()

        // cara mendapatkan value dari LiveData yang ada pada kelas ViewModel
        userViewModel.getUserList().observe(this, Observer { user ->
            user?.let {
                layout_nodata.isVisible = user.isEmpty()
                progressbar.visibility = View.GONE
                gridUserAdapter.setData(user,this)
            }
        })

        searchUser("A")
        recyclerview_userlist.adapter = gridUserAdapter

        gridUserAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemCLicked(data: User) {
                showBottomSheetUserDetail(data)
            }
        })
    }

    private fun searchUser(username: String) {
        progressbar.visibility = View.VISIBLE
        userViewModel.searchUsername(username)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_favorite_user -> startActivity(Intent(this, UserFavoriteActivity::class.java))
            else -> showBottomSheetNotificationSetting()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showBottomSheetUserDetail(user: User) {
        val view = layoutInflater.inflate(R.layout.bottomsheet_userdetail, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(view)
        dialog.show()
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
            view.btn_addtofavorite_bottomsheet.setOnClickListener {
                coroutineScope.launch {
                    val contentValues = ContentValues()
                    contentValues.put(MappingHelper.COLUMN_ID, userDetail.id)
                    contentValues.put(MappingHelper.USERNAME, userDetail.username)
                    contentValues.put(MappingHelper.AVATAR_URL, userDetail.avatar_url)
                    contentValues.put(MappingHelper.PUBLIC_REPOS, userDetail.public_repos)
                    contentValues.put(MappingHelper.NAME, userDetail.name)
                    contentResolver.insert(UserProvider.CONTENT_URI, contentValues)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, resources.getString(R.string.addtofav_success_message), Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                }
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun showBottomSheetNotificationSetting() {
        val view = layoutInflater.inflate(R.layout.bottomsheet_notificationsetting, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(view)
        view.switch_notification.setOnClickListener {
            isReminder = view.switch_notification.isChecked
            if (isReminder)
                reminderReceiver.setReminder(applicationContext, "09:00", resources.getString(R.string.notification_message))
            else
                reminderReceiver.cancelReminder(applicationContext)

            Toast.makeText(this, if (isReminder) resources.getString(R.string.notification_enabled) else resources.getString(R.string.notification_disabled), Toast.LENGTH_SHORT).show()
        }

        view.language_setting.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        (view.parent as View).setBackgroundColor(resources.getColor(android.R.color.transparent))
        dialog.show()
    }
}