package com.example.newsapp.firebase.data

class Message {
    //fetch the elements of the message
    //The sender of the message is  identified by the unique user id


    var message: String?=null
    var senderId: String?=null

    constructor(){}

    constructor(message: String?, senderId: String?) {
        this.message = message
        this.senderId = senderId
    }


}