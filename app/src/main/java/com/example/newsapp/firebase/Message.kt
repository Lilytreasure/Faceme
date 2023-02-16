package com.example.newsapp.firebase

class Message {
    //fetch the elements of the message

    var message: String?=null
    var senderId: String?=null

    constructor(){}

    constructor(message: String?, senderId: String?) {
        this.message = message
        this.senderId = senderId
    }


}