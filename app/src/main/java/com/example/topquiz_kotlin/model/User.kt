package com.example.topquiz_kotlin.model

/*
    Created by Edghi Makol on 23/03/21 at 00:10
*/class User {
    private lateinit var mFirstName : String

    fun getFirstName() = mFirstName

    fun setFirstName(firstname : String){
        mFirstName = firstname
    }
}