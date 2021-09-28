package ro.code4.deurgenta.services

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import ro.code4.deurgenta.data.model.Course

interface CourseService {
    @GET("courses")
    fun getCourses(): Single<List<Course>>
}
