package com.example.quiz

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var submitButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var quizProgress : TextView
    private lateinit var quizBar : ProgressBar

    private var questionBank = listOf(
        Question(R.string.question_australia, true) ,
        Question(R.string.question_ocean, true) ,
        Question(R.string.question_mideast, false) ,
        Question(R.string.question_africa, false) ,
        Question(R.string.question_america, true) ,
        Question(R.string.question_aisa, true)
    )
    var score = 0;
    var answeredCount = 0;
    private var currentIndex = 0
    private var currentAnswer = true
    var userAnswer = arrayOfNulls<Boolean>(questionBank.size)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton =findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        previousButton = findViewById(R.id.previous_button)
        submitButton = findViewById(R.id.submit_button)
        questionTextView = findViewById(R.id.question_text_view)
        quizProgress = findViewById(R.id.quiz_progress)
        quizBar = findViewById(R.id.progress_bar)

        trueButton.setOnClickListener{ view : View ->
            trueButton.setBackgroundColor(Color.BLUE)
            falseButton.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
            currentAnswer = true;


        }
        falseButton.setOnClickListener{ view : View ->
            falseButton.setBackgroundColor(Color.BLUE)
            trueButton.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
            currentAnswer = false;

        }
        nextButton.setOnClickListener {
            if(currentIndex == questionBank.size-1){
                if(isNull()) {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("아직 풀지 않은 문제가 있습니다")
                    builder.setMessage("그래도 점수를 확인 하시겠습니까?")
                    builder.setPositiveButton("네") { dialog, which ->
                        showAnswer()
                    }
                    builder.setNegativeButton("아니오") { dialog, which ->
                        Toast.makeText(this, "다시 문제를 확인해주세요", Toast.LENGTH_SHORT).show()
                    }
                    val dialog = builder.create()
                    dialog.show()
                }else {
                    showAnswer()
                }


            }else{
                currentIndex = (currentIndex + 1)
                isAnswered(currentIndex)
                updateQuestion();
            }

        }
        previousButton.setOnClickListener {
            if(currentIndex == 0){
                Toast.makeText(this, "뒤로 갈 수 없습니다", Toast.LENGTH_SHORT).show()
            }else{
                currentIndex = (currentIndex  - 1)
                isAnswered(currentIndex)
                updateQuestion();
            }

        }
        submitButton.setOnClickListener {
            if(questionBank[currentIndex].answered == false){
                checkAnswer(currentAnswer);
                answeredCount++
                questionBank[currentIndex].answered = true
                userAnswer[currentIndex] = currentAnswer;
            }
            isAnswered(currentIndex)
            updateQuestion();
        }


        updateQuestion();
    }

    private fun showAnswer(){
        trueButton.visibility = View.GONE
        falseButton.visibility = View.GONE
        submitButton.visibility = View.GONE
        checkScore()
        val answer = "총 점수는 ${score}/60 입니다"
        questionTextView.setText(answer)
    }
    private  fun isNull(): Boolean{
        for(b in userAnswer) {
            if (b == null) {
                return true
            }
        }
        return false
    }

    private  fun isAnswered(index: Int){
        val isQuestionAnswered = questionBank[index].answered
        if(isQuestionAnswered == false){
            trueButton.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
            falseButton.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
        }else{
            if(userAnswer[index] == true){
                trueButton.setBackgroundColor(Color.BLUE)
                falseButton.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
            }else{
                falseButton.setBackgroundColor(Color.BLUE)
                trueButton.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
            }
        }
        trueButton.isEnabled = !isQuestionAnswered
        falseButton.isEnabled = !isQuestionAnswered

    }


    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
        quizProgress.setText("${answeredCount}/6")
        quizBar.setProgress(answeredCount)


    }

    private fun checkScore(){
        for(i in 0..5){
            if(userAnswer[i] == questionBank[i].answer){
                score += 10
            }
        }
    }
    private fun checkAnswer(userAnswer: Boolean){
        var correctAnswer = questionBank[currentIndex].answer
        val messageResId = if(correctAnswer == userAnswer){
            R.string.correct_toast
        }else{
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
}