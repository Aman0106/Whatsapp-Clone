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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserAdapter(private val context: Context, private val userList: ArrayList<User>): RecyclerView.Adapter<UserAdapter.UsersViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.row_conversation,parent,false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val curUser = userList[position]

        val senderId = FirebaseAuth.getInstance().currentUser?.uid
        val senderRoom = senderId + curUser.uid
        FirebaseDatabase.getInstance().reference
            .child("chats")
            .child(senderRoom)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val lastMsg = snapshot.child("lastMsg").value.toString()
                        val lastMsgTime = snapshot.child("lastMsgTime").value.toString()
                        holder.txtLastMsg.text = lastMsg
                        holder.txtLastMsgTime.text = convertLongToTime(lastMsgTime.toLong())
                    }else{
                        holder.txtLastMsg.text = "Tap to chat"
                        holder.txtLastMsgTime.text = ""
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

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
        val txtLastMsg: TextView = itemView.findViewById(R.id.txt_last_message)
        val txtLastMsgTime: TextView = itemView.findViewById(R.id.txt_last_message_time)
    }


    fun convertLongToTime(time: Long):String{
        val format = SimpleDateFormat("h:mm a")
        return format.format(time)
    }
}