package ro.code4.deurgenta.repositories

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ro.code4.deurgenta.data.dao.UserDao
import ro.code4.deurgenta.data.model.LocationType
import ro.code4.deurgenta.data.model.response.toModel
import ro.code4.deurgenta.services.UserService

class UserRepository(
    private val userDao: UserDao,
    private val userService: UserService,
) {

    fun refreshAndFetchLocationTypes(): Observable<List<LocationType>> {
        val updateTask = userService.fetchLocationTypes()
            .map { apiTypes ->
                val mappedTypes = apiTypes.map { it.toModel() }
                if (mappedTypes.isNotEmpty()) {
                    userDao.updateLocationTypes(mappedTypes)
                }
                mappedTypes
            }
            .onErrorResumeWith(Single.just(emptyList()))
        return userDao.getLocationTypes().startWith(updateTask)
    }

    fun refreshLocationTypes(): Observable<Boolean> {
        return userService.fetchLocationTypes()
            .map { apiTypes ->
                if (apiTypes.isNotEmpty()) {
                    userDao.updateLocationTypes(apiTypes.map { it.toModel() })
                    true
                } else {
                    false
                }
            }.toObservable()
    }
}
