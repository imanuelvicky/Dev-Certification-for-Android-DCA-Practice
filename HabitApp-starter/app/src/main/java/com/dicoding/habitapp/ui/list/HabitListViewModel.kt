package com.dicoding.habitapp.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.data.HabitRepository
import com.dicoding.habitapp.utils.Event
import com.dicoding.habitapp.utils.HabitSortType

class HabitListViewModel(private val habitRepository: HabitRepository) : ViewModel() {

    private val _sort = MutableLiveData<HabitSortType>()

    val habits: LiveData<PagingData<Habit>> = _sort.switchMap {
        habitRepository.getHabits(it)
    }.cachedIn(viewModelScope)

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _undo = MutableLiveData<Event<Habit>>()
    val undo: LiveData<Event<Habit>> = _undo

    init {
        _sort.value = HabitSortType.START_TIME
    }

    fun sort(sortType: HabitSortType) {
        _sort.value = sortType
    }

    fun deleteHabit(habit: Habit) {
        habitRepository.deleteHabit(habit)
        _snackbarText.value = Event(R.string.habit_deleted)
        _undo.value = Event(habit)
    }

    fun insert(habit: Habit) {
        habitRepository.insertHabit(habit)
    }
}