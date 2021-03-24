package com.example.topquiz_kotlin.model

/*
    Created by Edghi Makol on 23/03/21 at 00:13
*/class Question(private var mQuestion : String, private var mChoiceList : List<String>, private var mAnswerIndex : Int) {

    fun getQuestion() = mQuestion

    fun getChoiceList() = mChoiceList

    fun getAnswerIndex() = mAnswerIndex

    fun setQuestion(question : String){
        mQuestion = question
    }

    fun setChoiceList(choiceList : List<String>){
        if(choiceList == null) throw IllegalArgumentException ("La liste ne peut etre nulle")
        mChoiceList = choiceList
    }

    fun setAnswerIndex(answerIndex : Int){
        if(answerIndex < 0 || answerIndex >= mChoiceList.size) throw IllegalArgumentException ("Index hors des bornes")
        mAnswerIndex = answerIndex
    }
}