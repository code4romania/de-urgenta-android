package ro.code4.deurgenta.repositories

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import ro.code4.deurgenta.data.dao.GroupDao
import ro.code4.deurgenta.data.model.requests.CreateGroupRequest
import ro.code4.deurgenta.services.GroupService

class GroupRepository(
    private val groupService: GroupService,
    private val groupDao: GroupDao
) {
    fun createGroup(name: String): Completable =
        groupService.createGroup(CreateGroupRequest(name))
            .flatMapCompletable(groupDao::save)
            .subscribeOn(Schedulers.io())
}
