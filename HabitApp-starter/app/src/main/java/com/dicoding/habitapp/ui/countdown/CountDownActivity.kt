package com.dicoding.habitapp.ui.countdown

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat.getParcelableExtra
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.notification.NotificationWorker
import com.dicoding.habitapp.utils.HABIT
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.HABIT_TITLE

class CountDownActivity : AppCompatActivity() {

    private lateinit var workManager: WorkManager
    private lateinit var req: OneTimeWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
        supportActionBar?.title = "Count Down"

        val habit = getParcelableExtra(intent, HABIT, Habit::class.java)

        if (habit != null){
            findViewById<TextView>(R.id.tv_count_down_title).text = habit.title

            val viewModel = ViewModelProvider(this).get(CountDownViewModel::class.java)

            //TODO 10 : Set initial time and observe current time. Update button state when countdown is finished
            viewModel.setInitialTime(habit.minutesFocus)

            //TODO 13 : Start and cancel One Time Request WorkManager to notify when time is up.
            viewModel.eventCountDownFinish.observe(this) {
                workManager = WorkManager.getInstance(this)
                if(it) {
                    val data = Data.Builder()
                        .putInt(HABIT_ID, habit.id)
                        .putString(HABIT_TITLE, habit.title)
                        .build()
                    req = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                        .setInputData(data)
                        .build()
                    workManager.enqueue(req)
                    updateButtonState(false)
                    Toast.makeText(this, R.string.notify_content, Toast.LENGTH_SHORT).show()
                }
            }

            viewModel.currentTimeString.observe(this) {
                findViewById<TextView>(R.id.tv_count_down).text = it
            }

            findViewById<Button>(R.id.btn_start).setOnClickListener {
                viewModel.startTimer()
                updateButtonState(true)
            }

            findViewById<Button>(R.id.btn_stop).setOnClickListener {
                viewModel.stopTimer()
                updateButtonState(false)
            }
        }

    }

    private fun updateButtonState(isRunning: Boolean) {
        findViewById<Button>(R.id.btn_start).isEnabled = !isRunning
        findViewById<Button>(R.id.btn_stop).isEnabled = isRunning
    }
}