package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.viewmodle.MainViewModel


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var tvQuestionNumber: TextView
    private lateinit var tvQuestionText: TextView
    private lateinit var btnAnswer1: Button
    private lateinit var btnAnswer2: Button
    private lateinit var btnAnswer3: Button
    private lateinit var btnAnswer4: Button
    private lateinit var btnForward: Button
    private lateinit var btnBehind: Button

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.btnAnswer1 -> processAnswer(1)
                R.id.btnAnswer2 -> processAnswer(2)
                R.id.btnAnswer3 -> processAnswer(3)
                R.id.btnAnswer4 -> processAnswer(4)
            }
        }
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private fun localbtn(){
        btnAnswer1.isEnabled = false
        btnAnswer2.isEnabled = false
        btnAnswer3.isEnabled = false
        btnAnswer4.isEnabled = false
    }
    private fun unlocalbtn(){
        btnAnswer1.isEnabled = true
        btnAnswer2.isEnabled = true
        btnAnswer3.isEnabled = true
        btnAnswer4.isEnabled = true
    }
    private fun updateUi() = with(viewModel) {
        tvQuestionNumber.text = getString(R.string.question_number_ui, getCurrentQuestionNumber() + 1, getQuestionSize())
        tvQuestionText.text = getQuestion().textQuestion
        btnAnswer1.text = getQuestion().answer1
        btnAnswer2.text = getQuestion().answer2
        btnAnswer3.text = getQuestion().answer3
        btnAnswer4.text = getQuestion().answer4

        if (viewModel.getAnsweredIndexQuestion() == true)
        {
            localbtn()
        } else {
            unlocalbtn()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun processAnswer(givenId: Int){
        viewModel.getCurrentQuestionNumber()
        viewModel.getCorrectQuestions()
        viewModel.setList()
        if (viewModel.checkAnswer(givenId))
            viewModel.getCorrectAnswerCount()
        if (!viewModel.toNextQuestion()){
            if (viewModel.checkAllAnswerCorrect()){
            tvQuestionText.text = getString(R.string.game_result,
                viewModel.getCorrectAnswerCount(),
                viewModel.getQuestionSize())
            val correctProc = (viewModel.getCorrectAnswerCount() * 100 / viewModel.getQuestionSize())
            val incorrectProc = 100 - correctProc
                if (correctProc > 50 && viewModel.getCorrectCheckQuestion() >= 15){

                    tvQuestionNumber.text = getString(R.string.Ocenka_2)

                } else if (correctProc > 75 && viewModel.getCorrectCheckQuestion() >= 15){

                    tvQuestionNumber.text = getString(R.string.Ocenka_3)

                } else if (correctProc > 85 && viewModel.getCorrectCheckQuestion() >= 15){

                    tvQuestionNumber.text = getString(R.string.Ocenka_4)

                } else tvQuestionNumber.text = getString(R.string.Ocenka_5)

            if (correctProc > 75 &&  viewModel.getCorrectCheckQuestion() >= 15)
            {
                tvQuestionNumber.text = getString(R.string.Ocenka_4)
                tvQuestionNumber.text = getString(R.string.Ocenka_5)
                val resultMes = getString(R.string.game_result, correctProc, incorrectProc)
                tvQuestionText.text = resultMes
                val intent = Intent(this, ActivityResult::class.java)
                startActivity(intent)
            }
                val intent = Intent(this@MainActivity, ActivityResult::class.java)
                startActivity(intent)
                finish()
            } else {
                askForRestart()
            }
        }
        updateUi()
    }

    private fun checkButton() {
        if (viewModel.getCurrentQuestionNumber() > 0) {
                viewModel.getCurrentQuestionNumberBack()
                updateUi()
        }
    }
        private fun checkButtons() {
            if (viewModel.getCurrentQuestionNumber() < viewModel.getQuestionSize() - 1) {
                    viewModel.getCurrentQuestionNumberForward()
                    updateUi()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber)
        tvQuestionText = findViewById(R.id.tvQuestionText)
        btnAnswer1 = findViewById(R.id.btnAnswer1)
        btnAnswer2 = findViewById(R.id.btnAnswer2)
        btnAnswer3 = findViewById(R.id.btnAnswer3)
        btnAnswer4 = findViewById(R.id.btnAnswer4)
        btnForward = findViewById(R.id.btnForward)
        btnBehind = findViewById(R.id.btnBehind)
        btnForward.setOnClickListener(this)
        btnBehind.setOnClickListener(this)
        btnAnswer1.setOnClickListener(this)
        btnAnswer2.setOnClickListener(this)
        btnAnswer3.setOnClickListener(this)
        btnAnswer4.setOnClickListener(this)
        btnBehind.setOnClickListener{ checkButton()}
        btnForward.setOnClickListener{checkButtons()}
        updateUi()
    }
    private fun askForRestart() = AlertDialog.Builder(this).run {
            setTitle((R.string.title_dialog))
            val messageText = getString(R.string.game_result,
                viewModel.getCorrectAnswerCount(),
                viewModel.getQuestionSize())
            setMessage(messageText)
            setNegativeButton(android.R.string.cancel){_,_ ->
                finish()
            }
            setPositiveButton(android.R.string.ok){_,_ ->
                viewModel.reset()
                updateUi()
                for (i in 0 until viewModel.getQuestionSize()){
                    viewModel.getAnsweredList()[i] = false
                }
            }
            setCancelable(false)
            create()
        }.show()

    override fun onResume() {
        super.onResume()
        updateUi()
    }
}
