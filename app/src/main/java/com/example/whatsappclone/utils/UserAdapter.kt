package com.example.whatsappclone.utils

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsappclone.activities.ChatActivity
import com.example.whatsappclone.R
import com.example.whatsappclone.classes.User

class UserAdapter(private val context: Context, private val userList: ArrayList<User>): RecyclerView.Adapter<UserAdapter.UsersViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.row_conversation,parent,false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val curUser = userList[position]

        holder.txtName.text = curUser.name

        Glide.with(context).load(curUser.profileImage).placeholder(R.drawable.blank_image).into(holder.profileImg)

        holder.itemView.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", curUser.name)
            intent.putExtra("uid", curUser.uid)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = userList.size

    class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txt_name)
        val profileImg: ImageView = itemView.findViewById(R.id.img_user)
    }
}