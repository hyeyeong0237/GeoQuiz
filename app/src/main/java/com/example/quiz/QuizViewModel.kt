package com.example.quiz

import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel(){

   var questionBank = listOf(
        Question(R.string.question_australia, true) ,
        Question(R.string.question_ocean, true) ,
        Question(R.string.question_mideast, false) ,
        Question(R.string.question_africa, false) ,
        Question(R.string.question_america, true) ,
        Question(R.string.question_aisa, true)
    )
    var currentIndex = 0
    var score = 0
    var answeredCount = 0
    var currentAnswer = true
    var userAnswer : BooleanArray = booleanArrayOf(true,true,true,true,true,true)



    val currentQuestionAnswer: Boolean
        get()= questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val currentQuestionAnswered
        get() = questionBank[currentIndex].answered


    fun moveToNext(){
        currentIndex = (currentIndex + 1)
    }

    fun moveToPrevious(){
        currentIndex = (currentIndex - 1)
    }

    fun questionAnswered(){
        questionBank[currentIndex].answered = true
    }

    fun checkScore(){
        for(i in 0..5){
            if(userAnswer[i] == questionBank[i].answer){
                score += 10
            }
        }
    }
   fun isNull(): Boolean{
        for(b in userAnswer) {
            if (b == null) {
                return true
            }
        }
        return false
    }


}