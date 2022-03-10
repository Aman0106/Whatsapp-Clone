package com.example.whatsappclone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.whatsappclone.R
import com.example.whatsappclone.utils.LoadingDialogue
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LogInActivity : AppCompatActivity() {

    private lateinit var mFirebaseAuth: FirebaseAuth

    private var phoneNumber:String? = null
    private var verificationId:String? = null

    private lateinit var progressBar: ProgressBar
    private val loading = LoadingDialogue(this, "Sending Otp...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        supportActionBar?.hide()

        mFirebaseAuth = FirebaseAuth.getInstance()

        if(mFirebaseAuth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val btnContinue: Button = findViewById(R.id.btn_continue)
        val edtContact: EditText = findViewById(R.id.edt_name)
        edtContact.requestFocus()

        progressBar = ProgressBar(this)
        progressBar.visibility

        btnContinue.setOnClickListener {
            phoneNumber = "+91"+edtContact.text.toString()
            loading.startLoading()
            verifyPhoneNumber()
        }
    }

    private fun verifyPhoneNumber(){
        val options = PhoneAuthOptions.newBuilder(mFirebaseAuth)
            .setPhoneNumber(phoneNumber!!)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(this@LogInActivity, "Unable to Authenticate", Toast.LENGTH_SHORT).show()
                    loading.stopLoading()
                }

                override fun onCodeSent(verifyId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verifyId, token)
                    verificationId = verifyId
                    Toast.makeText(this@LogInActivity, "Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LogInActivity, OtpActivity::class.java)
                    intent.putExtra("contact", phoneNumber)
                    intent.putExtra("verifyId", verifyId)
                    loading.stopLoading()
                    startActivity(intent)
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}