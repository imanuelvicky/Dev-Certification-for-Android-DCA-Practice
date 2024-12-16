package com.dicoding.courseschedule

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.dicoding.courseschedule.ui.add.AddCourseActivity
import com.dicoding.courseschedule.ui.home.HomeActivity
import com.dicoding.courseschedule.ui.list.ListActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddCourseTest {
    @get:Rule
    var homeActivityRule = ActivityTestRule(HomeActivity::class.java)

    @Before
    fun start(){
        Intents.init()
    }

    @Test
    fun addHomeTaskTest() {
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.action_add)).perform(ViewActions.click())

        Intents.intended(IntentMatchers.hasComponent(AddCourseActivity::class.java.name))
    }

    @Test
    fun addListTaskTest() {
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.action_list)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(ListActivity::class.java.name))

        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(AddCourseActivity::class.java.name))
    }

    @After
    fun finish(){
        Intents.release()
    }
}