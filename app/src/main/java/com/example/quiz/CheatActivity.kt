package com.example.quiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView

private const val EXTRA_ANSWER_IS_TRUE ="com.example.quiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.example.quiz.answer_shown"
const val KEY_ANSWER_SHOWN = "answer is shown"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView : TextView
    private lateinit var showAnswerButton : Button

    private var answerIsTrue = false
    private var isAnswerShown = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        isAnswerShown = savedInstanceState?.getBoolean(KEY_ANSWER_SHOWN, false) ?: false

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            isAnswerShown = true;
            answerTextView.setText(answerText)
            setAnswerShownResult(isAnswerShown)
        }
        setAnswerShownResult(isAnswerShown)

    }

    private fun setAnswerShownResult(isAnswerShown : Boolean){
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putBoolean(KEY_ANSWER_SHOWN, isAnswerShown)
    }

    companion object{
        fun newIntent(packageContext: Context, answerIsTrue : Boolean) : Intent {
            return Intent(packageContext,CheatActivity::class.java).apply { putExtra(
                EXTRA_ANSWER_IS_TRUE, answerIsTrue) }
        }
    }

}