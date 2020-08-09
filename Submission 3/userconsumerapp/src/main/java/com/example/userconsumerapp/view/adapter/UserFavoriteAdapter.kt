package com.example.userconsumerapp.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.userconsumerapp.R
import com.example.userconsumerapp.model.UserFavorite
import com.example.userconsumerapp.view.activity.UserDetailActivity

class UserFavoriteAdapter() : RecyclerView.Adapter<UserFavoriteAdapter.GridViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    private val userList = ArrayList<UserFavorite>()
    private lateinit var activity: Activity

    var listUserFavorites = ArrayList<UserFavorite>()
        set(listUser) {
            if (listUserFavorites.size > 0)
                this.listUserFavorites.clear()

            this.listUserFavorites.addAll(listUser)
            notifyDataSetChanged()
        }

    fun setData(users: ArrayList<UserFavorite>, activity: Activity) {
        userList.clear()
        userList.addAll(users)
        this.activity = activity
        notifyDataSetChanged()
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) { this.onItemClickCallback = onItemClickCallback }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder =
        GridViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_userfavorite, parent, false))

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        Glide.with(holder.itemView.context) // memasukkan context.
            .load(userList[position].avatar_url) // memasukkan sumber gambar, contohnya menggunakan url atau asset dari Drawable.
            .fitCenter()
            .centerCrop()
            .into(holder.imgAvatar)
        holder.txtName.text = userList[position].name
        holder.txtBio.text = activity.resources.getString(R.string.repository_bottomsheet , userList[position].public_repos)

        holder.showProfile.setOnClickListener {
            val intent = Intent(holder.itemView.context, UserDetailActivity::class.java)
            intent.putExtra(UserDetailActivity.from, UserDetailActivity.favoriteUser)
            intent.putExtra(UserDetailActivity.userInfo, userList[holder.adapterPosition])
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar: ImageView = itemView.findViewById(R.id.img_avatar_userfavorite)
        var txtName: TextView = itemView.findViewById(R.id.txt_name_userfavorite)
        var txtBio: TextView = itemView.findViewById(R.id.txt_repository_userfavorite)
        var showProfile: Button = itemView.findViewById(R.id.btn_showprofile_userfavorite_itemcard)
    }

    interface OnItemClickCallback { fun onItemCLicked(userFav: UserFavorite) }
}