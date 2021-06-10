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
    var isCheater = false
    var cheatCount = 3
    var score = 0
    var answeredCount = 0
    var currentAnswer : Boolean? = null
    var userAnswer  = arrayOfNulls<Boolean>(questionBank.size)



    val currentQuestionAnswer: Boolean
        get()= questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val currentQuestionAnswered
        get() = questionBank[currentIndex].answered


    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrevious(){
        currentIndex = (currentIndex + 5) % questionBank.size
    }

    fun questionAnswered(){
        questionBank[currentIndex].answered = true
    }

    fun checkScore(){
        for(i in 0 until questionBank.size){
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