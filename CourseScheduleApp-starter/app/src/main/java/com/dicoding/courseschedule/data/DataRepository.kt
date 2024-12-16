package com.dicoding.courseschedule.data

import android.content.Context
import android.graphics.pdf.PdfDocument.Page
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dicoding.courseschedule.util.QueryType
import com.dicoding.courseschedule.util.QueryUtil.nearestQuery
import com.dicoding.courseschedule.util.QueryUtil.sortedQuery
import com.dicoding.courseschedule.util.SortType
import com.dicoding.courseschedule.util.executeThread
import java.util.concurrent.Flow

//TODO 4 : Implement repository with appropriate dao
class DataRepository(private val dao: CourseDao) {

    fun getNearestSchedule(queryType: QueryType) : LiveData<Course?> {
        return dao.getNearestSchedule(nearestQuery(queryType))
    }

    fun getAllCourse(sortType: SortType): LiveData<PagingData<Course>> {
        val pagerFlow = Pager(
             config = PagingConfig(
                 pageSize = PAGE_SIZE,
                 enablePlaceholders = true,
                 initialLoadSize = PAGE_SIZE
             ),
            pagingSourceFactory = { dao.getAll(sortedQuery(sortType)) }
        ).flow

        return pagerFlow.asLiveData()
    }

    fun getCourse(id: Int) : LiveData<Course> {
        return dao.getCourse(id)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getTodaySchedule() : List<Course> {
        val calendar = Calendar.getInstance()
        return dao.getTodaySchedule(calendar.get(Calendar.DAY_OF_WEEK))
    }

    fun insert(course: Course) = executeThread {
        dao.insert(course)
    }

    fun delete(course: Course) = executeThread {
        dao.delete(course)
    }

    companion object {
        @Volatile
        private var instance: DataRepository? = null
        private const val PAGE_SIZE = 10

        fun getInstance(context: Context): DataRepository? {
            return instance ?: synchronized(DataRepository::class.java) {
                if (instance == null) {
                    val database = CourseDatabase.getInstance(context)
                    instance = DataRepository(database.courseDao())
                }
                return instance
            }
        }
    }
}