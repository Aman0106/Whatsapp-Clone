package com.example.whatsappclone.classes

class Message {
    var message:String? = null
    var senderUid:String? = null
    var messageId:String? = null
    var feeling:Int = -1
    var timeStamp:String? = null

    constructor(){ }

    constructor(message: String?, sendUid: String?, timeStamp: String?) {
        this.message = message
        this.senderUid = sendUid
        this.timeStamp = timeStamp
    }


}