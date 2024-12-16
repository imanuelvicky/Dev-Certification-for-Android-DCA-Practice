package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.list.ListViewModelFactory
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCourseActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, TimePickerFragment.DialogTimeListener {

    private var selectedDay: Int = 0
    private lateinit var viewModel: AddCourseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        viewModel = ViewModelProvider(this, ListViewModelFactory.createFactory(this))[AddCourseViewModel::class.java]

        val ibStart = findViewById<ImageButton>(R.id.ib_start_time)
        val ibEnd = findViewById<ImageButton>(R.id.ib_end_time)
        val spinner = findViewById<Spinner>(R.id.spinner_day)

        ibStart.setOnClickListener {
            TimePickerFragment().show(supportFragmentManager, START)
        }
        ibEnd.setOnClickListener {
            TimePickerFragment().show(supportFragmentManager, END)
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.day,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = it
        }
        spinner.onItemSelectedListener = this

        viewModel.saved.observe(this) {
            val handled = it.getContentIfNotHandled()
            if (handled != null){
                if(!handled){
                    Toast.makeText(this, "The empty field have to be filled", Toast.LENGTH_SHORT).show()
                } else {
                    finish()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val name = findViewById<TextInputEditText>(R.id.ed_course_name).text.toString()
        val start = findViewById<TextView>(R.id.tv_start_time).text.toString()
        val end = findViewById<TextView>(R.id.tv_end_time).text.toString()
        val lecturer = findViewById<TextInputEditText>(R.id.ed_lecturer).text.toString()
        val note = findViewById<TextInputEditText>(R.id.ed_note).text.toString()
        when (item.itemId) {
            R.id.action_insert -> {
                viewModel.insertCourse(
                    courseName = name,
                    day = selectedDay,
                    startTime = start,
                    endTime = end,
                    lecturer = lecturer,
                    note = note
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedDay = position
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        when (tag) {
            START -> findViewById<TextView>(R.id.tv_start_time).text = dateFormat.format(calendar.time)
            END -> findViewById<TextView>(R.id.tv_end_time).text = dateFormat.format(calendar.time)
        }
    }

    companion object {
        private const val START = "start"
        private const val END = "end"
    }
}