package com.example.topquiz_kotlin.model

import java.util.*

/*
    Created by Edghi Makol on 23/03/21 at 00:25
*/class QuestionBank(private var mQuestionList : List<Question>) {
    private var mNextQuestionIndex = 0
    init {
        Collections.shuffle(mQuestionList)
    }

    fun getQuestion() : Question {
        if(mNextQuestionIndex == mQuestionList.size) mNextQuestionIndex = 0
        return mQuestionList.get(mNextQuestionIndex++)
    }
}