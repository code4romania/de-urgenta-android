package ro.code4.deurgenta.repositories

import android.annotation.SuppressLint
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Retrofit
import ro.code4.deurgenta.data.AppDatabase
import ro.code4.deurgenta.data.model.Course
import ro.code4.deurgenta.data.model.CourseFilterValues
import ro.code4.deurgenta.data.model.MapAddress
import ro.code4.deurgenta.data.model.Register
import ro.code4.deurgenta.data.model.User
import ro.code4.deurgenta.data.model.response.LoginResponse
import ro.code4.deurgenta.services.AccountService
import ro.code4.deurgenta.services.CourseService
import java.util.*
import java.util.concurrent.TimeUnit

class Repository : KoinComponent {

    private val db: AppDatabase by inject()

    private val retrofit: Retrofit by inject()
    private val accountService: AccountService by lazy {
        retrofit.create(AccountService::class.java)
    }

    private val courseService: CourseService by lazy { retrofit.create(CourseService::class.java) }

    private val disposables: CompositeDisposable = CompositeDisposable()

    fun register(data: Register): Observable<String> = accountService.register(data)

    fun login(user: User): Observable<LoginResponse> = accountService.login(user)

    fun saveAddress(mapAddress: MapAddress): Completable {
        return Completable
            .fromAction {
                db.addressDao().save(mapAddress)
            }.subscribeOn(Schedulers.io())
    }

    // COURSES related code start
    @SuppressLint("CheckResult")
    fun fetchFilterOptions(): Observable<List<CourseFilterValues>> {
        // return db.coursesDao().getFilterOptions().delay(1, TimeUnit.SECONDS)
        // use this until the backend is available
        return Observable.just(tempCourses.map { CourseFilterValues(it.type, it.location) })
            .delay(1, TimeUnit.SECONDS)
    }

    fun fetchCoursesFor(type: String, city: String): Observable<List<Course>> {
        return Observable.just(
            tempCourses.filter {
                it.type == type && it.location.lowercase(Locale.getDefault())
                    .contains(city.lowercase(Locale.getDefault()))
            }
        ).delay(1, TimeUnit.SECONDS)
    }
    // COURSES related code end
}
