package com.example.newsapp.firebase

class User {

    var name:String?=null
    var email:String?=null
    var uid:String?=null

//its necessary to have a default constructor with no args
    constructor(){}

    constructor(name: String?, email: String?, uid: String?) {
        this.name = name
        this.email = email
        this.uid = uid
    }


}