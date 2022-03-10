package com.example.whatsappclone.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.R
import com.example.whatsappclone.classes.Message
import com.google.firebase.auth.FirebaseAuth

class MessagesAdapter(private val context:Context, private val messageList: ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val SENT = 1
    val RECEIVED = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == SENT){
            val view = LayoutInflater.from(context).inflate(R.layout.sent_message, parent, false)
            return SentViewHolder(view)
        }
        val view = LayoutInflater.from(context).inflate(R.layout.received_message, parent, false)
        return ReceiveViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val curMessage = messageList[position]

        if(holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder
            viewHolder.msg.text = curMessage.message

        }else{
            val viewHolder = holder as ReceiveViewHolder
            viewHolder.msg.text = curMessage.message
        }
    }

    override fun getItemCount(): Int = messageList.size

    override fun getItemViewType(position: Int): Int {
        val curMessage = messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(curMessage.senderUid))
            return SENT
        return RECEIVED
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val msg:TextView = itemView.findViewById(R.id.sent_message)
    }

    class ReceiveViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val msg:TextView = itemView.findViewById(R.id.received_message)
    }
}