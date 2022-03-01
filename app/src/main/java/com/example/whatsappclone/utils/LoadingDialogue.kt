package com.example.whatsappclone.utils

import android.app.Activity
import android.app.AlertDialog
import com.example.whatsappclone.R

class LoadingDialogue(private val mActivity:Activity) {
    private lateinit var alertDialog: AlertDialog

    fun startLoading(){
        val inflater = mActivity.layoutInflater
        val dialogueView = inflater.inflate(R.layout.loading_dialogue, null)
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogueView)
        builder.setCancelable(false)

        alertDialog = builder.create()
        alertDialog.show()
    }

    fun isDismiss(){
        alertDialog.dismiss()
    }
}