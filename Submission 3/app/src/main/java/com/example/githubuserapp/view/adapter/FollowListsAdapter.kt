package com.example.githubuserapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.model.User

class FollowListsAdapter : RecyclerView.Adapter<FollowListsAdapter.GridViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    private val userList = ArrayList<User>()
    fun setData(users: ArrayList<User>) {
        userList.clear()
        userList.addAll(users)
        notifyDataSetChanged()
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) { this.onItemClickCallback = onItemClickCallback }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder =
        GridViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_following, parent, false))

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        Glide.with(holder.itemView.context) // memasukkan context.
            .load(userList[position].avatar_url) // memasukkan sumber gambar, contohnya menggunakan url atau asset dari Drawable.
            .fitCenter()
            .centerCrop()
            .into(holder.imgAvatar)
        holder.txtName.text = userList[position].username
        holder.txtUrl.text = userList[position].html_url

        holder.itemView.setOnClickListener { onItemClickCallback.onItemCLicked(userList[holder.adapterPosition]) }
    }

    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar: ImageView = itemView.findViewById(R.id.img_avatar_followingitem)
        var txtName: TextView = itemView.findViewById(R.id.txt_username_followingitem)
        var txtUrl: TextView = itemView.findViewById(R.id.txt_url_followingitem)
    }

    interface OnItemClickCallback { fun onItemCLicked(data: User) }
}