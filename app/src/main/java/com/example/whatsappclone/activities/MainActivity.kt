package com.example.whatsappclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.R
import com.example.whatsappclone.classes.User
import com.example.whatsappclone.utils.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var users: ArrayList<User>
    private lateinit var userAdapter: UserAdapter
    private lateinit var userRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance().reference
        val db = FirebaseDatabase.getInstance().reference
        users = ArrayList()
        userAdapter = UserAdapter(this,users)
        userRecyclerView = findViewById(R.id.users_view)

        userRecyclerView.adapter = userAdapter

        mDatabaseReference.child("users").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()
                for (postSnapshot in snapshot.children){
                    val user = postSnapshot.getValue(User::class.java)
                    if(user?.uid != FirebaseAuth.getInstance().currentUser?.uid)
                        users.add(user!!)
                }

                //TODO(Optimise below Implementation)
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })



    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_search -> {
                Toast.makeText(this, "Searching", Toast.LENGTH_SHORT).show()
            }
            R.id.item_groups -> {
                Toast.makeText(this, "Grouping", Toast.LENGTH_SHORT).show()
            }
            R.id.item_settings -> {
                Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}