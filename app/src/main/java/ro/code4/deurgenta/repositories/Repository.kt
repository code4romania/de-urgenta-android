package ro.code4.deurgenta.repositories

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Retrofit
import ro.code4.deurgenta.data.AppDatabase
import ro.code4.deurgenta.data.model.*
import ro.code4.deurgenta.data.model.MapAddress
import ro.code4.deurgenta.data.model.Register
import ro.code4.deurgenta.data.model.User
import ro.code4.deurgenta.data.model.response.LoginResponse
import ro.code4.deurgenta.data.model.response.RegisterResponse
import ro.code4.deurgenta.services.ApiInterface
import ro.code4.deurgenta.services.BackpackInterface

class Repository : KoinComponent {

    companion object {
        @JvmStatic
        val TAG = Repository::class.java.simpleName
    }

    private val retrofit: Retrofit by inject()
    private val db: AppDatabase by inject()
    private val apiInterface: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
    private val backpackInterface: BackpackInterface by lazy {
        retrofit.create(BackpackInterface::class.java)
    }
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun login(user: User): Observable<LoginResponse> = apiInterface.login(user)

    fun register(data: Register): Observable<RegisterResponse> = apiInterface.register(data)

    // BACKPACK related code only start
    // TODO actually connect backpack related calls to the backend
    fun getBackpacks(): Observable<List<Backpack>> = db.backpackDao().getAllBackpacks()

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
    ): Observable<List<BackpackItem>> {
        return db.backpackDao().getItemsForType(backpack.id, type)
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
}

