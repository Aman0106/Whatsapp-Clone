package com.example.whatsappclone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.whatsappclone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider

class OtpActivity : AppCompatActivity() {

    private lateinit var txtInput1: TextView
    private lateinit var txtInput2: TextView
    private lateinit var txtInput3: TextView
    private lateinit var txtInput4: TextView
    private lateinit var txtInput5: TextView
    private lateinit var txtInput6: TextView

    private lateinit var btnVerify: Button

    private lateinit var firebaseAuth: FirebaseAuth

    private var phoneNumber:String? = null
    private var verificationId:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        supportActionBar?.hide()

        firebaseAuth = FirebaseAuth.getInstance()
        verificationId = intent.getStringExtra("verifyId")

        phoneNumber = intent.getStringExtra("contact")
        val txtContact:TextView = findViewById(R.id.txt_contact_display)

        txtInput1 = findViewById(R.id.edt_input1)
        txtInput2 = findViewById(R.id.edt_input2)
        txtInput3 = findViewById(R.id.edt_input3)
        txtInput4 = findViewById(R.id.edt_input4)
        txtInput5 = findViewById(R.id.edt_input5)
        txtInput6 = findViewById(R.id.edt_input6)

        btnVerify = findViewById(R.id.btn_verify)

        txtContact.text = "Verify: +91 $phoneNumber"

        setOtpInputs()
        txtInput1.requestFocus()

        btnVerify.setOnClickListener {

            verifyOtp()
        }
    }


    private fun verifyOtp(){
        val otp = getOtpFromUser()
        if(otp.length < 6){
            Toast.makeText(this, "Enter all digits", Toast.LENGTH_SHORT).show()
            return
        }

        val credential = PhoneAuthProvider.getCredential(verificationId!!,otp)

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this){
            if(it.isSuccessful){
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, SetupProfile::class.java))
                finishAffinity()
            }else{
                Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getOtpFromUser(): String {

        return txtInput1.text.toString() +
                txtInput2.text.toString() +
                txtInput3.text.toString() +
                txtInput4.text.toString() +
                txtInput5.text.toString() +
                txtInput6.text.toString()
    }

    private fun setOtpInputs(){

        txtInput1.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(s.toString().trim().isNotEmpty()){
                    txtInput2.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) { }

        })
        txtInput2.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(s.toString().trim().isNotEmpty()){
                    txtInput3.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) { }

        })
        txtInput3.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(s.toString().trim().isNotEmpty()){
                    txtInput4.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) { }

        })
        txtInput4.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(s.toString().trim().isNotEmpty()){
                    txtInput5.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) { }

        })
        txtInput5.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(s.toString().trim().isNotEmpty()){
                    txtInput6.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) { }

        })
        txtInput6.addTextChangedListener(object: TextWatcher{
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(p0: Editable?) {
                if(getOtpFromUser().length > 5)
                    verifyOtp()
            }
        })
    }

}