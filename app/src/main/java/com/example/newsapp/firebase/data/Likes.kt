package com.example.newsapp.firebase.data

class Likes {

    var like: String?=null
    var likeSenderId: String?=null

    constructor(){}
    constructor(like: String?, likeSenderId: String?) {
        this.like = like
        this.likeSenderId = likeSenderId
    }


}