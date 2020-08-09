package com.example.githubuserapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UserAdapter(var userList: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.GridViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        return GridViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        Glide.with(holder.itemView.context) // memasukkan context.
            .load(userList[position].avatar) // memasukkan sumber gambar, contohnya menggunakan url atau asset dari Drawable.
            .fitCenter()
            .centerCrop()
            .into(holder.imgAvatar)
        holder.txtName.text = userList[position].name
        holder.txtCompany.text = userList[position].company
        holder.txtLocation.text = userList[position].location

        holder.itemView.setOnClickListener { onItemClickCallback.onItemCLicked(userList[holder.adapterPosition]) }
    }

    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar: ImageView = itemView.findViewById(R.id.img_avatar)
        var txtName: TextView = itemView.findViewById(R.id.txt_name_itemcard)
        var txtCompany: TextView = itemView.findViewById(R.id.txt_companyname_itemcard)
        var txtLocation: TextView = itemView.findViewById(R.id.txt_location_itemcard)
    }

    interface OnItemClickCallback { fun onItemCLicked(data: User) }
}