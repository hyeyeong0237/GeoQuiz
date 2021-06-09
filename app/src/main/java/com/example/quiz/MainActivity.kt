package com.example.quiz

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider



private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val KEY_COUNT = "Count"


class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var submitButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var quizProgress : TextView
    private lateinit var quizBar : ProgressBar

    private val quizViewModel :QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?:0
        quizViewModel.currentIndex =currentIndex
        val answerCounts = savedInstanceState?.getInt(KEY_COUNT, 0) ?:0
        quizViewModel.answeredCount = answerCounts

        trueButton = findViewById(R.id.true_button)
        falseButton =findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        previousButton = findViewById(R.id.previous_button)
        submitButton = findViewById(R.id.submit_button)
        questionTextView = findViewById(R.id.question_text_view)
        quizProgress = findViewById(R.id.quiz_progress)
        quizBar = findViewById(R.id.progress_bar)

        trueButton.setOnClickListener{ view : View ->
            trueButtonPressed()
            quizViewModel.currentAnswer = true


        }
        falseButton.setOnClickListener{ view : View ->
            falseButtonPressed()
            quizViewModel.currentAnswer = false

        }
        nextButton.setOnClickListener {
            if(quizViewModel.currentIndex== quizViewModel.questionBank.size-1){
                if(quizViewModel.isNull()) {
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
                quizViewModel.moveToNext()
                isAnswered(quizViewModel.currentIndex)
                updateQuestion()
            }

        }
        previousButton.setOnClickListener {
            if(quizViewModel.currentIndex == 0){
                Toast.makeText(this, "뒤로 갈 수 없습니다", Toast.LENGTH_SHORT).show()
            }else{
                quizViewModel.moveToPrevious()
                isAnswered(quizViewModel.currentIndex)
                updateQuestion()
            }

        }
        submitButton.setOnClickListener {
            if(!(quizViewModel.currentQuestionAnswered)){
                checkAnswer(quizViewModel.currentAnswer)
                quizViewModel.answeredCount++
                quizViewModel.questionAnswered()
                quizViewModel.userAnswer[quizViewModel.currentIndex] = quizViewModel.currentAnswer
            }
            isAnswered(quizViewModel.currentIndex)
            updateQuestion()
        }

        isAnswered(quizViewModel.currentIndex)
        updateQuestion()
    }

    private fun showAnswer(){
        trueButton.visibility = View.GONE
        falseButton.visibility = View.GONE
        submitButton.visibility = View.GONE
        quizViewModel.checkScore()
        val answer = "총 점수는 ${quizViewModel.score}/60 입니다"
        questionTextView.setText(answer)
    }


    private  fun isAnswered(index: Int){
        val isQuestionAnswered = quizViewModel.currentQuestionAnswered
        if(!(isQuestionAnswered)){
            trueButton.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
            falseButton.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
        }else{
            if(quizViewModel.userAnswer[index]){
                trueButtonPressed()
            }else{
                falseButtonPressed()
            }
        }
        trueButton.isEnabled = !isQuestionAnswered
        falseButton.isEnabled = !isQuestionAnswered

    }

    private fun trueButtonPressed(){
        trueButton.setBackgroundColor(Color.BLUE)
        falseButton.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
    }

    private fun falseButtonPressed(){
        falseButton.setBackgroundColor(Color.BLUE)
        trueButton.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
    }


    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        quizProgress.setText("${quizViewModel.answeredCount}/6")
        quizBar.setProgress(quizViewModel.answeredCount)


    }

    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = quizViewModel.currentQuestionAnswer
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

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.d(TAG, "onSavedInstaceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        savedInstanceState.putInt(KEY_COUNT, quizViewModel.answeredCount)
    }
}