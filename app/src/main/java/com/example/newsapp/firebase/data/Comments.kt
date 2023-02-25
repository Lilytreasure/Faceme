package com.example.newsapp.firebase.data

class Comments {


    var comment: String?=null
    var senderId: String?=null
    var newsId: String?=null

    constructor(){}

    constructor(comment: String?, senderId: String?, newsId: String?) {
        this.comment = comment
        this.senderId = senderId
        this.newsId = newsId
    }


}