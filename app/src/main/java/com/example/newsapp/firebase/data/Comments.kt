package com.example.newsapp.firebase.data

class Comments {


    var comment: String?=null
    var senderId: String?=null

    constructor(){}


    constructor(comment: String?, senderId: String?) {
        this.comment = comment
        this.senderId = senderId
    }


}