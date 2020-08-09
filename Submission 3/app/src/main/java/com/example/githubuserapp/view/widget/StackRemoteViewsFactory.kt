package com.example.githubuserapp.view.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.localdb.AppDatabase
import com.example.githubuserapp.model.UserDao
import com.example.githubuserapp.model.UserFavorite
import com.example.githubuserapp.provider.MappingHelper
import com.example.githubuserapp.provider.UserProvider

internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

    private lateinit var userFavouriteList : List<UserFavorite>
    private lateinit var userDao: UserDao
    private var cursor : Cursor? = null

    override fun onCreate() {
        val appDatabase = AppDatabase.getAppDatabase(mContext)
        appDatabase?.let {
            userDao = it.getUserDao()
        }
    }

    override fun onDataSetChanged() {
        //Ini berfungsi untuk melakukan refresh saat terjadi perubahan.
        cursor?.close()

        val identityToken = Binder.clearCallingIdentity()

        // querying ke database
        cursor = mContext.contentResolver.query(UserProvider.CONTENT_URI, null, null, null, null)!!
        userFavouriteList = MappingHelper.mapCursorToUserArrayList(cursor)
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int =
        if (userFavouriteList.isEmpty()) 0
        else userFavouriteList.size

    override fun getViewAt(position: Int): RemoteViews? {
        /**
         * method ini memasang item yg berisi imageview
         * memasang gambar memanfaatkan remoteviews
         * kemudian item tsb ditampilin
         */
        val remoteView = RemoteViews(mContext.packageName, R.layout.widget_item)
        val bitmap: Bitmap = Glide.with(mContext)
                                .asBitmap()
                                .fitCenter()
                                .load(userFavouriteList[position].avatar_url)
                                .submit()
                                .get()
        remoteView.setImageViewBitmap(R.id.img_userfav_widget, bitmap)

        val extras = bundleOf(UserFavouriteWidget.EXTRA_ITEM to position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        remoteView.setOnClickFillInIntent(R.id.img_userfav_widget, fillInIntent)
        return remoteView
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}