package ro.code4.deurgenta.repositories

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import ro.code4.deurgenta.data.dao.GroupDao
import ro.code4.deurgenta.data.model.Group
import ro.code4.deurgenta.data.model.GroupMember
import ro.code4.deurgenta.data.model.requests.CreateGroupRequest
import ro.code4.deurgenta.data.model.response.toModelGroup
import ro.code4.deurgenta.data.model.response.toModelGroupMember
import ro.code4.deurgenta.services.GroupService

class GroupRepository(
    private val groupService: GroupService,
    private val groupDao: GroupDao
) {

    fun createGroup(name: String): Completable =
        groupService.createGroup(CreateGroupRequest(name))
            .flatMapCompletable(groupDao::save)
            .subscribeOn(Schedulers.io())

    fun fetchAndRefreshGroups(): Observable<List<Group>> {
        return groupService.getMyGroups()
            .map { it.map { entry -> entry.toModelGroup() } }
            .flatMapObservable {
                groupDao.saveSync(it)
                groupDao.list()
            }
    }

    fun fetchAndRefreshGroupMembers(groupId: String): Observable<List<GroupMember>> {
        return groupService.getMembersOfGroup(groupId)
            .map { it.map { entry -> entry.toModelGroupMember() } }
            .flatMapObservable {
                groupDao.saveMembersSync(groupId, it)
                groupDao.listMembers(groupId).map { groupWithMembersList ->
                    groupWithMembersList[0].members
                }
            }
    }
}
