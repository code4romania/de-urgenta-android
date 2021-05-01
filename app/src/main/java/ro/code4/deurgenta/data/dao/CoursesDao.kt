package ro.code4.deurgenta.data.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Observable
import ro.code4.deurgenta.data.model.CourseFilterValues

@Dao
interface CoursesDao {

    @Query("SELECT type, location FROM courses")
    fun getFilterOptions(): Observable<List<CourseFilterValues>>
}