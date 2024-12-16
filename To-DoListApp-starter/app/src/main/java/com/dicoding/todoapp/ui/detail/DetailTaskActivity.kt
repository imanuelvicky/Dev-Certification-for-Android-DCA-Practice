package com.dicoding.todoapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.ui.list.TaskActivity
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID
import com.google.android.material.textfield.TextInputEditText

class DetailTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        //TODO 11 : Show detail task and implement delete action
        val taskId = intent.getIntExtra(TASK_ID, 1)
        val factory = ViewModelFactory.getInstance(this)
        val taskViewModel = ViewModelProvider(this, factory)[DetailTaskViewModel::class.java]
        taskViewModel.setTaskId(taskId)
        taskViewModel.task.observe(this) {
            val title = findViewById<TextInputEditText>(R.id.detail_ed_title)
            val description = findViewById<TextInputEditText>(R.id.detail_ed_description)
            val dueDate = findViewById<TextInputEditText>(R.id.detail_ed_due_date)
            val btnDelete = findViewById<Button>(R.id.btn_delete_task)

            title.setText(it.title)
            description.setText(it.description)
            dueDate.setText(DateConverter.convertMillisToString(it.dueDateMillis))

            btnDelete.setOnClickListener {
                taskViewModel.task.removeObservers(this)
                taskViewModel.deleteTask()

                this.startActivity(Intent(this, TaskActivity::class.java))
            }
        }
    }
}