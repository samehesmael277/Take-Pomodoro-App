package com.sameh.countdowntimerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    val TAG = "TAGMainActivity"

    private lateinit var timer: TextView
    private lateinit var str_btn: Button
    private lateinit var reset: TextView
    private lateinit var textView: TextView
    private lateinit var progressBar: ProgressBar

    private val timer_counst: Long = 25 * 60 * 1000
    private var remining_time: Long = timer_counst


    private var myTimer: CountDownTimer? = null

    private var isTimerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initilization()

        str_btn.setOnClickListener {
            if(!isTimerRunning) {
                creatTimer(remining_time)
                textView.text = resources.getText(R.string.keep)
            }
        }

        reset.setOnClickListener {
            resetFun()
        }

    }

    private fun resetFun() {
        myTimer?.cancel()
        remining_time = timer_counst
        reformat()
        textView.text = resources.getText(R.string.pomodoro)
        isTimerRunning = false
        progressBar.progress = 100
    }

    private fun creatTimer(StartTimer: Long) {
        myTimer = object : CountDownTimer(StartTimer, 1 * 1000) {
            override fun onTick(timeLeft: Long) {
                remining_time = timeLeft
                reformat()
                progressBar.progress = remining_time.toDouble().div(timer_counst.toDouble()).times(100).toInt()
            }
            override fun onFinish() {
                Toast.makeText(this@MainActivity, "Finished !", Toast.LENGTH_SHORT).show()
                isTimerRunning = false
                textView.text = resources.getText(R.string.pomodoro)
                progressBar.progress = 100
                remining_time = timer_counst
                reformat()
            }
        }.start()
        isTimerRunning = true
    }

    private fun reformat() {
        val min = remining_time.div(1000).div(60)
        val sec = remining_time.div(1000) % 60
        val final_format = String.format("%02d:%02d", min, sec)
        timer.text = final_format
    }

    private fun initilization() {
        timer = findViewById(R.id.timerView)
        str_btn = findViewById(R.id.startButton)
        reset = findViewById(R.id.resetButton)
        textView = findViewById(R.id.textView)
        progressBar = findViewById(R.id.progressBar)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("RemaningTime", remining_time)
        outState.putString("take or keep", textView.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val StartTimerInLandmode = savedInstanceState.getLong("RemaningTime")
        val takeORkeep = savedInstanceState.getString("take or keep")
        if (StartTimerInLandmode != timer_counst)
            creatTimer(StartTimerInLandmode)
        textView.setText(takeORkeep)
    }

}