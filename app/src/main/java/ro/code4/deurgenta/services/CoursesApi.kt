package ro.code4.deurgenta.services

import io.reactivex.Single
import retrofit2.http.GET
import ro.code4.deurgenta.data.model.Course

interface CoursesApi {

    @GET("courses")
    fun getCourses(): Single<List<Course>>
}