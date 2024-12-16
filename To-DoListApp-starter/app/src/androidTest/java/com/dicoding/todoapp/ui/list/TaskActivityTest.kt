package com.dicoding.todoapp.ui.list

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.add.AddTaskActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

//TODO 16 : Write UI test to validate when user tap Add Task (+), the AddTaskActivity displayed
class TaskActivityTest {
    @get:Rule
    var taskActivityRule = ActivityTestRule(TaskActivity::class.java)

    @Before
    fun start(){
        Intents.init()
    }

    @Test
    fun addTaskTest() {
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click())

        Intents.intended(IntentMatchers.hasComponent(AddTaskActivity::class.java.name))
    }

    @After
    fun finish(){
        Intents.release()
    }
}