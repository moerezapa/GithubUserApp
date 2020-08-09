package com.example.githubuserapp.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.model.User
import com.example.githubuserapp.view.activity.UserDetailActivity

class UserAdapter() : RecyclerView.Adapter<UserAdapter.GridViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    private val userList = ArrayList<User>()
    private lateinit var activity: Activity
    fun setData(users: ArrayList<User>, activity: Activity) {
        userList.clear()
        userList.addAll(users)
        this.activity = activity
        notifyDataSetChanged()
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) { this.onItemClickCallback = onItemClickCallback }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder =
        GridViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        Glide.with(holder.itemView.context) // memasukkan context.
            .load(userList[position].avatar_url) // memasukkan sumber gambar, contohnya menggunakan url atau asset dari Drawable.
            .fitCenter()
            .centerCrop()
            .into(holder.imgAvatar)
        holder.txtName.text = userList[position].username
        holder.txtBio.text = userList[position].html_url

        holder.showProfile.setOnClickListener {
            val intent = Intent(holder.itemView.context, UserDetailActivity::class.java)
            intent.putExtra(UserDetailActivity.from, UserDetailActivity.detailUser)
            intent.putExtra(UserDetailActivity.userInfo, userList[holder.adapterPosition])
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        holder.showBottomSheet.setOnClickListener { onItemClickCallback.onItemCLicked(userList[holder.adapterPosition]) }
        holder.itemView.setOnClickListener { onItemClickCallback.onItemCLicked(userList[holder.adapterPosition]) }
    }

    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar: ImageView = itemView.findViewById(R.id.img_avatar_itemcard)
        var txtName: TextView = itemView.findViewById(R.id.txt_name_itemcard)
        var txtBio: TextView = itemView.findViewById(R.id.txt_url_itemcard)
        var showProfile: Button = itemView.findViewById(R.id.btn_showprofile_itemcard)
        var showBottomSheet: ImageButton = itemView.findViewById(R.id.btn_addtofavorite_itemcard)
    }

    interface OnItemClickCallback { fun onItemCLicked(data: User) }
}