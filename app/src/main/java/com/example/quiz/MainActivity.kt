package com.example.quiz

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
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
private const val REQUEST_CODE_CHEAT = 0


class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var submitButton: Button
    private lateinit var cheatButton:  Button
    private lateinit var scoreButton:  Button
    private lateinit var cheatBar :ProgressBar
    private lateinit var cheatProgress : TextView
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
        cheatButton = findViewById(R.id.cheat_button)
        scoreButton = findViewById(R.id.Score_button)
        quizProgress = findViewById(R.id.quiz_progress)
        quizBar = findViewById(R.id.progress_bar)
        cheatProgress = findViewById(R.id.cheat_progress)
        cheatBar = findViewById(R.id.cheatprogress_bar)

        trueButton.setOnClickListener{ view : View ->
            quizViewModel.currentAnswer = true
            trueButton.isSelected =true
            falseButton.isSelected = false
            trueButton.setBackgroundResource(R.drawable.button)
            falseButton.setBackgroundResource(R.drawable.button)


        }
        falseButton.setOnClickListener{ view : View ->
            quizViewModel.currentAnswer = false
            trueButton.isSelected = false
            falseButton.isSelected = true
            trueButton.setBackgroundResource(R.drawable.button)
            falseButton.setBackgroundResource(R.drawable.button)

        }
        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            buttonCheck()
            updateQuestion()

        }
        previousButton.setOnClickListener {

            quizViewModel.moveToPrevious()
            buttonCheck()
            updateQuestion()


        }
        submitButton.setOnClickListener {
            if(quizViewModel.currentQuestionAnswered) {
                buttonCheck()
                updateQuestion()
            }else {
                if(quizViewModel.currentAnswer == null) {
                    Toast.makeText(this, "정답을 선택해 주세요", Toast.LENGTH_SHORT).show()
                }else{
                    checkAnswer(quizViewModel.currentAnswer!!)
                    quizViewModel.answeredCount++
                    quizViewModel.questionAnswered()
                    quizViewModel.userAnswer[quizViewModel.currentIndex] = quizViewModel.currentAnswer
                    buttonCheck()
                    updateQuestion()
                }

            }

        }


        cheatButton.setOnClickListener {
            if(quizViewModel.cheatCount == 0){
                Toast.makeText(this, "더 이상 컨닝을 하실 수 없습니다", Toast.LENGTH_SHORT).show()
            }else{
                val answerIsTrue = quizViewModel.currentQuestionAnswer
                val intent = CheatActivity.newIntent(this, answerIsTrue)
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }

        }

        scoreButton.setOnClickListener {

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




        }


        buttonCheck()
        updateQuestion()
    }

    private fun showAnswer(){
        trueButton.visibility = View.GONE
        falseButton.visibility = View.GONE
        submitButton.visibility = View.GONE
        cheatButton.visibility = View.GONE
        previousButton.visibility =View.GONE
        nextButton.visibility = View.GONE
        quizViewModel.checkScore()
        val answer = "총 점수는 ${quizViewModel.score}/60 입니다"
        questionTextView.setText(answer)
    }


    private  fun buttonCheck(){
        val isQuestionAnswered = quizViewModel.currentQuestionAnswered
        trueButton.isEnabled = !isQuestionAnswered
        falseButton.isEnabled = !isQuestionAnswered
        if(isQuestionAnswered){
            if(quizViewModel.userAnswer[quizViewModel.currentIndex] == quizViewModel.currentQuestionAnswer){
                if(quizViewModel.currentQuestionAnswer == true){
                    trueButton.isSelected =true;
                    falseButton.isSelected = false;
                }else if(quizViewModel.currentQuestionAnswer == false){
                    trueButton.isSelected =false;
                    falseButton.isSelected = true;
                }
                trueButton.setBackgroundResource(R.drawable.correct_button)
                falseButton.setBackgroundResource(R.drawable.correct_button)
            }else{
                if(quizViewModel.userAnswer[quizViewModel.currentIndex] == true){
                    trueButton.isSelected =true;
                    falseButton.isSelected = false;
                }else if(quizViewModel.userAnswer[quizViewModel.currentIndex] == false){
                    trueButton.isSelected =false;
                    falseButton.isSelected = true;
                }
                trueButton.setBackgroundResource(R.drawable.incorrect_button)
                falseButton.setBackgroundResource(R.drawable.incorrect_button)
            }
        }else{
            trueButton.isSelected = false;
            falseButton.isSelected = false;
            trueButton.setBackgroundResource(R.drawable.button)
            falseButton.setBackgroundResource(R.drawable.button)
            quizViewModel.currentAnswer = null
        }

    }



    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        quizProgress.setText("${quizViewModel.answeredCount}/6")
        quizBar.setProgress(quizViewModel.answeredCount)
        cheatProgress.setText("${quizViewModel.cheatCount}/3")
        cheatBar.setProgress(quizViewModel.cheatCount)



    }

    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId : Int
        if (correctAnswer == userAnswer) {
            messageResId = R.string.correct_toast
        }else{

            messageResId = R.string.incorrect_toast
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK){

        }
        if(requestCode == REQUEST_CODE_CHEAT){
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            if(quizViewModel.isCheater){
                quizViewModel.cheatCount--
            }
        }
        buttonCheck()
        updateQuestion()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.d(TAG, "onSavedInstaceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        savedInstanceState.putInt(KEY_COUNT, quizViewModel.answeredCount)
    }
}