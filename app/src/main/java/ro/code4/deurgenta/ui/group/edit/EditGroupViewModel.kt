package ro.code4.deurgenta.ui.group.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.disposables.Disposable
import ro.code4.deurgenta.data.model.Group
import ro.code4.deurgenta.data.model.GroupMember
import ro.code4.deurgenta.data.model.fullName
import ro.code4.deurgenta.helper.SchedulersProvider
import ro.code4.deurgenta.helper.logD
import ro.code4.deurgenta.helper.logE
import ro.code4.deurgenta.helper.plusAssign
import ro.code4.deurgenta.repositories.GroupRepository
import ro.code4.deurgenta.ui.base.BaseViewModel

class EditGroupViewModel(
    private val groupRepository: GroupRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel() {

    private val _groupMembersState = MutableLiveData<GroupEditContentState>()
    val groupMembersState: LiveData<GroupEditContentState> = _groupMembersState
    private val _membersCount = MutableLiveData<GroupCount>()
    val membersCount: LiveData<GroupCount> = _membersCount
    private var currentJob: Disposable? = null
    private var groupDisposable: Disposable? = null

    fun fetchGroupMembers(group: Group) {
        _membersCount.value = GroupCount(group.numberOfMembers, group.maxNumberOfMembers)
        _groupMembersState.value = GroupEditContentState.Loading
        currentJob?.dispose()
        currentJob = groupRepository.fetchAndRefreshGroupMembers(group.id)
            .subscribeOn(schedulersProvider.io())
            .map { membersList ->
                membersList.apply {
                    map { member -> member.isGroupAdmin } // admin is always first
                }
            }
            .observeOn(schedulersProvider.main())
            .subscribe(
                { _groupMembersState.value = GroupEditContentState.Success(it) },
                { _groupMembersState.value = GroupEditContentState.Error(it) }
            )
    }

    fun remove(group: Group, groupMember: GroupMember) {
        disposables += groupRepository.deleteMemberFromGroup(group, groupMember)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.main())
            .subscribe(
                {
                    logD("Success: removeMember(${group.name}, ${groupMember.fullName})")
                    val newCount = _membersCount.value?.let {
                        if (it.nrOfMembers - 1 < 0) {
                            0
                        } else {
                            it.nrOfMembers - 1
                        }
                    }
                    _membersCount.value = newCount?.let { _membersCount.value?.copy(nrOfMembers = it) }
                },
                { logE("Failed: removeMember(${group.name}, ${groupMember.fullName})", it) }
            )
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.dispose()
        groupDisposable?.dispose()
    }
}

sealed class GroupEditContentState {
    object Loading : GroupEditContentState()
    data class Success(val data: List<GroupMember>) : GroupEditContentState()
    data class Error(val exception: Throwable) : GroupEditContentState()
}

data class GroupCount(val nrOfMembers: Int, val maxNrOfMembers: Int)

val GroupCount.hasRoomForMembers
    get() = nrOfMembers < maxNrOfMembers

val GroupCount.countDisplay: String
    get() = "($nrOfMembers/$maxNrOfMembers)"
