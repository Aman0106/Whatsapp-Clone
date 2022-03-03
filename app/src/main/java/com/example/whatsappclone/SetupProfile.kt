package com.example.whatsappclone

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.whatsappclone.classes.User
import com.example.whatsappclone.utils.LoadingDialogue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class SetupProfile : AppCompatActivity() {

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mFirebaseStorage: FirebaseStorage
    private lateinit var mFirebaseDatabase: FirebaseDatabase

    private lateinit var imageProfile: ImageView
    private lateinit var edtName: EditText
    private lateinit var btnContinue: Button

    private var selectedImage: Uri? = null


    private lateinit var progressBar: ProgressBar
    private val loading = LoadingDialogue(this, "Setting things up...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile)

        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseStorage = FirebaseStorage.getInstance()

        imageProfile = findViewById(R.id.image_profile)
        edtName = findViewById(R.id.edt_name)
        btnContinue = findViewById(R.id.btn_continue)

        progressBar = ProgressBar(this)
        progressBar.visibility

        val selectImage = registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                imageProfile.setImageURI(it)

                selectedImage = it
                Toast.makeText(this, "Image was selected"+selectedImage.toString(), Toast.LENGTH_SHORT).show()

            })

        imageProfile.setOnClickListener{
            selectImage.launch("image/*")
        }
        btnContinue.setOnClickListener {
            val name = edtName.text.toString()
            val uid = mFirebaseAuth.uid
            if(name.isEmpty()){
                edtName.error = "Please enter a name"
                return@setOnClickListener
            }
            loading.startLoading()
            if(selectedImage!=null){
                val reference = mFirebaseStorage.reference.child("user").child(uid!!).child("profilepic")
                reference.putFile(selectedImage!!).addOnCompleteListener{
                    if(it.isSuccessful){
                        reference.downloadUrl.addOnSuccessListener { task ->
                            val imageUrl = task.toString()
                            val phone = mFirebaseAuth.currentUser?.phoneNumber

                            val user = User(uid,name,phone,imageUrl)

                            mFirebaseDatabase.reference.child("users").child(uid!!).setValue(user)
                                .addOnSuccessListener {
                                    loading.stopLoading()
                                    startActivity(Intent(this,MainActivity::class.java))
                                }
                        }
                    }
                }
            }else{
                val phone = mFirebaseAuth.currentUser?.phoneNumber

                val user = User(uid,name,phone,"")

                mFirebaseDatabase.reference.child("users").child(uid!!).setValue(user)
                    .addOnSuccessListener {
                        loading.stopLoading()
                        startActivity(Intent(this,MainActivity::class.java))
                    }
            }
        }

    }


//  For some reason this does not work and i had to write the below in onCreate
//  I will look into this in the future
    private fun registerUser(){

        val name = edtName.text.toString()
        if(name.isEmpty()){
            edtName.error = "Please enter a name"
            return
        }

        var imageUrl: String = ""

        val uid = mFirebaseAuth.uid
        val phone = mFirebaseAuth.currentUser?.phoneNumber

        loading.startLoading()
        if (selectedImage!=null){
            val reference = mFirebaseStorage.reference.child("Profiles").child(mFirebaseAuth.uid!!)
            reference.putFile(selectedImage!!).addOnCompleteListener{ task->
                Toast.makeText(this, "Image was selected", Toast.LENGTH_SHORT).show()
                if(task.isSuccessful) {
                    reference.downloadUrl.addOnSuccessListener {
                        imageUrl = it.toString()
                    }
                }else{
                    Toast.makeText(this, "Image error", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val user = User(uid,name,phone,imageUrl)

        mFirebaseDatabase.reference.child("users").child(uid!!).setValue(user).addOnSuccessListener {
            loading.stopLoading()
            startActivity(Intent(this,MainActivity::class.java))
        }
            .addOnFailureListener{
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
    }
}