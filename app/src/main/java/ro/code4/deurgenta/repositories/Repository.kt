package ro.code4.deurgenta.repositories

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Retrofit
import ro.code4.deurgenta.data.AppDatabase
import ro.code4.deurgenta.data.model.*
import ro.code4.deurgenta.data.model.response.LoginResponse
import ro.code4.deurgenta.services.AuthService
import ro.code4.deurgenta.services.BackpackService
import ro.code4.deurgenta.services.CourseService
import java.util.*
import java.util.concurrent.TimeUnit

class Repository : KoinComponent {

    companion object {
        @JvmStatic
        val TAG = Repository::class.java.simpleName
    }

    private val db: AppDatabase by inject()
    private val backpackDao by lazy { db.backpackDao() }

    private val retrofit: Retrofit by inject()
    private val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }
    private val backpackService: BackpackService by lazy {
        retrofit.create(BackpackService::class.java)
    }
    private val courseService: CourseService by lazy { retrofit.create(CourseService::class.java) }

    private val disposables: CompositeDisposable = CompositeDisposable()

    fun register(data: Register): Observable<String> = authService.register(data)

    fun login(user: User): Observable<LoginResponse> = authService.login(user)

    // BACKPACK related code only start
    // Based on https://developer.android.com/jetpack/guide#persist-data

    fun getBackpacks(): Single<List<Backpack>> {
        val observableDb = backpackDao.getAllBackpacks()
        val observableApi = backpackService.getBackpacks()
        return Single.zip(
            observableDb,
            observableApi.onErrorReturnItem(emptyList()))
            { backpacksDb, backpacksApi ->
                val areAllBackpacksInDb = backpacksDb.containsAll(backpacksApi)
                if (!areAllBackpacksInDb) {
                    backpackDao.saveBackpack(*backpacksApi.toTypedArray())
                }
                // No data loaded from back end
                if (backpacksApi.isEmpty()) {
                    // Return cached backpacks
                    backpacksDb
                } else {
                    // Return the fresher data
                    backpacksApi
                }
            }
    }

    fun saveNewBackpack(backpack: Backpack) {
        disposables.add(
            Completable.fromAction { db.backpackDao().saveBackpack(backpack) }
                .subscribeOn(Schedulers.io()).subscribe({
                    Log.i(TAG, "saveNewBackpack($backpack)")
                }, { Log.e(TAG, "saveNewBackpack($backpack): Error($it)!") })
        )
    }

    fun getItemForBackpackType(
        backpack: Backpack,
        type: BackpackItemType
    ): Single<List<BackpackItem>> {
        return backpackDao.getItemsForType(backpack.id, type)
    }

    fun deleteBackpackItem(itemId: String) {
        disposables.add(
            Completable.fromAction { db.backpackDao().deleteItem(itemId) }
                .subscribeOn(Schedulers.io()).subscribe({
                    Log.i(TAG, "deleteBackpackItem($itemId)")
                }, { Log.e(TAG, "deleteBackpackItem($itemId): Error($it)!") })
        )
    }

    fun saveNewBackpackItem(backpackItem: BackpackItem) {
        disposables.add(
            Completable.fromAction { db.backpackDao().saveBackpackItem(backpackItem) }
                .subscribeOn(Schedulers.io()).subscribe({
                    Log.i(TAG, "saveNewBackpackItem($backpackItem): Success!")
                }, {
                    Log.e(TAG, "saveNewBackpackItem($backpackItem): Error($it)!")
                })
        )
    }

    fun updateBackpackItem(backpackItem: BackpackItem) {
        disposables.add(
            Completable.fromAction { db.backpackDao().saveBackpackItem(backpackItem) }
                .subscribeOn(Schedulers.io()).subscribe({
                    Log.i(TAG, "updateNewBackpackItem($backpackItem): Success!")
                }, {
                    Log.e(TAG, "updateNewBackpackItem($backpackItem): Error($it)!")
                })
        )
    }

    // BACKPACK related code only end

    fun saveAddress(mapAddress: MapAddress): Completable {
        return Completable
            .fromAction {
                db.addressDao().save(mapAddress)
            }.subscribeOn(Schedulers.io())
    }

    // COURSES related code start
    @SuppressLint("CheckResult")
    fun fetchFilterOptions(): Observable<List<CourseFilterValues>> {
        //return db.coursesDao().getFilterOptions().delay(1, TimeUnit.SECONDS)
        // use this until the backend is available
        return Observable.just(tempCourses.map { CourseFilterValues(it.type, it.location) })
            .delay(1, TimeUnit.SECONDS)
    }

    fun fetchCoursesFor(type: String, city: String): Observable<List<Course>> {
        return Observable.just(tempCourses.filter {
            it.type == type && it.location.toLowerCase(Locale.getDefault())
                .contains(city.toLowerCase(Locale.getDefault()))
        }).delay(1, TimeUnit.SECONDS)
    }
    // COURSES related code end
}


