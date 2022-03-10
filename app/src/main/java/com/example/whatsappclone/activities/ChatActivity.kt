package com.example.whatsappclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.R
import com.example.whatsappclone.classes.Message
import com.example.whatsappclone.utils.MessagesAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private lateinit var receiverName:String
    private lateinit var receiverUid:String
    private lateinit var senderUid:String

    private lateinit var messageList:ArrayList<Message>
    private lateinit var messageAdapter:MessagesAdapter

    private var senderRoom:String? = null
    private var receiverRoom:String? = null

    private lateinit var messageBox:EditText
    private lateinit var sendBtn:ImageView
    private lateinit var cameraBtn:ImageView
    private lateinit var attachmentsBtn:ImageView
    private lateinit var recyclerView: RecyclerView

    private lateinit var mDbReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_acitivity)
        setInteractions()

        receiverName = intent.getStringExtra("name").toString()
        receiverUid = intent.getStringExtra("uid").toString()
        senderUid = FirebaseAuth.getInstance().currentUser!!.uid
        mDbReference = FirebaseDatabase.getInstance().reference

        messageList = ArrayList()
        messageAdapter = MessagesAdapter(this, messageList)
        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = messageAdapter


        sendBtn.setOnClickListener{
            val msgText = messageBox.text.toString()
            val date = Date()
            val message = Message(msgText,senderUid,date.time.toString())

            mDbReference.child("chats").child(senderRoom!!).child("messages").push().setValue(message).addOnSuccessListener {

                val msgText = messageBox.text.toString()
                val date = Date()
                val message = Message(msgText,senderUid,date.time.toString())

                mDbReference.child("chats").child(receiverRoom!!).child("messages").push().setValue(message)

                messageBox.setText("")
            }
        }

        mDbReference.child("chats").child(senderRoom!!).child("messages").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (postSnapshot in snapshot.children){
                    val message = postSnapshot.getValue(Message::class.java)
                    messageList.add(message!!)
                }
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) { }
        })

        supportActionBar?.title = receiverName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setInteractions(){
        messageBox = findViewById(R.id.edt_message_box)
        sendBtn = findViewById(R.id.btn_send)
        cameraBtn = findViewById(R.id.btn_camera)
        attachmentsBtn = findViewById(R.id.btn_attachments)
        recyclerView = findViewById(R.id.recyclerview)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}