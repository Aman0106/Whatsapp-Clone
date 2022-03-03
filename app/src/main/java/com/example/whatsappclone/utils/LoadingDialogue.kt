package com.example.whatsappclone.utils

import android.app.Activity
import android.app.AlertDialog
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.example.whatsappclone.R

class LoadingDialogue(private val mActivity:Activity, private val msg:String) {

    private lateinit var alertDialog: AlertDialog


    fun startLoading(){
        val inflater = mActivity.layoutInflater
        val dialogueView = inflater.inflate(R.layout.loading_dialogue, null)
        val builder = AlertDialog.Builder(mActivity)

        val txtMsg: TextView = dialogueView.findViewById(R.id.txt_loading_dialogue)
        txtMsg.text = msg

        builder.setView(dialogueView)
        builder.setCancelable(false)

        alertDialog = builder.create()
        alertDialog.show()
    }

    fun stopLoading(){
        alertDialog.dismiss()
    }
}